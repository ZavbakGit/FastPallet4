package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfim
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.UseCaseSendCreatePalletToServer
import com.anit.fastpallet4.maping.getStatusByString

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class InteractorSendCreatePalletToServer : UseCaseSendCreatePalletToServer {
    @Inject
    lateinit var daoNet: DaoNet

    @Inject
    lateinit var getMetaObjByGuidServer: UseCaseGetMetaObjByGuidServer

    init {
        App.appComponent.inject(this)
    }

    override fun send(list:List<MetaObj>): Completable {
        return daoNet.sendCreatePallet(list)
            .map {
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .ignoreElements()
    }

}