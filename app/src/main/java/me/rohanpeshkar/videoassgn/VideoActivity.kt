package me.rohanpeshkar.videoassgn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        title = "Video"
        supportFragmentManager.beginTransaction()
            .add(R.id.frl_video, VideoPlayerFragment.create()).commit()
    }
}