package com.videocut.editor.util

import androidx.room.TypeConverter
import com.videocut.editor.model.AAspectRatio
import com.videocut.editor.model.FrameRate
import com.videocut.editor.model.Filter
import com.videocut.editor.model.FilterType
import com.videocut.editor.model.Resolution
import com.videocut.editor.model.TextAlignment
import com.videocut.editor.model.TextAnimation
import com.videocut.editor.model.Transition
import com.videocut.editor.model.TransitionType
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
    fun fromResolution(value: String?): Resolution? {
        return value?.let { Resolution.valueOf(it) }
    }

    @TypeConverter
    fun resolutionToString(resolution: Resolution?): String? {
        return resolution?.name
    }

    @TypeConverter
    fun fromFrameRate(value: String?): FrameRate? {
        return value?.let { FrameRate.valueOf(it) }
    }

    @TypeConverter
    fun frameRateToString(frameRate: FrameRate?): String? {
        return frameRate?.name
    }

    @TypeConverter
    fun fromAspectRatio(value: String?): com.videocut.editor.model.AspectRatio? {
        return value?.let { com.videocut.editor.model.AspectRatio.valueOf(it) }
    }

    @TypeConverter
    fun aspectRatioToString(aspectRatio: com.videocut.editor.model.AspectRatio?): String? {
        return aspectRatio?.name
    }

    @TypeConverter
    fun fromTextAlignment(value: String?): TextAlignment? {
        return value?.let { TextAlignment.valueOf(it) }
    }

    @TypeConverter
    fun textAlignmentToString(alignment: TextAlignment?): String? {
        return alignment?.name
    }

    @TypeConverter
    fun fromTextAnimation(value: String?): TextAnimation? {
        return value?.let { TextAnimation.valueOf(it) }
    }

    @TypeConverter
    fun textAnimationToString(animation: TextAnimation?): String? {
        return animation?.name
    }

    @TypeConverter
    fun fromFilterType(value: String?): FilterType? {
        return value?.let { FilterType.valueOf(it) }
    }

    @TypeConverter
    fun filterTypeToString(type: FilterType?): String? {
        return type?.name
    }

    @TypeConverter
    fun fromTransitionType(value: String?): TransitionType? {
        return value?.let { TransitionType.valueOf(it) }
    }

    @TypeConverter
    fun transitionTypeToString(type: TransitionType?): String? {
        return type?.name
    }

    @TypeConverter
    fun fromFilterList(value: String?): List<Filter>? {
        return value?.let { list ->
            list.split(",").mapNotNull { item ->
                val parts = item.split("|")
                if (parts.size >= 2) {
                    Filter(
                        type = FilterType.valueOf(parts[0]),
                        intensity = parts[1].toFloatOrNull() ?: 0.5f
                    )
                } else null
            }
        }
    }

    @TypeConverter
    fun filterListToString(filters: List<Filter>?): String? {
        return filters?.joinToString(",") { filter ->
            "${filter.type.name}|${filter.intensity}"
        }
    }

    @TypeConverter
    fun fromTransitionList(value: String?): List<Transition>? {
        return value?.let { list ->
            list.split(",").mapNotNull { item ->
                val parts = item.split("|")
                if (parts.size >= 2) {
                    Transition(
                        type = TransitionType.valueOf(parts[0]),
                        duration = parts[1].toLongOrNull() ?: 500L
                    )
                } else null
            }
        }
    }

    @TypeConverter
    fun transitionListToString(transitions: List<Transition>?): String? {
        return transitions?.joinToString(",") { transition ->
            "${transition.type.name}|${transition.duration}"
        }
    }
}
