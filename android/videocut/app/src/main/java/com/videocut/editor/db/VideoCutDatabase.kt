package com.videocut.editor.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.videocut.editor.model.AudioClip
import com.videocut.editor.model.Project
import com.videocut.editor.model.TextOverlay
import com.videocut.editor.model.VideoClip
import com.videocut.editor.util.Converters

@Database(
    entities = [Project::class, VideoClip::class, AudioClip::class, TextOverlay::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class VideoCutDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun videoClipDao(): VideoClipDao
    abstract fun audioClipDao(): AudioClipDao
    abstract fun textOverlayDao(): TextOverlayDao
}
