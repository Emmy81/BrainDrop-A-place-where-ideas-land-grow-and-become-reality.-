package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.data.models.Idea
import com.example.data.repository.IdeaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class IdeaViewModel(
    private val repository: IdeaRepository,
    private val geminiService: GeminiService = GeminiService()
) : ViewModel() {

    val allIdeas: StateFlow<List<Idea>> = repository.allIdeas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()

    private val _aiResponse = MutableStateFlow<String?>(null)
    val aiResponse = _aiResponse.asStateFlow()

    fun insertIdea(title: String, description: String, category: String) {
        viewModelScope.launch {
            repository.insert(
                Idea(
                    title = title,
                    description = description,
                    category = category
                )
            )
        }
    }

    fun analyzeIdea(title: String, description: String, category: String) {
        viewModelScope.launch {
            _isAnalyzing.update { true }
            _aiResponse.update { null }
            val response = geminiService.analyzeIdea(title, description, category)
            _aiResponse.update { response }
            _isAnalyzing.update { false }
        }
    }
    
    fun getIdeaById(id: Int): kotlinx.coroutines.flow.Flow<Idea?> {
        return repository.getIdeaById(id)
    }

    fun updateIdeaAiResponse(idea: Idea, aiResponse: String) {
        viewModelScope.launch {
            repository.update(idea.copy(aiResponse = aiResponse, updatedDate = System.currentTimeMillis()))
        }
    }

    fun updateIdea(idea: Idea) {
        viewModelScope.launch {
            repository.update(idea.copy(updatedDate = System.currentTimeMillis()))
        }
    }

    fun deleteIdea(idea: Idea) {
        viewModelScope.launch {
            repository.delete(idea)
        }
    }

    fun clearAiResponse() {
        _aiResponse.update { null }
    }
}
