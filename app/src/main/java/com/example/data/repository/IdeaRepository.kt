package com.example.data.repository

import com.example.data.database.IdeaDao
import com.example.data.models.Idea
import kotlinx.coroutines.flow.Flow

class IdeaRepository(private val ideaDao: IdeaDao) {
    val allIdeas: Flow<List<Idea>> = ideaDao.getAllIdeas()

    fun getIdeasByCategory(category: String): Flow<List<Idea>> {
        return ideaDao.getIdeasByCategory(category)
    }

    fun getIdeaById(id: Int): Flow<Idea?> {
        return ideaDao.getIdeaById(id)
    }

    suspend fun insert(idea: Idea) = ideaDao.insertIdea(idea)

    suspend fun update(idea: Idea) = ideaDao.updateIdea(idea)

    suspend fun delete(idea: Idea) = ideaDao.deleteIdea(idea)
}
