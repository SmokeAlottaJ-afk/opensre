package com.videocut.editor.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.videocut.editor.util.Converters
import java.util.Date

@Entity(
    tableName = "text_overlays",
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
data class TextOverlay(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long = 0,
    val text: String = "",
    val fontFamily: String = "sans-serif",
    val fontSize: Float = 24f,
    val fontColor: Int = 0xFFFFFFFF.toInt(),
    val backgroundColor: Int = 0x00000000,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val alignment: TextAlignment = TextAlignment.CENTER,
    val positionX: Float = 0.5f,
    val positionY: Float = 0.5f,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val animation: TextAnimation = TextAnimation.NONE,
    val createdAt: Date = Date()
)

enum class TextAlignment {
    LEFT,
    CENTER,
    RIGHT
}

enum class TextAnimation {
    NONE,
    FADE_IN,
    FADE_OUT,
    SLIDE_IN_LEFT,
    SLIDE_IN_RIGHT,
    SLIDE_IN_UP,
    SLIDE_IN_DOWN,
    TYPEWRITER,
    ZOOM_IN
}
