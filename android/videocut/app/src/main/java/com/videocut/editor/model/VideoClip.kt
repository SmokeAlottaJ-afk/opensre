package com.videocut.editor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.videocut.editor.util.Converters
import java.util.Date

@Entity(
    tableName = "video_clips",
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
data class VideoClip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long = 0,
    val filePath: String = "",
    val startTime: Long = 0,
    val endTime: Long = 0,
    val duration: Long = 0,
    val positionInTimeline: Long = 0,
    val orderIndex: Int = 0,
    val volume: Float = 1.0f,
    val speed: Float = 1.0f,
    val filters: List<Filter> = emptyList(),
    val transitions: List<Transition> = emptyList(),
    val createdAt: Date = Date()
)

data class Filter(
    val type: FilterType,
    val intensity: Float = 0.5f,
    val parameters: Map<String, Float> = emptyMap()
)

enum class FilterType {
    NONE,
    BRIGHTNESS,
    CONTRAST,
    SATURATION,
    HUE,
    GRAYSCALE,
    SEPIA,
    INVERT,
    BLUR,
    SHARPEN
}

data class Transition(
    val type: TransitionType,
    val duration: Long = 500
)

enum class TransitionType {
    NONE,
    FADE,
    SLIDE_LEFT,
    SLIDE_RIGHT,
    SLIDE_UP,
    SLIDE_DOWN,
    ZOOM_IN,
    ZOOM_OUT,
    ROTATE
}
