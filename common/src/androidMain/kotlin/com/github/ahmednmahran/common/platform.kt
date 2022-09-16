package com.github.ahmednmahran.common
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage

actual fun getHost(): String {
    return "10.0.2.2"
}
const val url = "https://i.picsum.photos/id/165/200/302.jpg?hmac=eYgfgzPdn3dVLQuqpzLCvE0GkTlw9QDQdMAU15Nhgxg"
@Composable
actual fun ProfileImage() {
    println("android image")
    AsyncImage(
        model = url,
        contentDescription = null
    )
}