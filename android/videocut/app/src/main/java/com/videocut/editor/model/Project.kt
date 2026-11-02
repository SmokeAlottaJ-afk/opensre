package com.videocut.editor.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.videocut.editor.util.Converters
import java.util.Date

@Entity(tableName = "projects")
@TypeConverters(Converters::class)
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "Untitled Project",
    val description: String = "",
    val resolution: Resolution = Resolution.RES_1080P,
    val frameRate: FrameRate = FrameRate.FPS_30,
    val aspectRatio: AspectRatio = AspectRatio.RATIO_16_9,
    val backgroundColor: Int = 0xFF000000.toInt(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val lastOpened: Date? = null
)

enum class Resolution {
    RES_480P,
    RES_720P,
    RES_1080P,
    RES_4K
}

enum class FrameRate {
    FPS_24,
    FPS_30,
    FPS_60
}

enum class AspectRatio {
    RATIO_1_1,
    RATIO_4_3,
    RATIO_16_9,
    RATIO_9_16,
    RATIO_3_4
}

data class ProjectWithClips(
    val project: Project,
    val videoClips: List<VideoClip> = emptyList(),
    val audioClips: List<AudioClip> = emptyList(),
    val textOverlays: List<TextOverlay> = emptyList()
)
