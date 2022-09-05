package com.github.ahmednmahran.common.ui

import androidx.compose.foundation.layout.*
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

    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.height(60.dp)) {
        var sentMessage by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            value = sentMessage,
            onValueChange = {
                sentMessage = it
            })
        Button(modifier = Modifier.fillMaxHeight(),
            onClick = {
                onButtonClick(sentMessage)
                sentMessage  = ""
            }) {
            Text(" Send ")
        }
    }
}