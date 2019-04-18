package com.anit.fastpallet4.data.repositories.net


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.intity.*
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.anit.fastpallet4.data.repositories.preferense.DaoPref
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.maping.Maping
import com.google.gson.Gson
import io.reactivex.Flowable
import javax.inject.Inject

class DaoNet {

    @Inject
    lateinit var managerNet: ManagerNet

    @Inject
    lateinit var daoPref: DaoPref

    @Inject
    lateinit var maping: Maping

    @Inject
    lateinit var gson: Gson


    init {
        App.appComponent.inject(this)
    }

    fun confirmDocs(list: List<MetaObj>): Flowable<ConfirmResponse> {
        var command = "command_confirm_doc_create_pallet"


        return Flowable.just(1)
            .flatMap {
                Flowable.just(daoPref.getCodeTsd())
            }
            .flatMap {
                if (it.isNullOrEmpty()) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Не заполнен код ТСД"))
                } else {
                    return@flatMap Flowable.just(it)
                }
            }
            .flatMap {
                var listDoc = list.map {
                    DocConfirm(it.guidServer!!, it.type!!.nameServer)
                }

                managerNet.reqest(
                    command = command,
                    objReqest = ConfirmDocLoadRequest(it as String, listDoc),
                    classReqest = ConfirmResponse::class.java
                )
            }.map {
                (it as ConfirmResponse)
            }
    }

    fun getListDocs(): Flowable<List<MetaObj>> {
        var command = "command_get_doc_create_pallet"


        return Flowable.just(1)
            .flatMap {
                Flowable.just(daoPref.getCodeTsd())
            }
            .flatMap {
                if (it.isNullOrEmpty()) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Не заполнен код ТСД"))
                } else {
                    return@flatMap Flowable.just(it)
                }
            }
            .flatMap {
                managerNet.reqest(
                    command = command,
                    objReqest = GetListDocsRequest(it as String),
                    classReqest = ListDocResponse::class.java
                )
            }
            .map {
                (it as ListDocResponse).listDocuments
            }
            .map {
                it.map { docResponse ->
                    maping.map(docResponse)
                }
            }
            .flatMap {
                if (it.any { it == null }) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Ошибка сериализации документа!"))
                } else {
                    return@flatMap Flowable.just(it)
                }
            }
            .map {
                it as? List<MetaObj>
            }
    }


    fun sendCreatePallet(list: List<MetaObj>): Flowable<Response> {
        var command = "command_send_doc_create_pallet"


        return Flowable.just(1)
            .flatMap {
                Flowable.just(daoPref.getCodeTsd())
            }
            .flatMap {
                if (it.isNullOrEmpty()) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Не заполнен код ТСД"))
                } else {
                    return@flatMap Flowable.just(it)
                }
            }
            .flatMap {
                managerNet.reqest(
                    command = command,
                    objReqest = SendDocumentsReqest(codeTSD = it.toString(), list = list),
                    classReqest = SendCreatePalletDocModelResponse::class.java
                )
            }
    }


}