package com.videocut.editor.di

import com.videocut.editor.db.AudioClipDao
import com.videocut.editor.db.ProjectDao
import com.videocut.editor.db.TextOverlayDao
import com.videocut.editor.db.VideoClipDao
import com.videocut.editor.repository.EditorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEditorRepository(
        projectDao: ProjectDao,
        videoClipDao: VideoClipDao,
        audioClipDao: AudioClipDao,
        textOverlayDao: TextOverlayDao
    ): EditorRepository {
        return EditorRepository(projectDao, videoClipDao, audioClipDao, textOverlayDao)
    }
}
