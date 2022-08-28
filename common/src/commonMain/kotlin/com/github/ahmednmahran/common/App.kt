package com.github.ahmednmahran.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.github.ahmednmahran.common.domain.ChatRepository
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.ui.MessageCard
import kotlinx.coroutines.*

// todo 1 change screen background
// todo 2 fix message sending logic
// todo 3 make message appear using LazyColumn



var baseUrl: String? = null

@Composable
fun App(chatRepository: ChatRepository = ChatRepository()) {
    var alert = chatRepository.alert.collectAsState("")
    var sentMessage by remember { mutableStateOf(TextFieldValue("your Message!")) }
    var receivedMessage by remember { mutableStateOf("received") }
    val list = remember {  mutableListOf<ChatMessage>()}

    list.add(chatRepository.chatMessage.collectAsState(ChatMessage("","")).value)

    //region
    MaterialTheme {

        Surface {
            if(alert.value.isNotBlank()){
                Alert(alert.value)
            }
            Column {

                LazyColumn(Modifier.weight(1f).background(MaterialTheme.colors.surface)) {
                    items(items = list, itemContent = { item ->
                        MessageCard(item)
                    })
                }
                Row (verticalAlignment = Alignment.Bottom){
                    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        value = sentMessage,
                        onValueChange = {
                            textFieldValue = it
                            sentMessage = it
                        })
                    Button(modifier = Modifier.height(60.dp).fillMaxSize(),
                        onClick = {
                            GlobalScope.launch {
                                sentMessage = textFieldValue
                                chatRepository.send(sentMessage.text)
                                textFieldValue = TextFieldValue("")
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

