package com.mugdog.nicetimer

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import com.mugdog.nicetimer.R.id.tv_countdown
import com.mugdog.nicetimer.util.NotificationUtil
import com.mugdog.nicetimer.util.PrefUtil

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

class TimerActivity : AppCompatActivity(), TimePickerFragment.onSetTimerListener {
    override fun onSetTimer(minute_time: Float) {
        PrefUtil.setPreviousTimerLengthSeconds(minute_time, this)
        initTimer()
    }

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Float): Float{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime.toLong(), pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)

            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0f;
    private var timerState = TimerState.Stopped;
    private var secondsRemaining = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "     Nice Timer"



        fab_play.setOnClickListener{ v ->
            if(secondsRemaining <= 0.0f) return@setOnClickListener
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }

        progress_countdown.isSeekModeEnabled = false

        progress_countdown.setBarColor(
                ContextCompat.getColor(this, R.color.colorBar1),
                ContextCompat.getColor(this, R.color.colorBar2),
                ContextCompat.getColor(this, R.color.colorBar3),
                ContextCompat.getColor(this, R.color.colorBar4)
        )
        progress_countdown.barWidth = 80

        progress_countdown.rimWidth = 100
        progress_countdown.rimColor = Color.GRAY

        tv_countdown.setOnClickListener { v ->
            if(timerState == TimerState.Stopped){
                val newFragment = TimePickerFragment()
                val bundle = Bundle()
                bundle.putFloat("Timer", secondsRemaining)
                newFragment.arguments = bundle

                newFragment.show(supportFragmentManager, "Timer Setting")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()

        removeAlarm(this)

        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm( this, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime.toLong())
        }else if(timerState == TimerState.Paused){
            NotificationUtil.showTimerPause(this)
        }
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setTimerRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)
        if(timerState == TimerState.Stopped){
            setNewTimerLength()
        }else{
            setPreviousTimerLength()
        }
        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getTimerRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0){
            val sec  = nowSeconds - alarmSetTime
            secondsRemaining -= sec
        }

        if(secondsRemaining <= 0)
            onTimerFinished()
        else if(timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_countdown.setValue(0f)

        PrefUtil.setTimerRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining.toLong() * 1000, 30){

            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = (millisUntilFinished.toFloat() / 1000)
                updateCountdownUI()
            }
        }.start()
    }
    private fun setNewTimerLength() {
//        val lengthInMinutes = PrefUtil.getTimerLength(this)
        val lengthInMinutes = PrefUtil.getPreviousTimerLengthSeconds(this)
        timerLengthSeconds = (lengthInMinutes * 60)
        progress_countdown.maxValue = timerLengthSeconds*10

    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.maxValue = timerLengthSeconds*10
    }

    private fun getTimeString(value : Float) :String{
        val hour = (value / 3600).toInt()
        val minute = (value / 60).toInt() % 60
        val second = (value % 60).toInt()

        if(value < 60){
            tv_countdown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 110f)
            return  second.toString()
        }
        else if(value < 3600){
            if(minute.toString().length == 1)
                tv_countdown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 85f)
            else
                tv_countdown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70f)

            return  minute.toString() + ":" +
                    "${if(second.toString().length == 1) "0"+second.toString() else second.toString()}"
        }

        tv_countdown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
        return "${if(hour.toString().length == 1) "0"+hour.toString() else hour.toString()}\n${
                    if(minute.toString().length == 1) "0"+minute.toString() else minute.toString()}:${
                        if(second.toString().length == 1) "0"+second.toString() else second.toString()}"
    }


    private fun updateCountdownUI() {
        tv_countdown.text = getTimeString(secondsRemaining)

        progress_countdown.setValue((timerLengthSeconds - secondsRemaining)*10)
    }

    private fun updateButtons() {
        when(timerState){
            TimerState.Running ->{
                fab_play.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Stopped ->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.Paused ->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
