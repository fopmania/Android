package com.mugdog.nicetimer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.ikovac.timepickerwithseconds.TimePicker
import com.mugdog.nicetimer.view.TimePickerwithSecondsDialog


class TimePickerFragment : DialogFragment(), TimePickerwithSecondsDialog.OnTimeSetListener {
    private lateinit var mListener: TimePickerFragment.onSetTimerListener
    var name_time = ""

    interface onSetTimerListener{
        fun onSetTimer(name: String, second_time: Float)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is onSetTimerListener)
            mListener = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        name_time = arguments!!.getString("Name", "")
        val sec_time = arguments!!.getFloat("Timer", 0f)
        val hour = (sec_time/3600).toInt()
        val minute = (sec_time/60).toInt()%60
        val second = (sec_time%60).toInt()


        return TimePickerwithSecondsDialog(
//        return MyTimePickerDialog(
                activity,
                R.style.DialogTheme,
                this,
                hour,
                minute,
                second,
                true // Is 24 hour view
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int, seconds: Int) {
        mListener.onSetTimer(name_time, (hourOfDay*3600 + minute*60 + seconds).toFloat())
    }



}
