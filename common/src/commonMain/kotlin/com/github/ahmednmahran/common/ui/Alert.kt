package com.github.ahmednmahran.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * create by @author AhmedNMahran
 */
@Composable
fun Alert(alert: String) {
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