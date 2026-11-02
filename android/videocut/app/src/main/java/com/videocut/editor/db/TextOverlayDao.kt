package com.videocut.editor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.videocut.editor.model.TextOverlay
import kotlinx.coroutines.flow.Flow

@Dao
interface TextOverlayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(overlay: TextOverlay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(overlays: List<TextOverlay>)

    @Query("SELECT * FROM text_overlays WHERE projectId = :projectId ORDER BY startTime ASC")
    fun getByProject(projectId: Long): Flow<List<TextOverlay>>

    @Query("SELECT * FROM text_overlays WHERE id = :id")
    fun getById(id: Long): Flow<TextOverlay?>

    @Query("DELETE FROM text_overlays WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM text_overlays WHERE projectId = :projectId")
    suspend fun deleteByProject(projectId: Long)

    @Query("DELETE FROM text_overlays")
    suspend fun deleteAll()
}
