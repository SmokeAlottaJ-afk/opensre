package com.videocut.editor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.videocut.editor.model.Project
import com.videocut.editor.model.ProjectWithClips
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project): Long

    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Long): Flow<Project?>

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM projects")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM projects WHERE id = :id")
    fun getWithClips(id: Long): Flow<ProjectWithClips?>

    @Query("UPDATE projects SET name = :name, description = :description, updatedAt = datetime('now') WHERE id = :id")
    suspend fun update(id: Long, name: String, description: String)

    @Query("UPDATE projects SET updatedAt = datetime('now'), lastOpened = datetime('now') WHERE id = :id")
    suspend fun updateLastOpened(id: Long)
}
