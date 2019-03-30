package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import io.reactivex.Flowable
import javax.inject.Inject

class InteractorUseCaseGetFlowableListMetaObj:UseCaseGetListMetaObj{


    @Inject
    lateinit var dao: Dao

    init {
        App.appComponent.inject(this)
    }

    override fun get(): Flowable<List<ItemListMetaObj?>> {
        return dao.getFlowableList()
    }

}