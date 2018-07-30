package com.mugdog.nicetimer.util

import android.content.Context
import android.preference.PreferenceManager
import com.mugdog.nicetimer.TimerActivity


class PrefUtil{
    companion object {

        fun getTimerLength( context : Context): Float{
            return 1f
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.mugdog.nicetimer.previous_timer_length"

        fun getPreviousTimerLengthSeconds(context: Context): Float{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getFloat(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 1f)
        }

        fun setPreviousTimerLengthSeconds(seconds: Float, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putFloat(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
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
            editor.apply()
        }

        private const val TIMER_REMAINING_ID = "com.mugdog.nicetimer.seconds_remaining"

        fun getTimerRemaining(context: Context) : Float{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getFloat(TIMER_REMAINING_ID, 0f)
        }

        fun setTimerRemaining(seconds: Float, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putFloat(TIMER_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.mugdog.nicetimer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}