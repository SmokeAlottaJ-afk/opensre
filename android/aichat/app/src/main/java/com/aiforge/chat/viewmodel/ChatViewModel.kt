package com.aiforge.chat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiforge.chat.model.Message
import com.aiforge.chat.model.MessageRole
import com.aiforge.chat.repository.ChatRepository
import com.aiforge.chat.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _conversationId: Long? by lazy {
        savedStateHandle.get<Long?>("conversationId")
    }
    val conversationId: Long? get() = _conversationId

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _conversationTitle = MutableStateFlow("")
    val conversationTitle: StateFlow<String> = _conversationTitle.asStateFlow()

    private var currentConversationId: Long? = null

    init {
        loadConversation()
    }

    private fun loadConversation() {
        viewModelScope.launch {
            if (conversationId != null) {
                chatRepository.getConversationWithMessages(conversationId!!)
                    .flowOn(Dispatchers.IO)
                    .collectLatest { (conversation, messages) ->
                        _conversationTitle.value = conversation?.title ?: "New Chat"
                        currentConversationId = conversation?.id
                        _messages.value = messages
                    }
            } else {
                _conversationTitle.value = "New Chat"
                _messages.value = emptyList()
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) {
            _error.value = "Message cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val actualConversationId = currentConversationId ?: run {
                    val newConversation = chatRepository.createConversation()
                    currentConversationId = newConversation.id
                    newConversation.id
                }

                chatRepository.sendMessage(actualConversationId, content)
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        _error.value = e.message ?: "Unknown error"
                        _isLoading.value = false
                    }
                    .collect { message ->
                        val currentMessages = _messages.value.toMutableList()
                        val index = currentMessages.indexOfFirst { it.id == message.id }
                        if (index >= 0) {
                            currentMessages[index] = message
                        } else {
                            currentMessages.add(message)
                        }
                        _messages.value = currentMessages
                        _isLoading.value = message.isStreaming
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }

    fun updateTitle(title: String) {
        viewModelScope.launch {
            currentConversationId?.let { id ->
                chatRepository.updateConversationTitle(id, title)
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
