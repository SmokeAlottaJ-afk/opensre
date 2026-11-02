package com.aiforge.chat.repository

import com.aiforge.chat.db.ConversationDao
import com.aiforge.chat.db.MessageDao
import com.aiforge.chat.model.Conversation
import com.aiforge.chat.model.Message
import com.aiforge.chat.model.MessageRole
import com.aiforge.chat.network.ChatApi
import com.aiforge.chat.network.ChatCompletionRequest
import com.aiforge.chat.network.toChatMessage
import com.aiforge.chat.network.toMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatApi: ChatApi,
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val settingsRepository: SettingsRepository
) {

    fun getConversations(): Flow<List<Conversation>> {
        return conversationDao.getAll()
    }

    fun getConversationWithMessages(conversationId: Long): Flow<Pair<Conversation?, List<Message>>> {
        return flow {
            val conversation = conversationDao.getById(conversationId)
            val messages = messageDao.getByConversation(conversationId)
            emit(conversation to messages)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createConversation(model: String = settingsRepository.getModel()): Conversation {
        val conversation = Conversation(
            title = "New Chat",
            model = model,
            createdAt = Date(),
            updatedAt = Date()
        )
        val id = conversationDao.insert(conversation)
        return conversation.copy(id = id)
    }

    suspend fun sendMessage(
        conversationId: Long,
        content: String
    ): Flow<Message> = flow {
        val conversation = conversationDao.getById(conversationId).first() ?: throw IllegalStateException("Conversation not found")

        val userMessage = Message(
            conversationId = conversationId,
            role = MessageRole.USER,
            content = content,
            timestamp = Date()
        )
        val userMessageId = messageDao.insert(userMessage)
        emit(userMessage.copy(id = userMessageId))

        val messages = messageDao.getByConversation(conversationId).first()
            .map { it.toChatMessage() }
            .toMutableList()

        messages.add(0, com.aiforge.chat.network.ChatMessage("system", "You are a helpful assistant."))

        val request = ChatCompletionRequest(
            model = conversation.model,
            messages = messages,
            temperature = 0.7,
            stream = true
        )

        val response = chatApi.chatCompletionsStream(request)
        if (!response.isSuccessful) {
            throw Exception("API Error: ${response.code()} - ${response.message()}")
        }

        val assistantMessage = Message(
            conversationId = conversationId,
            role = MessageRole.ASSISTANT,
            content = "",
            timestamp = Date(),
            isStreaming = true
        )
        val assistantMessageId = messageDao.insert(assistantMessage)
        emit(assistantMessage.copy(id = assistantMessageId))

        val body = response.body() ?: throw Exception("Empty response body")
        val contentBuilder = StringBuilder()

        try {
            body.source().use { source ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (source.read(buffer).also { bytesRead = it } != -1) {
                    val chunk = String(buffer, 0, bytesRead, Charsets.UTF_8)
                    contentBuilder.append(chunk)
                    val assistantMessageUpdate = assistantMessage.copy(
                        id = assistantMessageId,
                        content = contentBuilder.toString(),
                        isStreaming = true
                    )
                    messageDao.insert(assistantMessageUpdate)
                    emit(assistantMessageUpdate)
                }
            }
        } catch (e: Exception) {
            val assistantMessageUpdate = assistantMessage.copy(
                id = assistantMessageId,
                content = contentBuilder.toString(),
                isStreaming = false,
                error = e.message
            )
            messageDao.insert(assistantMessageUpdate)
            emit(assistantMessageUpdate)
        }

        val finalAssistantMessage = assistantMessage.copy(
            id = assistantMessageId,
            content = contentBuilder.toString(),
            isStreaming = false
        )
        messageDao.insert(finalAssistantMessage)
        emit(finalAssistantMessage)

        conversationDao.updateUpdatedAt(conversationId)
    }

    suspend fun deleteConversation(conversationId: Long) {
        messageDao.deleteByConversation(conversationId)
        conversationDao.delete(conversationId)
    }

    suspend fun deleteAllConversations() {
        messageDao.deleteAll()
        conversationDao.deleteAll()
    }

    suspend fun updateConversationTitle(conversationId: Long, title: String) {
        conversationDao.updateTitle(conversationId, title)
    }
}
