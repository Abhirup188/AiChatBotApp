package com.example.aichatbotapp

sealed class GeminiUiState {
    object WelcomeState:GeminiUiState()
    object Error:GeminiUiState()
    data class Success(val result:String):GeminiUiState()

}