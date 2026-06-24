package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ai.GeminiService
import com.example.data.repository.IdeaRepository

class IdeaViewModelFactory(
    private val repository: IdeaRepository,
    private val geminiService: GeminiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IdeaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IdeaViewModel(repository, geminiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
