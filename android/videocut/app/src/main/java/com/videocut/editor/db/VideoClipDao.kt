package com.videocut.editor.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.videocut.editor.model.VideoClip
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoClipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clip: VideoClip): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clips: List<VideoClip>)

    @Query("SELECT * FROM video_clips WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getByProject(projectId: Long): Flow<List<VideoClip>>

    @Query("SELECT * FROM video_clips WHERE id = :id")
    fun getById(id: Long): Flow<VideoClip?>

    @Query("DELETE FROM video_clips WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM video_clips WHERE projectId = :projectId")
    suspend fun deleteByProject(projectId: Long)

    @Query("DELETE FROM video_clips")
    suspend fun deleteAll()

    @Query("UPDATE video_clips SET orderIndex = :newIndex WHERE id = :id")
    suspend fun updateOrder(id: Long, newIndex: Int)

    @Query("UPDATE video_clips SET startTime = :startTime, endTime = :endTime, duration = :duration WHERE id = :id")
    suspend fun updateTrim(id: Long, startTime: Long, endTime: Long, duration: Long)
}
