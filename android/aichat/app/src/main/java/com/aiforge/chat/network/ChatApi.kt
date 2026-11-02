package com.aiforge.chat.network

import com.aiforge.chat.model.Message
import com.aiforge.chat.model.MessageRole
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    @POST("v1/chat/completions")
    suspend fun chatCompletions(
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>

    @POST("v1/chat/completions")
    suspend fun chatCompletionsStream(
        @Body request: ChatCompletionRequest
    ): Response<ResponseBody>
}

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    val max_tokens: Int? = null,
    val stream: Boolean = false
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val id: String,
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage? = null
)

data class Choice(
    val index: Int,
    val message: AssistantMessage,
    val finish_reason: String? = null
)

data class AssistantMessage(
    val role: String,
    val content: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

fun Message.toChatMessage(): ChatMessage {
    return ChatMessage(
        role = when (role) {
            MessageRole.SYSTEM -> "system"
            MessageRole.USER -> "user"
            MessageRole.ASSISTANT -> "assistant"
        },
        content = content
    )
}

fun ChatMessage.toMessage(conversationId: Long): Message {
    val role = when (role) {
        "system" -> MessageRole.SYSTEM
        "user" -> MessageRole.USER
        else -> MessageRole.ASSISTANT
    }
    return Message(
        conversationId = conversationId,
        role = role,
        content = content
    )
}
