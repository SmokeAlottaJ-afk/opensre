package com.videocut.editor.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.videocut.editor.model.AudioClip
import com.videocut.editor.model.Project
import com.videocut.editor.model.TextOverlay
import com.videocut.editor.model.VideoClip
import com.videocut.editor.repository.EditorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: EditorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: Long by lazy {
        savedStateHandle.get<Long>("projectId") ?: 0L
    }

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _videoClips = MutableStateFlow<List<VideoClip>>(emptyList())
    val videoClips: StateFlow<List<VideoClip>> = _videoClips.asStateFlow()

    private val _audioClips = MutableStateFlow<List<AudioClip>>(emptyList())
    val audioClips: StateFlow<List<AudioClip>> = _audioClips.asStateFlow()

    private val _textOverlays = MutableStateFlow<List<TextOverlay>>(emptyList())
    val textOverlays: StateFlow<List<TextOverlay>> = _textOverlays.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow(0L)
    val totalDuration: StateFlow<Long> = _totalDuration.asStateFlow()

    init {
        loadProject()
    }

    private fun loadProject() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                combine(
                    repository.getProjectWithClips(projectId),
                    repository.getVideoClipsByProject(projectId),
                    repository.getAudioClipsByProject(projectId),
                    repository.getTextOverlaysByProject(projectId)
                ) { (project, _), clips, audioClips, overlays ->
                    _project.value = project.first
                    _videoClips.value = clips
                    _audioClips.value = audioClips
                    _textOverlays.value = overlays
                    _totalDuration.value = clips.sumOf { it.duration }
                }
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        _error.value = e.message ?: "Unknown error"
                        _isLoading.value = false
                    }
                    .collect {
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }

    fun addVideoClip(filePath: String, duration: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val clip = repository.addVideoClip(projectId, filePath, duration)
                _videoClips.value = _videoClips.value + clip
                _totalDuration.value = _videoClips.value.sumOf { it.duration }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteVideoClip(clipId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteVideoClip(clipId)
                _videoClips.value = _videoClips.value.filter { it.id != clipId }
                _totalDuration.value = _videoClips.value.sumOf { it.duration }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun reorderVideoClips(clips: List<VideoClip>) {
        viewModelScope.launch {
            repository.reorderVideoClips(clips)
            _videoClips.value = clips
        }
    }

    fun trimVideoClip(clipId: Long, startTime: Long, endTime: Long, duration: Long) {
        viewModelScope.launch {
            repository.trimVideoClip(clipId, startTime, endTime, duration)
            _videoClips.value = _videoClips.value.map {
                if (it.id == clipId) {
                    it.copy(startTime = startTime, endTime = endTime, duration = duration)
                } else it
            }
            _totalDuration.value = _videoClips.value.sumOf { it.duration }
        }
    }

    fun addAudioClip(filePath: String, duration: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val clip = repository.addAudioClip(projectId, filePath, duration)
                _audioClips.value = _audioClips.value + clip
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAudioClip(clipId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteAudioClip(clipId)
                _audioClips.value = _audioClips.value.filter { it.id != clipId }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTextOverlay(text: String, startTime: Long = 0, endTime: Long = 10000) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val overlay = repository.addTextOverlay(projectId, text, startTime, endTime)
                _textOverlays.value = _textOverlays.value + overlay
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTextOverlay(overlayId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteTextOverlay(overlayId)
                _textOverlays.value = _textOverlays.value.filter { it.id != overlayId }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProject(name: String, description: String) {
        viewModelScope.launch {
            repository.updateProject(projectId, name, description)
            _project.value = _project.value?.copy(name = name, description = description)
        }
    }

    fun play() {
        _isPlaying.value = true
    }

    fun pause() {
        _isPlaying.value = false
    }

    fun seekTo(position: Long) {
        _currentPosition.value = position
    }

    fun updatePosition(position: Long) {
        _currentPosition.value = position
    }

    fun clearError() {
        _error.value = null
    }
}
