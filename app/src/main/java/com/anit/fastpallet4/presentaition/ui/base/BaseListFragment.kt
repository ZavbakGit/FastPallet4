package com.anit.fastpallet4.presentaition.ui.base


import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.KeyEvent
import android.view.View
import android.widget.ListView
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.ui.util.EventKeyClick
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

open abstract class BaseListFragment<T> : ListFragment() {

    var flowableListItem: Flowable<List<T>> = Flowable.empty()
    val publishSubjectItemClick = PublishSubject.create<Int>()
    val publishSubjectKeyClick = PublishSubject.create<EventKeyClick>()


    protected abstract fun getlayoutItem(): Int
    protected abstract fun bindView(item: T, holder: Any)
    protected abstract fun createViewHolder(view: View): Any


    private val bagDisposable = CompositeDisposable()
    private lateinit var adapter: BaseAdapterListFragment<T>

    fun getList()= adapter.getList()

    override fun onStop() {
        super.onStop()
        bagDisposable.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = Adapter(activity!!)
        setListAdapter(adapter)
        listView.setSelector(R.drawable.selector)


        listView.setOnKeyListener { view, keyKode, keyEvent ->

            val position = listView.getSelectedItemPosition()

            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                publishSubjectKeyClick.onNext(EventKeyClick(keyCode = keyKode,id = position))
            }
            false
        }


        bagDisposable.add(
            flowableListItem
                .subscribe {
                    adapter.mlist = it
                }
        )
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        publishSubjectItemClick.onNext(position)
    }

    private inner class Adapter(mContext: Context) : BaseAdapterListFragment<T>(mContext) {
        override fun bindView(item: T, holder: Any) {
            this@BaseListFragment.bindView(item, holder)
        }

        override fun getLayaot(): Int = this@BaseListFragment.getlayoutItem()
        override fun createViewHolder(view: View): Any = this@BaseListFragment.createViewHolder(view)
    }

}