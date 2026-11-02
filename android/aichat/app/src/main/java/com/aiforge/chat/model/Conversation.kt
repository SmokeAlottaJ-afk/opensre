package com.aiforge.chat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aiforge.chat.util.Converters
import java.util.Date

@Entity(tableName = "conversations")
@TypeConverters(Converters::class)
data class Conversation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "New Chat",
    val model: String = "gpt-3.5-turbo",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

@TypeConverters(Converters::class)
data class ConversationWithMessages(
    val conversation: Conversation,
    val messages: List<Message> = emptyList()
)
