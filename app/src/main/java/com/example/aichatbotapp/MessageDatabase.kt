package com.example.aichatbotapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aichatbotapp.data.Message
import com.example.aichatbotapp.data.MessageDao

@Database(
    entities = [Message::class],
    version = 1,
    exportSchema = true
)
abstract class MessageDatabase:RoomDatabase() {
    abstract fun messageDao(): MessageDao
}