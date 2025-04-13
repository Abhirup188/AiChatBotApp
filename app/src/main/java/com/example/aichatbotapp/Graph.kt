package com.example.aichatbotapp

import android.content.Context
import androidx.room.Room
import com.example.aichatbotapp.data.MessageRepository

object Graph {
    lateinit var database: MessageDatabase

    val messageRepository by lazy {
        MessageRepository(messageDao = database.messageDao())
    }
    fun provide(context: Context){
        database = Room
            .databaseBuilder(context = context,MessageDatabase::class.java,"message.db")
            .build()
    }
}