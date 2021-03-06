package com.mugdog.nicetimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mugdog.nicetimer.util.NotificationUtil
import com.mugdog.nicetimer.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            AppConstants.ACTION_STOP -> {
                TimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getTimerRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerActivity.nowSeconds

                secondsRemaining -= (nowSeconds - alarmSetTime)/1000f
                PrefUtil.setTimerRemaining(secondsRemaining, context)

                TimerActivity.removeAlarm(context)
                PrefUtil.setTimerState(TimerActivity.TimerState.Paused, context)
                NotificationUtil.showTimerPause(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getTimerRemaining(context)
                val wakeupTime = TimerActivity.setAlarm(context, secondsRemaining)
                PrefUtil.setTimerState(TimerActivity.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeupTime)
            }
            AppConstants.ACTION_START -> {
                val name = PrefUtil.getTimerName(context)
                val secondsRemaining = PrefUtil.getTimerLengthSeconds(context)
                val wakeupTime = TimerActivity.setAlarm(context, secondsRemaining)
                PrefUtil.setTimerState(TimerActivity.TimerState.Running, context)
                PrefUtil.setTimerName(name, context)
                PrefUtil.setTimerRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeupTime)
            }
        }
    }
}
