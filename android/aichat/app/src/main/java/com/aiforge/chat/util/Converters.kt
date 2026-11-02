package com.aiforge.chat.util

import androidx.room.TypeConverter
import com.aiforge.chat.model.MessageRole
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMessageRole(value: String?): MessageRole? {
        return value?.let { MessageRole.valueOf(it) }
    }

    @TypeConverter
    fun messageRoleToString(role: MessageRole?): String? {
        return role?.name
    }
}
