package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfim
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfimResponse
import com.anit.fastpallet4.data.repositories.net.intity.SendCreatePalletDocModelResponse
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.extra.InfoPallet
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetInfoPallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer


import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class InteractorGetInfoPallet : UseCaseGetInfoPallet {
    @Inject
    lateinit var daoNet: DaoNet

    @Inject
    lateinit var getMetaObjByGuidServer: UseCaseGetMetaObjByGuidServer

    init {
        App.appComponent.inject(this)
    }

    override fun load(listNumber: List<String>): Single<List<InfoPallet>> {
        return daoNet.getInfoPallet(listNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}