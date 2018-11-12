package me.rohanpeshkar.videoassgn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_video_player.*
import me.rohanpeshkar.videoassgn.R
import me.rohanpeshkar.videoassgn.utils.*
import java.util.*
import java.util.concurrent.TimeUnit


class VideoPlayerFragment : Fragment() {

    //Timer to handle timer
    private lateinit var mTimer: Timer

    //Runnable task executed after time difference
    private lateinit var mTimerTask: TimerTask

    //Subtitle adapter
    private lateinit var mSubtitleAdapter: SubtitleAdapter

    //Value to be retained from state changed
    private var mStopPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            mStopPosition = savedInstanceState.getInt(POSITION_FRAGMENT)
        }
        initializeVideoView()
        initializeSubtitles()
    }

    /**
     * Method to initialize video view, listeners
     */
    private fun initializeVideoView() {
        video_view.setVideoURI(getVideoUri(context?.packageName, R.raw.never_give_up))
        val mediaController = MediaController(context)
        mediaController.setAnchorView(lin_video_view)
        video_view.setMediaController(mediaController)
        video_view.setOnPreparedListener {
            start()
            initializeAndStartTimer(0)
            mediaController.show(0)
        }

        video_view.setOnCompletionListener {
            video_view.seekTo(1)
            mTimer.cancel()
            mSubtitleAdapter.scrollTo(-1)
            rev_sub_titles.smoothScrollToPosition(0)
        }
    }

    /**
     * Method to initialize and start timer task for subtitle
     */
    private fun initializeAndStartTimer(startMillis: Long) {
        mTimer = Timer()
        mTimerTask = object : TimerTask() {
            override fun run() {
                updateSubtitle()
            }
        }
        mTimer.schedule(mTimerTask, startMillis, DELAY_SUBTITLE_UPDATE)
    }

    /**
     * Initialize subtitles list
     */
    private fun initializeSubtitles() {
        rev_sub_titles.layoutManager = LinearLayoutManager(context)
        mSubtitleAdapter = SubtitleAdapter(
            SubtitleUtils.getSubtitlesFromFile(context, SRT_FILE_NAME)
        )
        { startTimeMillis, _ ->
            startTimeMillis?.toInt()?.let {
                video_view.seekTo(it)
            }
        }
        rev_sub_titles.adapter = mSubtitleAdapter
    }

    /**
     * Method to start the video in video view
     */
    private fun start() {
        video_view.start()
    }


    /**
     * Release player and cancel timer
     */
    private fun releasePlayer() {
        video_view.stopPlayback()
        mTimer.cancel()
        mTimerTask.cancel()
    }

    /**
     * Resume/start video
     */
    override fun onResume() {
        super.onResume()
        video_view.seekTo(mStopPosition)
        video_view.start()
        initializeAndStartTimer(mStopPosition.toLong())
    }

    /**
     * Pause video & timers
     */
    override fun onPause() {
        super.onPause()
        video_view.pause()
        mTimer.cancel()
        mTimerTask.cancel()
    }

    /**
     * Release and stop player
     */
    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    /**
     * Method to save position in instance state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mStopPosition = video_view.currentPosition
        outState.putInt(POSITION_FRAGMENT, mStopPosition)
    }


    /**
     * Method to update subtitle and update UI accordingly
     */
    private fun updateSubtitle() {
        activity?.runOnUiThread {
            val position = mSubtitleAdapter.getPositionFromSeconds(
                TimeUnit.MILLISECONDS
                    .toSeconds(video_view.currentPosition.toLong()).toInt()
            )

            position?.let {
                mSubtitleAdapter.scrollTo(it)
                val layoutManager = (rev_sub_titles.layoutManager as LinearLayoutManager)
                layoutManager.scrollToPositionWithOffset(it, 100.px)
            }
        }
    }

    companion object {
        fun create(): VideoPlayerFragment {
            return VideoPlayerFragment()
        }
    }


}