package com.mugdog.nicetimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.net.Uri.parse
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.mugdog.nicetimer.util.NotificationUtil
import com.mugdog.nicetimer.util.PrefUtil

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*

class TimerActivity : AppCompatActivity(), TimePickerFragment.onSetTimerListener {
    override fun onSetTimer(second_time: Float) {
        timerLengthSeconds = second_time
        secondsRemaining = second_time
        PrefUtil.setTimerLengthSeconds(second_time, this)
        setPreviousTimerLength();
        updateCountdownUI()
        background_timer.setImageResource( R.drawable.sub_g_star_btn )
    }

    init{
        instance = this;
    }

    companion object {
        private var instance: TimerActivity? = null

        fun applicationContext() : Context{
            return instance!!.applicationContext
        }

        fun playRingtone() {
            if(instance == null)    return
            if(instance?.ringtone!!.isPlaying)   return
            instance?.ringtone!!.play()
            instance?.background_timer!!.setImageResource(R.drawable.sub_g_alarm)
        }

        fun stopRingtone() {
            if(instance?.ringtone!!.isPlaying)
                instance?.ringtone!!.stop()
        }


        fun setAlarm(context: Context, secondsRemaining: Float): Long{
            val wakeUpTime = nowSeconds + (secondsRemaining*1000).toLong()
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
        Stopped, Paused, Running, Alarm
    }

    private lateinit var ringtone : Ringtone
    private var cdtimer: CountDownTimer? = null
    private var timerLengthSeconds = 0f
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0f
    private var listTimer : kotlin.collections.List<String> = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        supportActionBar?.title = "  Nice Timer"
        supportActionBar?.title


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
        progress_countdown.setTextColor(Color.TRANSPARENT)
        progress_countdown.innerContourSize = 0f
        progress_countdown.outerContourSize = 0f

        fab_play.setOnClickListener{
            onRunningTimer()
        }

        fab_stop.setOnClickListener {
            onStopTimer()
        }

        fab_setting.setOnClickListener {
            if(timerState == TimerState.Stopped || timerState == TimerState.Paused)
                setCountTimer()
        }

        tv_countdown.setOnClickListener {
            onTimerCenterClick()
        }

        tv_countdown.setOnLongClickListener {
            if(timerState == TimerState.Stopped || timerState == TimerState.Paused)
                setCountTimer()
            false;
        }

        background_timer.setOnClickListener {
            onTimerCenterClick()
        }
        background_timer.setOnLongClickListener {
            if(timerState == TimerState.Stopped || timerState == TimerState.Paused)
                setCountTimer()
            false;
        }
        background_timer.setImageResource( R.drawable.sub_g_star_btn)

//        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
//        if(alarmSetTime > 0){
//            val sec  = (nowSeconds - alarmSetTime)/1000.0f
//            if(timerState == TimerState.Alarm && sec > 30.0f){
//                PrefUtil.setTimerState(TimerState.Stopped, this)
//                setPreviousTimerLength();
//            }
//        }

        val myFont = Typeface.createFromAsset(applicationContext.assets, "fonts/ds_digib.ttf")
        tv_countdown.setTypeface(myFont);

        btTimeList.setTypeface(myFont)
        btTimeList.setOnClickListener { v ->
            btTimeListView.toggle()
        }
        btTimeListView.collapse()


        val rtUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(this, rtUri)



//        listTimer += "00:01:03"
//        listTimer += "00:02:00"
//        listTimer += "00:02:30"

        var ad = ArrayAdapter(this, android.R.layout.simple_list_item_1, listTimer)
        lvTimerHistory.adapter = ad


    }

    private fun onTimerCenterClick() {
        when(timerState){
            TimerState.Stopped -> {
                if(timerLengthSeconds <= 0f){
                    setCountTimer()
                }else{
                    onRunningTimer()
                }
            }
            TimerState.Alarm -> {
                onResetTimer()
            }
            TimerState.Running -> {
                onPauseTimer()
            }
            TimerState.Paused -> {
                onStartTimer()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }


    private fun onRunningTimer() {
        if(timerState == TimerState.Running){
            if(timerLengthSeconds <= 0f){
                cdtimer?.cancel()
                setCountTimer()
            }
            else{
                onPauseTimer()
            }
        }else if( timerState == TimerState.Paused || timerState == TimerState.Stopped){
            if(timerLengthSeconds <= 0f){
                cdtimer?.cancel()
                setCountTimer()
            }
            else
                onStartTimer()
        }else{
            onStopTimer()
        }
    }

    private fun setCountTimer() {
        onResetTimer()
        val newFragment = TimePickerFragment()
        val bundle = Bundle()
        bundle.putFloat("Timer", secondsRemaining)
        newFragment.arguments = bundle
        newFragment.show(supportFragmentManager, "Timer Setting")
    }


    private fun onPauseTimer(){
        cdtimer?.cancel()
        timerState = TimerState.Paused
        updateButtons()
    }

    private fun onStartTimer(){
        if(secondsRemaining <= 0.0f) return
        startTimer()
        timerState = TimerState.Running
        updateButtons()
    }
    private fun onStopTimer(){
        stopRingtone()
        if(timerState == TimerState.Running) cdtimer?.cancel()
        onTimerFinished()
        onResetTimer()
        updateButtons()
    }


    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this)

        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()
        when (timerState) {
            TimerState.Alarm -> NotificationUtil.showTimerExpired(this)
            TimerState.Running -> {
                cdtimer?.cancel()
                val wakeUpTime = setAlarm( this, secondsRemaining)
                NotificationUtil.showTimerRunning(this, wakeUpTime)
            }
            TimerState.Paused -> NotificationUtil.showTimerPause(this)
        }
        PrefUtil.setTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setTimerRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        Log.d("nicetimer", "initTimer")
        timerState = PrefUtil.getTimerState(this)
        if(timerState == TimerState.Stopped) {
            setNewTimerLength()
            background_timer.setImageResource( R.drawable.sub_g_star_btn )
        }
        else{
            setPreviousTimerLength()
        }


        secondsRemaining = if(timerState == TimerState.Alarm) 0f
                            else if(timerState == TimerState.Running || timerState == TimerState.Paused)
                                PrefUtil.getTimerRemaining(this)
                            else
                                timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0){
            val sec  = (nowSeconds - alarmSetTime)/1000.0f
            secondsRemaining -= sec
        }

