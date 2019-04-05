package com.anit.fastpallet4.presentaition.ui.screens.listdoc

import android.os.Bundle
import android.widget.PopupMenu
import com.anit.fastpallet4.R
import com.anit.fastpallet4.presentaition.navigation.RouterProvider
import com.anit.fastpallet4.presentaition.presenter.ListDocPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseFragment
import com.anit.fastpallet4.presentaition.ui.base.MyListFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fr_list_doc_scr.*

import java.io.Serializable


class ListDocFrScreen : BaseFragment(), ListDocView {

    class InputParamObj() : Serializable

    var inputParamObj: Serializable? = null

    companion object {

        val PARAM_KEY = "param"
        fun newInstance(inputParam: InputParamObj? = null): ListDocFrScreen {
            val bundle: Bundle = Bundle()
            bundle.putSerializable(PARAM_KEY, inputParam)
            var fragment = ListDocFrScreen()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ListDocPresenter

    @ProvidePresenter
    fun providePresenter() = ListDocPresenter(
        router = (activity as RouterProvider).getRouter(),
        inputParamObj = arguments?.getSerializable(PARAM_KEY) as? InputParamObj
    )



    override fun getLayout() = R.layout.fr_list_doc_scr
    override fun onBackPressed() = presenter.onBackPressed()


    override fun onStart() {
        super.onStart()

        var listFrag = MyListFragment.newInstance(presenter.getFlowableListItem())

        var transaction = getFragmentTransaction()
        transaction.replace(R.id.conteiner_frame_list, listFrag)
        transaction.commit()


        bagDisposable.add(
            listFrag.publishSubjectItemClick
                .subscribe {
                    var item = listFrag.getList().get(it)
                    presenter.onClickItem(item.identifier!!,item.type)
                }

        )

        tv_menu.setOnClickListener {
            presenter.onClickMainMenu()
        }

    }




    override fun showMainMenu(listmenu: List<Pair<Int, String>>) {
        var popupMenu = PopupMenu(activity, tv_menu)
        listmenu.forEach {
            popupMenu.menu.add(0, it.first, 0, it.second)
        }

        popupMenu.setOnMenuItemClickListener {
            presenter.onClickMainPopMenu(it.itemId)
        }

        popupMenu.show()

    }

}


