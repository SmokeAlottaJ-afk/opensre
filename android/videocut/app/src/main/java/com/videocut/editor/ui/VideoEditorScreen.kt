package com.videocut.editor.ui

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Cut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Export
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.videocut.editor.model.VideoClip
import com.videocut.editor.viewmodel.EditorViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditorScreen(
    projectId: Long,
    onBackClick: () -> Unit
) {
    val viewModel: EditorViewModel = hiltViewModel()
    val project by viewModel.project.collectAsState()
    val videoClips by viewModel.videoClips.collectAsState()
    val audioClips by viewModel.audioClips.collectAsState()
    val textOverlays by viewModel.textOverlays.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val totalDuration by viewModel.totalDuration.collectAsState()

    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(EditorTab.VIDEO) }
    var showMediaPicker by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            // Add to project
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.name ?: "Untitled Project") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Save project
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                    IconButton(
                        onClick = {
                            // Export project
                        }
                    ) {
                        Icon(Icons.Default.Export, contentDescription = "Export")
                    }
                    IconButton(
                        onClick = { showMediaPicker = true }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Preview Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                // Video Preview would go here
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (videoClips.isEmpty()) {
                        Text(
                            text = "No media added",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    } else {
                        // Display first clip as preview
                        Text(
                            text = "Video Preview",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                // Playback Controls
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.play() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isPlaying) {
                        IconButton(
                            onClick = { viewModel.pause() },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                Icons.Default.Pause,
                                contentDescription = "Pause",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { viewModel.play() },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                LinearProgressIndicator(
                    progress = { if (totalDuration > 0) currentPosition.toFloat() / totalDuration.toFloat() else 0f },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formatTime(totalDuration),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                EditorTab.values().forEach { tab ->
                    TabButton(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected tab
            when (selectedTab) {
                EditorTab.VIDEO -> VideoTabContent(
                    clips = videoClips,
                    onClipClick = { /* Handle clip selection */ },
                    onDeleteClip = { clipId -> viewModel.deleteVideoClip(clipId) }
                )
                EditorTab.AUDIO -> AudioTabContent(
                    clips = audioClips,
                    onDeleteClip = { clipId -> viewModel.deleteAudioClip(clipId) }
                )
                EditorTab.TEXT -> TextTabContent(
                    overlays = textOverlays,
                    onAddOverlay = { text -> viewModel.addTextOverlay(text) },
                    onDeleteOverlay = { overlayId -> viewModel.deleteTextOverlay(overlayId) }
                )
                EditorTab.EFFECTS -> EffectsTabContent()
            }
        }

        if (showMediaPicker) {
            // Media picker dialog would go here
        }
    }
}

@Composable
fun TabButton(
    tab: EditorTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (tab) {
        EditorTab.VIDEO -> Icons.Default.Videocam
        EditorTab.AUDIO -> Icons.Default.Audiotrack
        EditorTab.TEXT -> Icons.Default.TextFields
        EditorTab.EFFECTS -> Icons.Default.Settings
    }

    val label = when (tab) {
        EditorTab.VIDEO -> "Video"
        EditorTab.AUDIO -> "Audio"
        EditorTab.TEXT -> "Text"
        EditorTab.EFFECTS -> "Effects"
    }

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun VideoTabContent(
    clips: List<VideoClip>,
    onClipClick: (VideoClip) -> Unit,
    onDeleteClip: (Long) -> Unit
) {
    if (clips.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Videocam,
            title = "No video clips",
            description = "Add video clips to your project"
        )
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clips) { clip ->
                ClipThumbnail(
                    clip = clip,
                    onClick = { onClipClick(clip) },
                    onDelete = { onDeleteClip(clip.id) }
                )
            }
        }
    }
}

@Composable
fun AudioTabContent(
    clips: List<com.videocut.editor.model.AudioClip>,
    onDeleteClip: (Long) -> Unit
) {
    if (clips.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Audiotrack,
            title = "No audio clips",
            description = "Add audio clips to your project"
        )
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clips) { clip ->
                AudioClipItem(
                    clip = clip,
                    onDelete = { onDeleteClip(clip.id) }
                )
            }
        }
    }
}

@Composable
fun TextTabContent(
    overlays: List<com.videocut.editor.model.TextOverlay>,
    onAddOverlay: (String) -> Unit,
    onDeleteOverlay: (Long) -> Unit
) {
    var newText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (overlays.isEmpty()) {
            EmptyState(
                icon = Icons.Default.TextFields,
                title = "No text overlays",
                description = "Add text overlays to your project"
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(overlays) { overlay ->
                    TextOverlayItem(
                        overlay = overlay,
                        onDelete = { onDeleteOverlay(overlay.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter text...") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newText.isNotEmpty()) {
                        onAddOverlay(newText)
                        newText = ""
                    }
                }
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun EffectsTabContent() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Effects",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        // Effects options would go here
    }
}

@Composable
fun ClipThumbnail(
    clip: VideoClip,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Thumbnail would be displayed here
        Icon(
            Icons.Default.Videocam,
            contentDescription = "Video",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = formatTime(clip.duration),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        )
    }
}

@Composable
fun AudioClipItem(
    clip: com.videocut.editor.model.AudioClip,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.MusicNote,
            contentDescription = "Audio",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Audio Clip",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatTime(clip.duration),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun TextOverlayItem(
    overlay: com.videocut.editor.model.TextOverlay,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = overlay.text,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun EmptyState(
    icon: android.graphics.drawable.Icon,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

enum class EditorTab {
    VIDEO,
    AUDIO,
    TEXT,
    EFFECTS
}

@Preview(showBackground = true)
@Composable
fun VideoEditorScreenPreview() {
    // Preview setup
}
