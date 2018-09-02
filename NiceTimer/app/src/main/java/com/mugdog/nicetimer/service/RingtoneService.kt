package com.mugdog.nicetimer.service

import android.app.Service
import android.media.RingtoneManager
import android.content.Intent
import android.os.IBinder
import android.media.Ringtone


class RingtoneService : Service() {
    private var ringtone: Ringtone? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val rtUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)
        this.ringtone = RingtoneManager.getRingtone(this, rtUri)

        this.ringtone?.play()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        this.ringtone?.stop()
    }
}