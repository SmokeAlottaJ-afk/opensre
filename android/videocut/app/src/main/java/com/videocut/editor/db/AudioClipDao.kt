package com.videocut.editor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.videocut.editor.model.AudioClip
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioClipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: AudioClip): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clips: List<AudioClip>)

    @Query("SELECT * FROM audio_clips WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getByProject(projectId: Long): Flow<List<AudioClip>>

    @Query("SELECT * FROM audio_clips WHERE id = :id")
    fun getById(id: Long): Flow<AudioClip?>

    @Query("DELETE FROM audio_clips WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM audio_clips WHERE projectId = :projectId")
    suspend fun deleteByProject(projectId: Long)

    @Query("DELETE FROM audio_clips")
    suspend fun deleteAll()
}
