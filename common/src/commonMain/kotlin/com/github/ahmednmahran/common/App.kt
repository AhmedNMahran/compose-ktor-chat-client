package com.github.ahmednmahran.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.github.ahmednmahran.common.domain.ChatRepository
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.ui.Alert
import com.github.ahmednmahran.common.ui.MessageCard
import com.github.ahmednmahran.common.ui.MessageComposer
import kotlinx.coroutines.*

// todo 1 change screen background
// todo 2 fix message sending logic
// todo 3 make message appear using LazyColumn


/**
 * created by @author AhmedNMahran
 */
@Composable
fun App(chatRepository: ChatRepository) {
    var alert = chatRepository.alert.collectAsState("")
    var user = chatRepository.user.collectAsState(null)
    var receivedMessage by remember { mutableStateOf("received") }
    val list = remember {  mutableListOf<ChatMessage>()}
    chatRepository.chatMessage.collectAsState(ChatMessage("","")).value.let {
        if(it.body.isNotBlank() && it.sender.isNotBlank())
            list.add(it)
    }

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
                MessageComposer{
                    chatRepository.send(it)
                }

            }
        }
    }
    //endregion


}

