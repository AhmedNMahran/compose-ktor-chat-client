package com.github.ahmednmahran.android

import com.github.ahmednmahran.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.ahmednmahran.common.model.ChatMessage
import com.github.ahmednmahran.common.ui.MessageCard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun MessagePreview(){
    MessageCard(ChatMessage("Assalamu Alaykom","Ahmed"))
}