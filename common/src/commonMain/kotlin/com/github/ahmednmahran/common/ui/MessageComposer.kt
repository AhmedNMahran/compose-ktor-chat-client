package com.github.ahmednmahran.common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Created by @author AhmedNMahran
 * use this to send a message
 * @param onButtonClick : callback to be called when send button is clicked
 */
@Composable
fun MessageComposer(
    onButtonClick: (String) -> Unit = {}
) {

    Row(verticalAlignment = Alignment.Bottom) {
        var sentMessage by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = sentMessage,
            onValueChange = {
                sentMessage = it
            })
        Button(modifier = Modifier.height(60.dp).fillMaxSize(),
            onClick = {
                onButtonClick(sentMessage)
                sentMessage  = ""
            }) {
            Text("Send")
        }
    }
}