package com.mugdog.nicetimer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mugdog.nicetimer.R
import com.mugdog.nicetimer.model.NTimer
import com.mugdog.nicetimer.util.TimerUtil
import android.widget.ImageButton
import com.mugdog.nicetimer.TimerActivity


class TimerAdapter(var ct: Context?, var resource: Int, var items: List<NTimer>) : ArrayAdapter<NTimer>(ct, resource, items) {

    private class ViewHolder(row: View?){
        var id = 0
        var txtName: TextView? = null
        var txtTime: TextView? = null
        var timer = 0f
        init{
            this.txtName = row?.findViewById(R.id.tvListName)
            this.txtTime = row?.findViewById(R.id.tvListTime)
        }
    }

    override fun getView(position: Int, cv: View?, parent: ViewGroup?): View {



        val holder: ViewHolder
        val v: View?



        if(cv == null) {
            val inflater = ct?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(resource, null)
            holder = ViewHolder(v)
            v?.tag = holder
        }else{
            v = cv
            holder = v.tag as ViewHolder
        }

        val bt = v?.findViewById(R.id.btDelete) as ImageButton
        bt.tag = items[position].id //important so we know which item to delete on button click

        bt.setOnClickListener(View.OnClickListener { v ->
            v.visibility = View.GONE
            notifyDataSetChanged()

            val id = v.tag as Int //get the position of the view to delete stored in the tag
            TimerActivity.removeTimer(id)
        })

        cv?.setOnClickListener { v ->
            TimerActivity.setTimerInfo( items[position].name, items[position].seconds )
        }



        val item = items[position]
        holder.txtName?.text = item.name
        holder.txtTime?.text = TimerUtil.getTimeStringHHMMSS(item.seconds)
        return v as View
    }



}