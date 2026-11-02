package com.aiforge.chat.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aiforge.chat.util.Converters
import java.util.Date

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["conversationId"])]
)
@TypeConverters(Converters::class)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long = 0,
    val role: MessageRole = MessageRole.USER,
    val content: String = "",
    val timestamp: Date = Date(),
    val isStreaming: Boolean = false,
    val error: String? = null
)

enum class MessageRole {
    SYSTEM,
    USER,
    ASSISTANT
}
