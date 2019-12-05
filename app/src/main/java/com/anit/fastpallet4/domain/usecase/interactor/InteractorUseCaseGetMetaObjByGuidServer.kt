package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.dbrealm.DaoDb
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer
import javax.inject.Inject

class InteractorUseCaseGetMetaObjByGuidServer: UseCaseGetMetaObjByGuidServer {


    @Inject
    lateinit var dao: DaoDb

    init {
        App.appComponent.inject(this)
    }

    override fun get(guid: String, typeFromServer: String): MetaObj? {
        return dao.getMetaObjByServerGuid(guid,typeFromServer)
    }

}