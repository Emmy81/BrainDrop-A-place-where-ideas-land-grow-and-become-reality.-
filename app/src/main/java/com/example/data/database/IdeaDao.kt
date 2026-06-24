package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.models.Idea
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Query("SELECT * FROM ideas ORDER BY createdDate DESC")
    fun getAllIdeas(): Flow<List<Idea>>

    @Query("SELECT * FROM ideas WHERE category = :category ORDER BY createdDate DESC")
    fun getIdeasByCategory(category: String): Flow<List<Idea>>

    @Query("SELECT * FROM ideas WHERE id = :id")
    fun getIdeaById(id: Int): Flow<Idea?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: Idea)

    @Update
    suspend fun updateIdea(idea: Idea)

    @Delete
    suspend fun deleteIdea(idea: Idea)
}
