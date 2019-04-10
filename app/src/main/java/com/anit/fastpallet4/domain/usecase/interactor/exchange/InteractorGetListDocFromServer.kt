package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObjByGuidServer
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InteractorGetListDocFromServer : UseCaseGetListDocFromServer {
    @Inject
    lateinit var daoNet: DaoNet

    @Inject
    lateinit var getMetaObjByGuidServer: UseCaseGetMetaObjByGuidServer

    init {
        App.appComponent.inject(this)
    }


    private fun joinDocServAndDB(servDoc: MetaObj): MetaObj {
        var docDb = getMetaObjByGuidServer.get(servDoc.guidServer ?: "")

        if (docDb == null) {
            return servDoc
        } else {

            return docDb
        }
    }

    override fun load(): Completable {
        return daoNet.getListDocs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Flowable.fromIterable(it)
            }
            .map {
                joinDocServAndDB(it)
            }
            .doOnNext {
                it.save()
            }
            .ignoreElements()
    }

}