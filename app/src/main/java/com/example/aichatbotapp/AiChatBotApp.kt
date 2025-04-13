package com.example.aichatbotapp

import android.app.Application

class AiChatBotApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}