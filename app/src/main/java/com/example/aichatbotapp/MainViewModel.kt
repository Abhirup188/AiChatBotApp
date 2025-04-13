package com.example.aichatbotapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aichatbotapp.data.Message
import com.example.aichatbotapp.data.MessageRepository
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class MainViewModel(private val messageRepository: MessageRepository=Graph.messageRepository): ViewModel() {
    var geminiUiState:GeminiUiState by mutableStateOf(GeminiUiState.WelcomeState)
        private set
    var message by mutableStateOf("")
        private set
    var messageList by mutableStateOf(listOf<String>())
        private set
    var geminiMessageList by mutableStateOf(listOf<String>())
        private set
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun onMessageChanged(newMessage:String){
        message = newMessage
    }
    fun sendFullMessage(){
        messageList = messageList+message
        sendGeminiMessage(message)
        message=""
    }
    fun sendGeminiMessage(prompt:String){
        viewModelScope.launch {
            try {
                val result = generativeModel.generateContent(prompt)
                geminiUiState = GeminiUiState.Success(result.text.toString())
                geminiMessageList = geminiMessageList+result.text.toString()
                saveMessage(Message(text = result.text.toString(), isFromUser = false))
                saveMessage(Message(text = prompt, isFromUser = true))
            }catch (e:Exception){
                e.printStackTrace()
                geminiUiState = GeminiUiState.Error
            }
        }
    }
    private fun saveMessage(message: Message){
        viewModelScope.launch {
            messageRepository.insertMessage(message)
        }
    }
    init {
        loadMessage()
    }
    private fun loadMessage() {
        viewModelScope.launch {
            messageRepository.getAllMessages().collect { messages ->
                messageList = messages.filter { it.isFromUser }.map { it.text }
                geminiMessageList = messages.filter { !it.isFromUser }.map { it.text }
            }
        }
    }
}