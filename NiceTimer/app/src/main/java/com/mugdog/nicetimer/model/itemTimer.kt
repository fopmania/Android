package com.mugdog.nicetimer.model

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class itemTimer(id: Int, title: String)


class listTimerAdapter(context : Context, listTimer : ArrayList<itemTimer>) : BaseAdapter() {
    override fun getItem(position: Int) {

    }

    override fun getCount(): Int {

        return 0

    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView!!
    }

}