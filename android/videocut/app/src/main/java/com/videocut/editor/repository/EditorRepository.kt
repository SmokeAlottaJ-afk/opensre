package com.videocut.editor.repository

import com.videocut.editor.db.AudioClipDao
import com.videocut.editor.db.ProjectDao
import com.videocut.editor.db.TextOverlayDao
import com.videocut.editor.db.VideoClipDao
import com.videocut.editor.model.AudioClip
import com.videocut.editor.model.Project
import com.videocut.editor.model.TextOverlay
import com.videocut.editor.model.VideoClip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class EditorRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val videoClipDao: VideoClipDao,
    private val audioClipDao: AudioClipDao,
    private val textOverlayDao: TextOverlayDao
) {

    fun getProjects(): Flow<List<Project>> {
        return projectDao.getAll()
    }

    fun getProjectWithClips(projectId: Long): Flow<Pair<Project?, List<VideoClip>>> {
        return flow {
            val project = projectDao.getById(projectId).first()
            val clips = videoClipDao.getByProject(projectId).first()
            emit(project to clips)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createProject(name: String = "Untitled Project"): Project {
        val project = Project(
            name = name,
            createdAt = Date(),
            updatedAt = Date()
        )
        val id = projectDao.insert(project)
        return project.copy(id = id)
    }

    suspend fun deleteProject(projectId: Long) {
        textOverlayDao.deleteByProject(projectId)
        audioClipDao.deleteByProject(projectId)
        videoClipDao.deleteByProject(projectId)
        projectDao.delete(projectId)
    }

    suspend fun addVideoClip(
        projectId: Long,
        filePath: String,
        duration: Long
    ): VideoClip {
        val clips = videoClipDao.getByProject(projectId).first()
        val orderIndex = clips.size

        val clip = VideoClip(
            projectId = projectId,
            filePath = filePath,
            duration = duration,
            endTime = duration,
            orderIndex = orderIndex,
            positionInTimeline = (clips.lastOrNull()?.positionInTimeline ?: 0) + duration,
            createdAt = Date()
        )

        val id = videoClipDao.insert(clip)
        projectDao.updateLastOpened(projectId)
        return clip.copy(id = id)
    }

    suspend fun deleteVideoClip(clipId: Long) {
        videoClipDao.delete(clipId)
    }

    suspend fun reorderVideoClips(clips: List<VideoClip>) {
        clips.forEachIndexed { index, clip ->
            videoClipDao.updateOrder(clip.id, index)
        }
    }

    suspend fun trimVideoClip(
        clipId: Long,
        startTime: Long,
        endTime: Long,
        duration: Long
    ) {
        videoClipDao.updateTrim(clipId, startTime, endTime, duration)
    }

    suspend fun addAudioClip(
        projectId: Long,
        filePath: String,
        duration: Long
    ): AudioClip {
        val clips = audioClipDao.getByProject(projectId).first()
        val orderIndex = clips.size

        val clip = AudioClip(
            projectId = projectId,
            filePath = filePath,
            duration = duration,
            endTime = duration,
            orderIndex = orderIndex,
            createdAt = Date()
        )

        val id = audioClipDao.insert(clip)
        projectDao.updateLastOpened(projectId)
        return clip.copy(id = id)
    }

    suspend fun deleteAudioClip(clipId: Long) {
        audioClipDao.delete(clipId)
    }

    suspend fun addTextOverlay(
        projectId: Long,
        text: String,
        startTime: Long = 0,
        endTime: Long = 10000
    ): TextOverlay {
        val overlay = TextOverlay(
            projectId = projectId,
            text = text,
            startTime = startTime,
            endTime = endTime,
            createdAt = Date()
        )

        val id = textOverlayDao.insert(overlay)
        projectDao.updateLastOpened(projectId)
        return overlay.copy(id = id)
    }

    suspend fun deleteTextOverlay(overlayId: Long) {
        textOverlayDao.delete(overlayId)
    }

    suspend fun updateProject(
        projectId: Long,
        name: String,
        description: String
    ) {
        projectDao.update(projectId, name, description)
    }

    fun getVideoClipsByProject(projectId: Long): Flow<List<VideoClip>> {
        return videoClipDao.getByProject(projectId)
    }

    fun getAudioClipsByProject(projectId: Long): Flow<List<AudioClip>> {
        return audioClipDao.getByProject(projectId)
    }

    fun getTextOverlaysByProject(projectId: Long): Flow<List<TextOverlay>> {
        return textOverlayDao.getByProject(projectId)
    }
}
