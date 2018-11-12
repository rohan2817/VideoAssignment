package me.rohanpeshkar.videoassgn.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.rohanpeshkar.videoassgn.R

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        //Loading Video Player fragment
        supportFragmentManager.beginTransaction().add(
            R.id.frl_video,
            VideoPlayerFragment.create()
        ).commit()
    }
}