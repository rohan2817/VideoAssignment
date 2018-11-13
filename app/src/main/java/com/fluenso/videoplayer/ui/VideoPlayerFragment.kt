package com.fluenso.videoplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fluenso.videoplayer.R
import com.fluenso.videoplayer.utils.*
import kotlinx.android.synthetic.main.fragment_video_player.*

import java.util.*


class VideoPlayerFragment : Fragment() {

    //Timer to handle timer
    private lateinit var timer: Timer

    //Runnable task executed after time difference
    private lateinit var timerTask: TimerTask

    //Subtitle adapter
    private lateinit var subtitleAdapter: SubtitleAdapter

    //Value to be retained from state changed
    private var stopPosition: Int = 0

    companion object {
        fun create(): VideoPlayerFragment {
            return VideoPlayerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            stopPosition = savedInstanceState.getInt(TIMER_POSITION)
        }
        initializeVideoView()
        initializeSubtitles()
    }

    override fun onResume() {
        super.onResume()
        video_view.seekTo(stopPosition)
        video_view.start()
        initializeAndStartTimer(stopPosition.toLong())
    }

    override fun onPause() {
        super.onPause()
        video_view.pause()
        timerTask.cancel()
        timer.cancel()
    }

    override fun onStop() {
        super.onStop()
        video_view.stopPlayback()
        timer.cancel()
        timerTask.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stopPosition = video_view.currentPosition
        outState.putInt(TIMER_POSITION, stopPosition)
    }

    private fun initializeVideoView() {
        video_view.setVideoURI(getVideoUri(context?.packageName, R.raw.never_give_up))
        val mediaController = MediaController(context)
        mediaController.setAnchorView(rel_video_view)
        video_view.setMediaController(mediaController)
        video_view.setOnPreparedListener {
            video_view.start()
            initializeAndStartTimer(0)
            mediaController.show(0)
        }

        video_view.setOnCompletionListener {
            video_view.seekTo(1)
            timer.cancel()
            timerTask.cancel()
            subtitleAdapter.getScrollPosition(0)?.let { position ->
                subtitleAdapter.goToSubtitle(position)
            }
            rev_sub_titles.smoothScrollToPosition(0)
        }
    }

    private fun initializeAndStartTimer(startMillis: Long) {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                updateSubtitle()
            }
        }
        timer.schedule(timerTask, startMillis, DELAY_SUBTITLE_UPDATE)
    }

    private fun initializeSubtitles() {
        context?.let {
            rev_sub_titles.layoutManager = LinearLayoutManager(it)
            subtitleAdapter = SubtitleAdapter(
                getSubtitlesFromFile(
                    it,
                    SRT_FILE_NAME
                )
            )
            { startTimeMillis, _ -> video_view.seekTo(startTimeMillis.toInt()) }
            rev_sub_titles.adapter = subtitleAdapter
        }
    }

    private fun updateSubtitle() {
        activity?.runOnUiThread {
            subtitleAdapter.getScrollPosition(video_view.currentPosition.toSeconds())?.let {
                subtitleAdapter.goToSubtitle(it)
                val layoutManager = rev_sub_titles.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(it, SUBTITLE_SCROLL_OFFSET.px)
            }
        }
    }
}