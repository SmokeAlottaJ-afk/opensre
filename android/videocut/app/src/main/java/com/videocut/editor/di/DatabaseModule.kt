package com.videocut.editor.di

import android.content.Context
import androidx.room.Room
import com.videocut.editor.db.AudioClipDao
import com.videocut.editor.db.ProjectDao
import com.videocut.editor.db.TextOverlayDao
import com.videocut.editor.db.VideoClipDao
import com.videocut.editor.db.VideoCutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VideoCutDatabase {
        return Room.databaseBuilder(
            context,
            VideoCutDatabase::class.java,
            "videocut_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: VideoCutDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideVideoClipDao(database: VideoCutDatabase): VideoClipDao {
        return database.videoClipDao()
    }

    @Provides
    @Singleton
    fun provideAudioClipDao(database: VideoCutDatabase): AudioClipDao {
        return database.audioClipDao()
    }

    @Provides
    @Singleton
    fun provideTextOverlayDao(database: VideoCutDatabase): TextOverlayDao {
        return database.textOverlayDao()
    }
}
