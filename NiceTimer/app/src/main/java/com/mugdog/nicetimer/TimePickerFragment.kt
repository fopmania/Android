package com.mugdog.nicetimer

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TimePicker
import kotlin.math.min


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var mListener: TimePickerFragment.onSetTimerListener

    interface onSetTimerListener{
        fun onSetTimer(minute_time: Float)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is onSetTimerListener)
            mListener = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sec_time = arguments!!.getFloat("Timer", 0f)
        val hour = (sec_time/3600).toInt()
        val minute = (sec_time/60).toInt()%60
        val second = sec_time%60

        return TimePickerDialog(
                activity, // Context
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, // Theme
                this, // TimePickerDialog.OnTimeSetListener
                hour, // Hour of day
                minute, // Minute
                true // Is 24 hour view
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mListener.onSetTimer((hourOfDay*60 + minute).toFloat())
    }

}
