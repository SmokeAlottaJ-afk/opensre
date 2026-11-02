package com.aiforge.chat.di

import com.aiforge.chat.db.ConversationDao
import com.aiforge.chat.db.MessageDao
import com.aiforge.chat.network.ChatApi
import com.aiforge.chat.repository.ChatRepository
import com.aiforge.chat.repository.SettingsRepository
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
    fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepository()
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatApi: ChatApi,
        conversationDao: ConversationDao,
        messageDao: MessageDao,
        settingsRepository: SettingsRepository
    ): ChatRepository {
        return ChatRepository(chatApi, conversationDao, messageDao, settingsRepository)
    }
}
