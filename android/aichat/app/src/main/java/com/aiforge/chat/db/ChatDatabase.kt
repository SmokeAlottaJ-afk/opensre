package com.aiforge.chat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aiforge.chat.model.Conversation
import com.aiforge.chat.model.Message
import com.aiforge.chat.util.Converters

@Database(
    entities = [Conversation::class, Message::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}
