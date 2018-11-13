package me.rohanpeshkar.videoassgn.models

import java.util.concurrent.TimeUnit

data class Subtitle(
    val startTime: String,
    val endTime: String,
    val text: String
) {

    val startTimeMillis: Long by lazy {
        convertToAbsoluteMillis(startTime)
    }

    val endTimeMillis: Long by lazy {
        convertToAbsoluteMillis(endTime)
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