package ru.dima.firstproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        when (p1?.action) {
            Actions.PLAY.toString() -> {
                Intent(p0, MusicService::class.java).also {
                    it.action = Actions.PLAY.toString()
                    p0?.startService(it)
                }
            }

            Actions.NEXT.toString() -> {
                Intent(p0, MusicService::class.java).also {
                    it.action = Actions.NEXT.toString()
                    p0?.startService(it)
                }
            }

            Actions.PREV.toString() -> {
                Intent(p0, MusicService::class.java).also {
                    it.action = Actions.PREV.toString()
                    p0?.startService(it)
                }
            }
        }
    }
}