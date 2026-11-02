package com.videocut.editor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.videocut.editor.util.Converters
import java.util.Date

@Entity(
    tableName = "audio_clips",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["projectId"])]
)
@TypeConverters(Converters::class)
data class AudioClip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long = 0,
    val filePath: String = "",
    val startTime: Long = 0,
    val endTime: Long = 0,
    val duration: Long = 0,
    val positionInTimeline: Long = 0,
    val orderIndex: Int = 0,
    val volume: Float = 1.0f,
    val fadeIn: Long = 0,
    val fadeOut: Long = 0,
    val createdAt: Date = Date()
)
