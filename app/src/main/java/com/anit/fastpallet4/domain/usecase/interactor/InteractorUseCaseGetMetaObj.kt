package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import javax.inject.Inject

class InteractorUseCaseGetMetaObj:UseCaseGetMetaObj{

    @Inject
    lateinit var dao: Dao

    init {
        App.appComponent.inject(this)
    }

    override fun get(guid: String): MetaObj? {
        return dao.getMetaObj(guid)
    }

}