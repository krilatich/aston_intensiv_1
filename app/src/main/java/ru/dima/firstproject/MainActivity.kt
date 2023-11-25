package ru.dima.firstproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    private lateinit var next: ImageView
    private lateinit var prev: ImageView
    private lateinit var play: ImageView
    private lateinit var title: TextView
    private lateinit var image: ImageView

    private lateinit var br: BroadcastReceiver
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        getViews()
        setObservers()
        setListeners()
        registerBroadcastReceiver()
        checkTrack()
    }

    private fun getViews() {
        next = findViewById(R.id.next)
        prev = findViewById(R.id.prev)
        play = findViewById(R.id.play)
        title = findViewById(R.id.title)
        image = findViewById(R.id.trackImage)
    }

    private fun setListeners() {
        next.setOnClickListener { nextClicked() }
        prev.setOnClickListener { prevClicked() }
        play.setOnClickListener { playClicked() }
    }

    private fun setObservers() {
        viewModel.trackName.observe(this) {
            title.text = it
        }
        viewModel.trackImage.observe(this) {
            image.setImageResource(it)
        }
        viewModel.isPlaying.observe(this) {
            if (it) {
                play.setImageResource(R.drawable.ic_pause)
            } else play.setImageResource(R.drawable.ic_play)
        }
    }

    private fun registerBroadcastReceiver() {
        br = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                intent?.run {
                    getStringExtra("track_name")
                        ?.let(viewModel::updateTrackName)
                    getBooleanExtra("is_playing", false)
                        .let(viewModel::updateIsPlaying)
                    getIntExtra("track_image", R.drawable.ic_launcher_foreground)
                        .let(viewModel::updateTrackImage)
                }
            }
        }
        val intFilter = IntentFilter("track_action")
        LocalBroadcastManager.getInstance(this).registerReceiver(br, intFilter)
    }

    private fun nextClicked() {
        Intent(applicationContext, MusicService::class.java).also {
            it.action = Actions.NEXT.toString()
            startService(it)
        }
    }

    private fun prevClicked() {
        Intent(applicationContext, MusicService::class.java).also {
            it.action = Actions.PREV.toString()
            startService(it)
        }
    }

    private fun playClicked() {
        Intent(this, MusicService::class.java).also {
            it.action = Actions.PLAY.toString()
            startService(it)
        }
    }

    private fun checkTrack() {
        Intent(this, MusicService::class.java).also {
            it.action = Actions.CHECK.toString()
            startService(it)
        }
    }
}
