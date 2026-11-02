package com.aiforge.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aiforge.chat.model.Message
import com.aiforge.chat.model.MessageRole
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val isUser = message.role == MessageRole.USER

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Avatar(
                role = message.role,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f, fill = !isUser)
                .padding(vertical = 4.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            val bubbleColor = when (message.role) {
                MessageRole.USER -> MaterialTheme.colorScheme.primary
                MessageRole.ASSISTANT -> MaterialTheme.colorScheme.surfaceContainer
                MessageRole.SYSTEM -> MaterialTheme.colorScheme.surfaceContainer
            }

            val textColor = when (message.role) {
                MessageRole.USER -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurface
            }

            Box(
                modifier = Modifier
                    .clip(
                        when (isUser) {
                            true -> RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
                            false -> RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
                        }
                    )
                    .background(bubbleColor)
                    .clickable(
                        enabled = message.content.isNotEmpty(),
                        onClick = {
                            clipboardManager.setText(AnnotatedString(message.content))
                        }
                    )
                    .padding(12.dp)
            ) {
                Column {
                    if (message.error != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = message.error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Text(
                        text = message.content,
                        color = textColor,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 20.dp
                    )

                    if (message.isStreaming) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Typing...",
                                color = textColor.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatTime(message.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Avatar(
                role = message.role,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun Avatar(
    role: MessageRole,
    modifier: Modifier = Modifier
) {
    val icon = when (role) {
        MessageRole.USER -> Icons.Default.Person
        MessageRole.ASSISTANT -> Icons.Default.SmartToy
        MessageRole.SYSTEM -> Icons.Default.MoreVert
    }

    val backgroundColor = when (role) {
        MessageRole.USER -> MaterialTheme.colorScheme.primaryContainer
        MessageRole.ASSISTANT -> MaterialTheme.colorScheme.secondaryContainer
        MessageRole.SYSTEM -> MaterialTheme.colorScheme.surfaceContainer
    }

    val iconColor = when (role) {
        MessageRole.USER -> MaterialTheme.colorScheme.onPrimaryContainer
        MessageRole.ASSISTANT -> MaterialTheme.colorScheme.onSecondaryContainer
        MessageRole.SYSTEM -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = when (role) {
                MessageRole.USER -> "User"
                MessageRole.ASSISTANT -> "Assistant"
                MessageRole.SYSTEM -> "System"
            },
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun formatTime(date: Date): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(date)
}

@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    val userMessage = Message(
        id = 1,
        conversationId = 1,
        role = MessageRole.USER,
        content = "Hello, how are you?",
        timestamp = Date()
    )

    val assistantMessage = Message(
        id = 2,
        conversationId = 1,
        role = MessageRole.ASSISTANT,
        content = "I'm doing well, thank you for asking! How can I help you today?",
        timestamp = Date()
    )

    // Preview would need proper setup with MaterialTheme
}
