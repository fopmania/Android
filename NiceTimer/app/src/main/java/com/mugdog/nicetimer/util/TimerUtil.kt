package com.mugdog.nicetimer.util


class TimerUtil {
    companion object {
        fun getTimeStringHHMMSS(value: Float): CharSequence? {
            val hour = (value / 3600).toInt()
            val minute = (value / 60).toInt() % 60
            val second = (value % 60).toInt()

            return "${if(hour.toString().length == 1) "0"+hour.toString() else hour.toString()}:${
            if(minute.toString().length == 1) "0"+minute.toString() else minute.toString()}:${
            if(second.toString().length == 1) "0"+second.toString() else second.toString()}"
        }

    }
}