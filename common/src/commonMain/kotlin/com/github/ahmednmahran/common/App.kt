package com.github.ahmednmahran.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty

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

    var text by remember { mutableStateOf("Send Message!") }
    var sentMessage by remember { mutableStateOf(TextFieldValue("your Message!")) }
    var receivedMessage by remember { mutableStateOf("received") }
    val list = mutableListOf<String>()
    var chat by mutableStateOf(list)
    val platformName = getPlatformName()
    if (!job.isActive)
        job.start()

    suspend fun startChat(wsClient: WsClient) {
        try {
            wsClient.receive {
//                writeMessage(it)
                list.add(it)
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

    Column {

        LazyColumn(Modifier.weight(1f)) {
            items(items = chat + listOf("hello","Ahmed", " Salaaaaam"), itemContent = { item ->
                Card(
                    modifier = Modifier.width(80.dp).height(30.dp).background(color = Color.Yellow)
                ) {
                    Text(item)
                }

            })
        }
        Row (verticalAlignment = Alignment.Bottom){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.7f).then(Modifier.padding(30.dp)),
                value = sentMessage,
                onValueChange = {
                    sentMessage = it

                })
            Spacer(modifier = Modifier.width(30.dp))
            Button(modifier = Modifier.height(60.dp),
                onClick = {
                    GlobalScope.launch {
                        sendMessage(wsClient, sentMessage.text)
                    }
                }) {
                Text("Send")
            }
        }

    }
    //endregion


//    Column {
//        Card {
//            Text(text = receivedMessage)
//        }
//        Row {
//
//            Button(onClick = {
//                text = "Send, ${platformName}"
//                GlobalScope.launch {
//                    sendMessage(wsClient, sentMessage.text)
//                }
//
//            }) {
//                Text(text)
//            }
//            OutlinedTextField(
//                modifier = Modifier.fillMaxWidth(0.7f).then(Modifier.padding(30.dp)),
//                value = sentMessage,
//                onValueChange = {
//                    sentMessage = it
//
//                })
//
//        }
//
//    }
}


fun receiveFlow(message: String) = flow<String> {
    emit(message)
}

suspend fun initConnection(wsClient: WsClient) {

    try {
        wsClient.connect()
        wsClient.receive {
            writeMessage(it)
            receiveFlow(it)
        }
    } catch (e: Exception) {
        if (e is ClosedReceiveChannelException) {
            writeMessage("Disconnected. ${e.message}.")
        } else if (e is WebSocketException) {
            writeMessage("Unable to connect.")
        }
        withTimeout(5000) {
            GlobalScope.launch { initConnection(wsClient) }
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

//suspend fun startChat() {
//    val client = HttpClient {
//        install(WebSockets)
//    }
//    client.webSocket(method = HttpMethod.Get, host = "10.0.2.2", port = 8080, path = "/chat") {
//        val messageOutputRoutine = launch { outputMessages() }
//        val userInputRoutine = launch { inputMessages() }
//
//        userInputRoutine.join() // Wait for completion; either "exit" or error
//        messageOutputRoutine.cancelAndJoin()
//    }
//
//    client.close()
//    println("Connection closed. Goodbye!")
//}
//
//var received = ""
//
//fun DefaultClientWebSocketSession.outputMessages() = flow {
//    try {
//        for (message in incoming) {
//            message as? Frame.Text ?: continue
//            emit(message.readText())
//            received = message.readText()+"\n"
//        }
//    } catch (e: Exception) {
//        emit("Error while receiving: " + e.message)
//    }
//}
//
//suspend fun DefaultClientWebSocketSession.inputMessages() {
//    while (true) {
//
//        if (inputMessage.equals("exit", true)) return
//        try {
//            send(inputMessage)
//        } catch (e: Exception) {
//            println("Error while sending: " + e.message)
//            return
//        }
//    }
//}
