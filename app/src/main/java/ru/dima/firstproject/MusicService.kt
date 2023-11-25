package ru.dima.firstproject

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.dima.firstproject.data.MusicRepositoryImpl
import ru.dima.firstproject.data.model.Track

class MusicService : Service() {

    private val tracks = MusicRepositoryImpl().getMusicList()
    private var position: Int = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.PLAY.toString() -> playClicked()

            Actions.NEXT.toString() -> nextClicked()

            Actions.PREV.toString() -> prevClicked()

            Actions.CHECK.toString() -> sendTrackToActivity(tracks[position])
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun nextClicked() {
        stopMusic()
        if (position == tracks.size - 1) position = 0
        else position++
        playMusic(tracks[position].srcAudio)
        sendTrackToActivity(tracks[position])
        showNotification(R.drawable.ic_pause)
    }

    private fun prevClicked() {
        stopMusic()
        if (position == 0) position = tracks.size - 1
        else position--
        playMusic(tracks[position].srcAudio)
        sendTrackToActivity(tracks[position])
        showNotification(R.drawable.ic_pause)
    }

    private fun playClicked() {
        if (mediaPlayer?.isPlaying != true) {
            if (mediaPlayer == null)
                playMusic(tracks[position].srcAudio)
            else
                mediaPlayer?.start()
            sendTrackToActivity(tracks[position])
            showNotification(R.drawable.ic_pause)
        } else {
            mediaPlayer?.pause()
            sendTrackToActivity(tracks[position])
            showNotification(R.drawable.ic_play)
        }
    }

    private fun playMusic(src: Int) {
        mediaPlayer = MediaPlayer.create(this, src)
        mediaPlayer?.apply {
            setOnCompletionListener {
                stopMusic()
                sendTrackToActivity(tracks[position])
            }
        }?.start()
    }

    private fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun sendTrackToActivity(track: Track) {
        val intent = Intent("track_action")
        intent
            .putExtra("track_name", track.name)
            .putExtra("is_playing", mediaPlayer?.isPlaying)
            .putExtra("track_image", track.srcImage)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun showNotification(playBtn: Int) {
        val mediaSession = MediaSessionCompat(applicationContext, "PlayerAudio")
        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val prevIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Actions.PREV.toString())
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Actions.PLAY.toString())
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent =
            Intent(this, NotificationReceiver::class.java).setAction(Actions.NEXT.toString())
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val picture = BitmapFactory.decodeResource(resources, tracks[position].srcImage)
        val notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_launcher_foreground).setLargeIcon(picture)
            .setContentTitle(tracks[position].name)
            .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent)
            .addAction(playBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent).setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            ).setPriority(NotificationCompat.PRIORITY_LOW).setContentIntent(contentIntent)
            .setOnlyAlertOnce(true).build()

        startForeground(1, notification)
    }
}