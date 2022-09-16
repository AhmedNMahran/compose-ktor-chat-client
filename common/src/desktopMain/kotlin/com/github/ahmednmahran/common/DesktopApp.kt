// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.ahmednmahran.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.singleWindowApplication
import com.github.ahmednmahran.common.domain.ChatRepository

@Preview
@Composable
fun AppPreview() {
    App(ChatRepository())
}

fun main() = singleWindowApplication {
    App(ChatRepository())
}