package com.mugdog.nicetimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.mugdog.nicetimer.util.NotificationUtil
import com.mugdog.nicetimer.util.PrefUtil

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*

class TimerActivity : AppCompatActivity(), TimePickerFragment.onSetTimerListener {
    override fun onSetTimer(second_time: Float) {
        timerLengthSeconds = second_time
        secondsRemaining = second_time
        PrefUtil.setPreviousTimerLengthSeconds(second_time, this)
        setPreviousTimerLength();
        updateCountdownUI()
    }

    init{
        instance = this;
    }

    companion object {
        private var instance: TimerActivity? = null

        fun applicationContext() : Context{
            return instance!!.applicationContext
        }

        fun getRingtone() : Ringtone{
            return instance!!.ringtone
        }


        fun setAlarm(context: Context, secondsRemaining: Float): Long{
            val wakeUpTime = (nowSeconds + (secondsRemaining*1000).toLong())
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
            get() = Calendar.getInstance().timeInMillis
//            get() = System.currentTimeMillis()


    }

    enum class TimerState {
        Stopped, Paused, Running, Alram
    }

    private lateinit var ringtone : Ringtone
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0f
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0f


    fun getRington() : Ringtone  {   return ringtone     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.title = "     Nice Timer"

        fab_play.setOnClickListener{ v ->
            onClickStart()
        }

        fab_pause.setOnClickListener { v ->
            onClickPause()
        }

        fab_stop.setOnClickListener { v ->
            onClickStop()
        }

        progress_countdown.isSeekModeEnabled = false
        progress_countdown.setBarColor(
                ContextCompat.getColor(this, R.color.colorBar1),
                ContextCompat.getColor(this, R.color.colorBar2),
                ContextCompat.getColor(this, R.color.colorBar3),
                ContextCompat.getColor(this, R.color.colorBar4)
        )

        progress_countdown.barWidth = 30
        progress_countdown.rimWidth = 35
        progress_countdown.rimColor = Color.TRANSPARENT
        progress_countdown.innerContourSize = 0f
        progress_countdown.outerContourSize = 0f

        tv_countdown.setOnClickListener {
            when(timerState){
                TimerState.Stopped -> {
                    setCountTimer()
                    // or  Start
                    // onClickStart()
                }
                TimerState.Alram -> {
                    onTimerReset()
                }
                TimerState.Running -> {
                    onClickPause()
                }
                TimerState.Paused -> {
                    onClickStart()
                }
            }
        }
        val myFont = Typeface.createFromAsset(applicationContext.assets, "fonts/ds_digib.ttf")
        tv_countdown.setTypeface(myFont);

        btTimeList.setOnClickListener { v ->
            btTimeListBar.toggle()
        }

        background_timer.setImageResource( R.drawable.sub_g_set_btn)

        val rtUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(this, rtUri)

    }

    private fun setCountTimer() {
        val newFragment = TimePickerFragment()
        val bundle = Bundle()
        bundle.putFloat("Timer", secondsRemaining)
        newFragment.arguments = bundle
        newFragment.show(supportFragmentManager, "Timer Setting")
    }


    private fun onClickPause(){
        timer.cancel()
        timerState = TimerState.Paused
        updateButtons()
        background_timer.setImageResource( R.drawable.sub_g_star_btn )
    }

    private fun onClickStart(){
        if(secondsRemaining <= 0.0f) return
        startTimer()
        timerState = TimerState.Running
        updateButtons()
        background_timer.setImageResource( R.drawable.sub_g_pause_btn )
    }
    private fun onClickStop(){
        ringtone.stop()
        timer.cancel()
        onTimerFinished()
        onTimerReset()
        background_timer.setImageResource( R.drawable.sub_g_set_btn )
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
            val wakeUpTime = setAlarm( this, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
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
            val sec  = (nowSeconds - alarmSetTime)/1000.0f
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
        timerState = TimerState.Alram
        secondsRemaining = 0f
        PrefUtil.setTimerState(timerState, this)
        setNewTimerLength()
        PrefUtil.setTimerRemaining(timerLengthSeconds, this)
        updateCountdownUI()

        if(!ringtone.isPlaying)
            ringtone.play()
    }

    private fun onTimerReset(){
        if(ringtone.isPlaying)
            ringtone.stop()

        timerState = TimerState.Stopped
        PrefUtil.setTimerState(timerState, this)

        setNewTimerLength()

        progress_countdown.setValue(0f)

        PrefUtil.setTimerRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }


    private fun startTimer() {
        timerState = TimerState.Running

        background_timer.setImageResource( R.drawable.sub_g_pause_btn )


        timer = object : CountDownTimer((secondsRemaining * 1000).toLong(), 30){

            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = (millisUntilFinished.toFloat() / 1000)
                updateCountdownUI()
            }
        }.start()
    }
    private fun setNewTimerLength() {
//        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
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

    fun doAlram(){

    }
}
