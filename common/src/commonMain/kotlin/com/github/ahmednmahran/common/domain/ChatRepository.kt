package com.github.ahmednmahran.common.domain


import com.github.ahmednmahran.common.getHost
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.model.ChatUser
import com.github.ahmednmahran.domain.DatabaseRepository
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatRepository(private val chatUser: ChatUser
= DatabaseRepository.getUsers().random(), private val host: String = getHost()
) {
    private val client: HttpClient by lazy {
        HttpClient {
            install(Auth){
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = chatUser.username,
                            password = chatUser.password
                        )
                    }
                    realm = "Access to the '/' path"
                }
            }
            install(WebSockets)
        }
    }
    private val _chatMessage = MutableStateFlow(ChatMessage("",""))
    val chatMessage: StateFlow<ChatMessage> = _chatMessage

    private var _alert = MutableStateFlow("")
    val alert: StateFlow<String> = _alert

    private var _user = MutableStateFlow<ChatUser?>(null)
    val user = _user

    private val _job by lazy {
        CoroutineScope(Dispatchers.Default).launch {
            val response: HttpResponse = client.post("login") {
                host = this@ChatRepository.host
                port = 8080
            }
            println(response.bodyAsText())
            connect()
            startChat()
        }

    }
    private var _session: WebSocketSession? = null

    init {
        if (!_job.isActive)
            _job.start()
    }

    private suspend fun connect() {
        _session = client.webSocketSession(
            method = HttpMethod.Get,
            host = host,
            port = 8080,
            path = "/chat"
        )
        _user.value = chatUser
    }

    private suspend fun startChat() {
        try {
            receive()
        } catch (e: Exception) {
            if (e is ClosedReceiveChannelException) {
                _alert.emit("Disconnected. ${e.message}.")
            } else if (e is WebSocketException) {
                _alert.emit("Unable to connect.")
            }
            withTimeout(5000) {
                CoroutineScope(Dispatchers.Default).launch { startChat() }
            }
        }
    }

    fun send(message: String) {
        CoroutineScope(Dispatchers.Default).launch {
            _session?.send(Frame.Text(message))
        }
    }


    private suspend fun receive() {
        while (true) {
            val frame = _session?.incoming?.receive()
            if (frame is Frame.Text) {
                extractChatMessage(frame.readText())
            }
        }
    }

    private suspend fun extractChatMessage(it: String) {
        println("extract: $it")
        try{
            _chatMessage.emit(Json.decodeFromString(it))
            _alert.emit("")
        }catch (th: Throwable){
            _alert.emit(it)
        }
    }


}