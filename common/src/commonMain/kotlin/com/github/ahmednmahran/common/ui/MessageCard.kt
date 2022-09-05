package com.github.ahmednmahran.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.github.ahmednmahran.common.model.ChatMessage

enum class Position {
    LEFT, RIGHT
}
@Composable
fun MessageCard(message: ChatMessage,
                position: Position = remember {  Position.LEFT },
                modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(16.dp))
    Row {
        if(position == Position.RIGHT)
            Spacer(modifier = Modifier.weight(1f))
        Card(elevation = 8.dp, modifier = Modifier.wrapContentWidth() ) {
            Row(
                modifier = Modifier.padding(all = 8.dp)
                    .background(MaterialTheme.colors.background)
                    .clipToBounds()
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
                )
                // Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = message.sender,
                        color = MaterialTheme.colors.secondaryVariant,
                        style = MaterialTheme.typography.subtitle2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

