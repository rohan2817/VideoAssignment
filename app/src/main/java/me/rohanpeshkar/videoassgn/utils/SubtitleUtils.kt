package me.rohanpeshkar.videoassgn.utils

import android.content.Context
import me.rohanpeshkar.videoassgn.models.Subtitle
import java.io.BufferedReader
import java.io.InputStreamReader

object SubtitleUtils {

    /**
     * Method to read subtitles from the file
     * Accepts context & name of the file
     */
    private fun getSrtFileContent(context: Context?, filename: String): String {
        val inputStream = context?.assets?.open(filename)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val content = bufferedReader.readText()
        inputStream?.close()
        bufferedReader.close()
        return content
    }

    /**
     * Method to read subtitles from file and create list of Subtitle objects
     * This method uses RegEx to match & extract content from the subtitle file
     * The file is fetched from the assets folder
     */
    fun getSubtitlesFromFile(context: Context?, filename: String): ArrayList<Subtitle> {

        val subtitlesList = arrayListOf<Subtitle>()

        var subtitlesString = getSrtFileContent(context, filename)

        val timeStampPattern = "$REGEX_TIMESTAMP$REGEX_SPACE-->$REGEX_SPACE$REGEX_TIMESTAMP".toRegex()


        while (subtitlesString.contains(timeStampPattern)) {
            val matchedValue = timeStampPattern.find(subtitlesString)
            if (matchedValue != null) {
                val timeArray = matchedValue.value.split("-->")
                val startTime = timeArray[0].trim()
                val endTime = timeArray[1].trim()
                val subtitleText: String
                if (matchedValue.next() != null) {
                    subtitleText = subtitlesString.substring(
                        matchedValue.range.last + 1,
                        (matchedValue.next()?.range?.first ?: 1) - 1
                    ).replace("\\s\\d+".toRegex(), "").trim()
                    subtitlesString = subtitlesString.substring(matchedValue.next()?.range?.first ?: 0)
                } else {
                    subtitleText =
                            subtitlesString.substring(matchedValue.range.last).replace("\\n\\d+\\n".toRegex(), "")
                    subtitlesString = subtitlesString.substring(matchedValue.range.last)
                }
                subtitlesList.add(Subtitle(startTime, endTime, subtitleText))
            }
        }

        return subtitlesList
    }
}