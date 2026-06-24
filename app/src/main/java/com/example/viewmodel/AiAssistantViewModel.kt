package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(val role: String, val content: String)

class AiAssistantViewModel(
    private val geminiService: GeminiService = GeminiService()
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun sendMessage(query: String, context: String? = null) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _messages.update { it + ChatMessage("user", query) }
            _isLoading.update { true }

            val response = geminiService.askAssistant(query, context)

            _messages.update { it + ChatMessage("ai", response) }
            _isLoading.update { false }
        }
    }
}
