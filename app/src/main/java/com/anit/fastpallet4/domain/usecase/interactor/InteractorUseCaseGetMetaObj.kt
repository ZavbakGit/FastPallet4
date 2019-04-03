package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.db.DaoDb
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import javax.inject.Inject

class InteractorUseCaseGetMetaObj:UseCaseGetMetaObj{

    @Inject
    lateinit var dao: DaoDb

    init {
        App.appComponent.inject(this)
    }

    override fun get(guid: String): MetaObj? {
        return dao.getMetaObj(guid)
    }

}