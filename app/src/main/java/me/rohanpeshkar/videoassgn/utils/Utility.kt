package me.rohanpeshkar.videoassgn.utils

import android.content.res.Resources
import android.net.Uri


//region :Extensions
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
//endregion

//region :Constants
const val REGEX_TIMESTAMP = "\\d+:\\d{2}:\\d{2},\\d+"
const val REGEX_SPACE = "\\s"
const val POSITION_FRAGMENT = "position"
const val DELAY_SUBTITLE_UPDATE: Long = 500
const val SRT_FILE_NAME = "subtitles.srt"
//endregion

//region :Utility Methods
public fun getVideoUri(packageName: String?, rawId: Int): Uri {
    return Uri.parse("android.resource://$packageName/raw/$rawId")
}
//endregion