        if(timerState == TimerState.Running || timerState == TimerState.Paused){
            if(secondsRemaining <= 0)   onTimerFinished()
            else    startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }


    private fun onTimerFinished() {
        timerState = TimerState.Alarm
        background_timer.setImageResource( R.drawable.sub_g_alarm )
        secondsRemaining = 0f
        PrefUtil.setTimerState(timerState, this)
        setNewTimerLength()
        PrefUtil.setTimerRemaining(timerLengthSeconds, this)
        updateCountdownUI()
        playRingtone();
    }

    private fun onResetTimer(){
        stopRingtone()
        instance?.background_timer!!.setImageResource(R.drawable.sub_g_star_btn)

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


        cdtimer = object : CountDownTimer((secondsRemaining * 1000).toLong(), 30){

            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = (millisUntilFinished.toFloat() / 1000)
                updateCountdownUI()
            }
        }.start()
    }



    private fun setNewTimerLength() {
//        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = PrefUtil.getTimerLengthSeconds(this)
        progress_countdown.maxValue = timerLengthSeconds*10

    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getTimerLengthSeconds(this)
        progress_countdown.maxValue = timerLengthSeconds*10
    }

    private fun getTimeString(value : Float) :String{
        if(value <= 0)  return ""

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

    private fun getTimeStringHHMMSS(value: Float): CharSequence? {
        val hour = (value / 3600).toInt()
        val minute = (value / 60).toInt() % 60
        val second = (value % 60).toInt()

        return "${if(hour.toString().length == 1) "0"+hour.toString() else hour.toString()}:${
            if(minute.toString().length == 1) "0"+minute.toString() else minute.toString()}:${
            if(second.toString().length == 1) "0"+second.toString() else second.toString()}"
    }

    private fun updateCountdownUI() {
        btTimeList.text = getTimeStringHHMMSS(timerLengthSeconds)
        tv_countdown.text = getTimeString(secondsRemaining)
        progress_countdown.setValue((timerLengthSeconds - secondsRemaining)*10)
    }


    private fun updateButtons() {
        when(timerState){
            TimerState.Running ->{
                fab_play.isEnabled = true
                fab_stop.isEnabled = true
                fab_play.setImageResource(R.drawable.ic_pause)
                background_timer.setImageResource(R.drawable.sub_g_pause_btn)
            }
            TimerState.Stopped ->{
                fab_play.isEnabled = true
                fab_stop.isEnabled = false
                fab_play.setImageResource(R.drawable.ic_play)
                if(timerLengthSeconds <= 0f)
                    background_timer.setImageResource(R.drawable.sub_g_set_btn)
                else
                    background_timer.setImageResource(R.drawable.sub_g_star_btn)
            }
            TimerState.Paused ->{
                fab_play.isEnabled = true
                fab_stop.isEnabled = true
                fab_play.setImageResource(R.drawable.ic_play)
                background_timer.setImageResource(R.drawable.sub_g_star_btn)
            }
            TimerState.Alarm ->{
                fab_play.isEnabled = false
                fab_stop.isEnabled = true
                fab_play.setImageResource(R.drawable.ic_play)
                background_timer.setImageResource( R.drawable.sub_g_alarm )
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        val itm = menu.findItem(R.id.menu_about)
        val info = this.packageManager.getPackageInfo(packageName, 0)
        itm.title = info.versionName

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_rating -> {

                val appPackageName = packageName // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (e: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }


//                val uris = Uri.parse("market://details?id=" + packageName.toString())
//                val intent = Intent(Intent.ACTION_VIEW, uris)
//                startActivity(intent)

            }
//            R.id.menu_settins -> {
//            }
            R.id.menu_about -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}
