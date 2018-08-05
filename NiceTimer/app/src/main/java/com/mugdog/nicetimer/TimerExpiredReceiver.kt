package com.mugdog.nicetimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mugdog.nicetimer.util.PrefUtil
import android.media.RingtoneManager
import android.media.Ringtone
import android.util.Log


class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("nicetimer", "TimerExpiredReceiver.onReceive ")
        PrefUtil.setTimerState(TimerActivity.TimerState.Alarm, context)
        PrefUtil.setAlarmSetTime(0, context)
        TimerActivity.playRingtone()
    }
}
