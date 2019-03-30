package com.anit.fastpallet4.presentaition.ui.base

import android.view.View
import android.widget.TextView
import com.anit.fastpallet4.R
import io.reactivex.Flowable

class MyListFragment : BaseListFragment<ItemList>() {

    companion object {
        fun newInstance(flowableList:Flowable<List<ItemList>>): MyListFragment {
            var fragment = MyListFragment()
            fragment.flowableListItem = flowableList
            return fragment
        }
    }

    override fun getlayoutItem(): Int = R.layout.fr_list_doc_item

    override fun bindView(item: ItemList, holder: Any) {
        (holder as ViewHolder).tv_info.text = item.info
        holder.tv_left.text = item.left
        holder.tv_right.text = item.right
    }

    override fun createViewHolder(view: View): Any =
        ViewHolder(view)

    internal class ViewHolder(view: View) {
        var tv_info: TextView = view.findViewById(R.id.tv_item_info)
        var tv_left: TextView = view.findViewById(R.id.tv_info__doc_left)
        var tv_right: TextView = view.findViewById(R.id.tv_info_doc_right)
    }

}