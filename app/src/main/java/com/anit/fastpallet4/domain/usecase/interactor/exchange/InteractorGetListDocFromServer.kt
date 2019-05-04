package com.anit.fastpallet4.domain.usecase.interactor.exchange

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.intity.ItemConfim
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer


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
        var dbDoc = getMetaObjByGuidServer.get(servDoc.guidServer ?: "", servDoc.typeFromServer!!)

        if (dbDoc == null) {
            return servDoc
        } else {

            when {
                dbDoc is CreatePallet -> {
                    var newList: MutableList<StringProduct> = mutableListOf()

                    newList.addAll((servDoc as CreatePallet).stringProducts)

                    dbDoc.stringProducts.forEach { itDb ->

                        var strProd = newList.find { it.guidProduct.equals(itDb.guidProduct) }

                        if (strProd != null) {
                            strProd.pallets = itDb.pallets
                        } else {
                            if (!itDb.pallets.isEmpty()) {
                                newList.add(itDb)
                            }
                        }
                    }

                    dbDoc.stringProducts = newList
                }


            }

            return dbDoc
        }
    }

    /**
     * Проставляем новый статус из прешедших подтверждений
     */
    private fun getListWithNewStatus(listDocument: List<MetaObj>, listConfirm: List<ItemConfim>): List<MetaObj> {
        return listDocument.map { document ->
            var status = listConfirm.find { it.guid == document.guidServer }?.status

            document.status = Status.getStatusByString(status)!!
            return@map document
        }
    }

    private fun confirmDocuments(listDocument: List<MetaObj>): Flowable<List<MetaObj>> {
        return daoNet.confirmDocs(listDocument)
            .map {
                it.listConfirm
            }
            .flatMap {
                var b = (it.map { it.guid }.sortedBy { it } ==
                        listDocument.map { it.guidServer }.sortedBy { it })

                if (!b) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Не верное подтверждение!"))
                } else {
                    return@flatMap Flowable.just(getListWithNewStatus(listDocument, it))
                }
            }
            .map {
                it as List<MetaObj>
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
            .observeOn(Schedulers.io())
            .toList()
            .toFlowable()
            .flatMap {
                confirmDocuments(it)
            }
            .flatMap {
                Flowable.fromIterable(it)
            }
            .map {
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                it.save()
            }
            .ignoreElements()
    }

}