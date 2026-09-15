package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val sender: String, // "USER" or "MYRA"
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mode: String = "CHAT", // "VOICE", "CHAT", "SYSTEM"
    val topic: String = "General", // "Personal", "Code/PC", "General", "Files", "Task", "Reminder"
    val keywords: String = "",
    val isRecalled: Boolean = false,
    val replyToMessageId: String? = null
) {
    val isUser: Boolean get() = sender == "USER"

    val formattedTime: String
        get() = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))

    val formattedDate: String
        get() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))

    val relativeTime: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            return when {
                diff < 60_000L -> "Just now"
                diff < 3600_000L -> "${diff / 60_000L}m ago"
                diff < 86400_000L -> "${diff / 3600_000L}h ago"
                diff < 172800_000L -> "Yesterday"
                else -> formattedDate
            }
        }
}

@Entity(tableName = "conversation_sessions")
data class ConversationSessionEntity(
    @PrimaryKey val sessionId: String,
    val title: String,
    val startTime: Long = System.currentTimeMillis(),
    val lastActiveTime: Long = System.currentTimeMillis(),
    val messageCount: Int = 0,
    val summary: String? = null
)
