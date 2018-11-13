package com.fluenso.videoplayer.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.concurrent.TimeUnit


//region :Extensions
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun TextView.boldText() {
    setTypeface(typeface, Typeface.BOLD)
}

fun TextView.normalText() {
    typeface = Typeface.DEFAULT
}

fun TextView.setColor(context: Context, colorRes: Int) {
    setTextColor(ContextCompat.getColor(context, colorRes))
}

fun Int.toSeconds(): Int = TimeUnit.MILLISECONDS.toSeconds(this.toLong()).toInt()
//endregion

//region :Constants
const val REGEX_TIMESTAMP = "\\d+:\\d{2}:\\d{2},\\d+"
const val REGEX_SPACE = "\\s"
const val TIMER_POSITION = "position"
const val DELAY_SUBTITLE_UPDATE: Long = 500
const val SRT_FILE_NAME = "subtitles.srt"
const val SUBTITLE_SCROLL_OFFSET = 100
//endregion

//region :Utility Methods
fun getVideoUri(packageName: String?, rawId: Int): Uri {
    return Uri.parse("android.resource://$packageName/raw/$rawId")
}
//endregion