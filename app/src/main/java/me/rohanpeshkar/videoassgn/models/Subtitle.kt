package me.rohanpeshkar.videoassgn.models

import java.util.concurrent.TimeUnit

data class Subtitle(
    val startTime: String? = null,
    val endTime: String? = null,
    val text: String? = null
) {

    val startTimeMillis: Long? by lazy {
        startTime?.let { convertToAbsoluteMillis(it) }
    }

    val endTimeMillis: Long? by lazy {
        endTime?.let { convertToAbsoluteMillis(it) }
    }


    /**
     * Convert timestamp into actual milliseconds
     */
    private fun convertToAbsoluteMillis(time: String): Long {
        val milliseconds = time.substringAfter(',').toLong()
        val hours: Long
        val minutes: Long
        val seconds: Long
        with(time.split(':')) {
            hours = TimeUnit.HOURS.toMillis(this[0].toLong())
            minutes = TimeUnit.MINUTES.toMillis(this[1].toLong())
            seconds = TimeUnit.SECONDS.toMillis(this[2].substringBefore(',').toLong())
        }
        return hours + minutes + seconds + milliseconds
    }
}