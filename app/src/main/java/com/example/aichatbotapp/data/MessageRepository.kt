package com.example.aichatbotapp.data

import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {

    suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message)
    }
    fun getAllMessages(): Flow<List<Message>> =messageDao.getAllMessages()

    suspend fun deleteMessage(message: Message) {
        messageDao.deleteMessage(message)
    }
}