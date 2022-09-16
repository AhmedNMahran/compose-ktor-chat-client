package com.github.ahmednmahran.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.github.ahmednmahran.common.domain.ChatRepository
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.ui.Alert
import com.github.ahmednmahran.common.ui.MessageCard
import com.github.ahmednmahran.common.ui.MessageComposer
import com.github.ahmednmahran.common.ui.Position
import kotlinx.coroutines.flow.collect

/**
 * created by @author AhmedNMahran
 */
@Composable
fun App(chatRepository: ChatRepository) {
    var alert = chatRepository.alert.collectAsState("")
    val user = chatRepository.user.collectAsState(null)
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
                ProfileImage()
                LazyColumn(Modifier.weight(1f).background(MaterialTheme.colors.surface)) {
                    items(items = list, itemContent = { chatMessage ->
                        MessageCard(chatMessage,
                            if (chatMessage.sender == user.value?.username)
                                Position.LEFT
                            else
                                Position.RIGHT
                        )
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

