package com.anit.fastpallet4.presentaition.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


abstract class BaseAdapterListFragment<T>(val mContext: Context) : BaseAdapter() {
    var mlist: List<T> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    abstract fun getLayaot(): Int
    abstract fun bindView(item: T,holder:Any)
    abstract fun createViewHolder(view: View) :Any


    fun getList():List<T> = mlist
    override fun getCount() = mlist.size
    override fun getItem(position: Int) = mlist[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: Any

        if (convertView == null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(getLayaot(), parent, false)
            viewHolder = createViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag
        }
        bindView(mlist[position],viewHolder)
        return convertView
    }

}