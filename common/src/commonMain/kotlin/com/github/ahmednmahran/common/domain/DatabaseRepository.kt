package com.github.ahmednmahran.domain

import com.github.ahmednmahran.common.model.ChatUser


/**
 * Repository to access and make database operations
 */
object DatabaseRepository {
    fun getUsers() = dummyUsernames.map {
        ChatUser(it,"","")
    }
    private val dummyUsernames = listOf("Ahmed","Mohamed", "Hoda","Abdullah","Aly", "Somaya","Hend")
}