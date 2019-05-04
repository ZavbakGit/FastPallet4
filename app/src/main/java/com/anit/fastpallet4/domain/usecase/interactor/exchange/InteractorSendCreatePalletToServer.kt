package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfim
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfimResponse
import com.anit.fastpallet4.data.repositories.net.intity.SendCreatePalletDocModelResponse
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.UseCaseSendCreatePalletToServer


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


    fun saveStatus(list:List<ItemConfimResponse>){
        list.forEach {
            var metaObj = getMetaObjByGuidServer.get(it.guid!!,it.type!!)
            var stattus = Status.getStatusByString(it.status)
            metaObj!!.status = stattus!!
            metaObj.save()
        }
    }

    override fun send(list:List<MetaObj>): Completable {
        return daoNet.sendCreatePallet(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                saveStatus((it as SendCreatePalletDocModelResponse).listConfirm)
            }
            .ignoreElements()
    }

}