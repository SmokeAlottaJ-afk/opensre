package com.aiforge.chat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aiforge.chat.model.Conversation
import com.aiforge.chat.model.ConversationWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversation: Conversation): Long

    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<Conversation>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    fun getById(id: Long): Flow<Conversation?>

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM conversations")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM conversations WHERE id = :id")
    fun getWithMessages(id: Long): Flow<ConversationWithMessages?>

    @Query("UPDATE conversations SET title = :title, updatedAt = datetime('now') WHERE id = :id")
    suspend fun updateTitle(id: Long, title: String)

    @Query("UPDATE conversations SET updatedAt = datetime('now') WHERE id = :id")
    suspend fun updateUpdatedAt(id: Long)
}
