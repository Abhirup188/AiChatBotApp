package com.example.aichatbotapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var text:String,
    var isFromUser:Boolean=false,
)
