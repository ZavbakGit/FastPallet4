package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import io.reactivex.Completable
import javax.inject.Inject

class InteractorGetListDocFromServer:UseCaseGetListDocFromServer{
    @Inject
    lateinit var daoNet: DaoNet

    init {
        App.appComponent.inject(this)
    }

    override fun load(): Completable {
        return daoNet.getListDocs()
            .ignoreElements()
    }

}