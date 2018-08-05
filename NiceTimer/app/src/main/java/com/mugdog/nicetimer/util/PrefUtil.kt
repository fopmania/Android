package com.mugdog.nicetimer.util

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.mugdog.nicetimer.TimerActivity


class PrefUtil{
    companion object {

        private const val TIMER_LENGTH_SECONDS_ID = "com.mugdog.nicetimer.timer_length"

        fun getTimerLengthSeconds(context: Context): Float{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getFloat(TIMER_LENGTH_SECONDS_ID, 0f)
        }

        fun setTimerLengthSeconds(seconds: Float, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putFloat(TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.mugdog.nicetimer.timer_state"

        fun getTimerState(context: Context) : TimerActivity.TimerState{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = pref.getInt(TIMER_STATE_ID, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            Log.d("nicetimer", "setTimerState: " + ordinal)
            editor.apply()
        }

        private const val TIMER_REMAINING_ID = "com.mugdog.nicetimer.seconds_remaining"

        fun getTimerRemaining(context: Context) : Float{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val tr = pref.getFloat(TIMER_REMAINING_ID, 0f)
            Log.d("nicetimer", "setTimerState: " + tr)
            return tr
        }

        fun setTimerRemaining(seconds: Float, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putFloat(TIMER_REMAINING_ID, seconds)
            Log.d("nicetimer", "setTimerRemaining: " + seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.mugdog.nicetimer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val st = pref.getLong(ALARM_SET_TIME_ID, 0)
            Log.d("nicetimer", "getAlarmSetTime: " + st)
            return st
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            Log.d("nicetimer", "setAlarmSetTime: " + time)
            editor.apply()
        }
    }
}