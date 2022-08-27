package com.github.ahmednmahran.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.ui.MessageCard
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

import kotlinx.coroutines.flow.flow

// todo 1 change screen background
// todo 2 fix message sending logic
// todo 3 make message appear using LazyColumn

val wsClient = WsClient(HttpClient {
    install(WebSockets)
})
val job by lazy {
    GlobalScope.launch {
        wsClient.connect()
    }

}

@Composable
fun App() {

    var alert by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf(TextFieldValue("your Message!")) }
    var receivedMessage by remember { mutableStateOf("received") }
    val list = remember {  mutableListOf<ChatMessage>()}
    var chat by mutableStateOf(list)
    val platformName = getPlatformName()
    if (!job.isActive)
        job.start()

    fun extractChatMessage(it: String) {
        if (it.startsWith("[")) {
            alert = ""
            list.add(
                ChatMessage(
                    sender = it.substring(1, it.indexOf("]:"))
                        .replace("]:", ""),
                    body = it.substring(it.indexOf("]:") + 2, it.lastIndex).trim()
                )
            )
        } else {
            alert = it
        }
    }

    suspend fun startChat(wsClient: WsClient) {
        try {
            wsClient.receive {
//                writeMessage(it)
                extractChatMessage(it)
                println("startChat: $chat")
            }
        } catch (e: Exception) {
            if (e is ClosedReceiveChannelException) {
                writeMessage("Disconnected. ${e.message}.")
            } else if (e is WebSocketException) {
                writeMessage("Unable to connect.")
            }
            withTimeout(5000) {
                GlobalScope.launch { startChat(wsClient) }
            }
        }
    }

    GlobalScope.launch { startChat(wsClient) }
    //region
    MaterialTheme {

        Surface {
            if(alert.isNotBlank()){
                Alert(alert)
            }
            Column {

                LazyColumn(Modifier.weight(1f).background(MaterialTheme.colors.surface)) {
                    items(items = list, itemContent = { item ->
                        MessageCard(item)

                    })
                }
                Row (verticalAlignment = Alignment.Bottom){
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        value = sentMessage,
                        onValueChange = {
                            sentMessage = it

                        })
                    Button(modifier = Modifier.height(60.dp).fillMaxSize(),
                        onClick = {
                            GlobalScope.launch {
                                sendMessage(wsClient, sentMessage.text)
                            }
                        }) {
                        Text("Send")
                    }
                }

            }
        }
    }
    //endregion


}

@Composable
private fun Alert(alert: String) {
    Card(elevation = 8.dp) {
        Box(
            modifier = Modifier.background(MaterialTheme.colors.error).clip(
                RoundedCornerShape(16.dp)
            )
        ) {
            Text(
                text = alert,
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

suspend fun sendMessage(client: WsClient, input: String) {
    if (input.isNotEmpty()) {
        client.send(input)
    }
}


fun writeMessage(message: String, messageCallback: ((String) -> Unit) = {}) {
    messageCallback(message)
}

class WsClient(private val client: HttpClient) {
    var session: WebSocketSession? = null

    suspend fun connect() {
        session = client.webSocketSession(
            method = HttpMethod.Get,
            host = "10.0.2.2",
            port = 8080,
            path = "/chat"
        )
    }

    suspend fun send(message: String) {
        session?.send(Frame.Text(message))
    }


    suspend fun receive(onReceive: (input: String) -> Unit) {
        while (true) {
            val frame = session?.incoming?.receive()

            if (frame is Frame.Text) {
                onReceive(frame.readText())
            }
        }
    }
}

