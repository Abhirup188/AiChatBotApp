package com.example.aichatbotapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<Message>>

    @Delete
    suspend fun deleteMessage(message: Message)

}