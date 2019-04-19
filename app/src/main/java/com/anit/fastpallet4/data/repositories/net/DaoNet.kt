package com.anit.fastpallet4.data.repositories.net


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.intity.*
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.anit.fastpallet4.data.repositories.preferense.DaoPref
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.extra.InfoPallet
import com.anit.fastpallet4.domain.utils.getDecimalStr
import com.anit.fastpallet4.maping.Maping
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.Single
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

    fun getInfoPallet(listPalletNumber: List<String>): Single<List<InfoPallet>> {
        var command = "command_get_pallet_state"

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
                    objReqest = GetInfoPalletReqest(codeTSD = it.toString(), list = listPalletNumber),
                    classReqest = GetInfoPalletResponse::class.java
                )
            }
            .map {
                it as GetInfoPalletResponse
            }
            .map {
                it.list.map {
                    InfoPallet(
                        code = it.PaletCode,
                        guid = it.PaletGuid,
                        countBox = getDecimalStr(it.PaletPlacesCount).toIntOrNull()?:0,
                        count = getDecimalStr(it.PaletWeight).toFloatOrNull()?:0f,
                        nameProduct = it.nameProduct,
                        sclad = it.PaletStore,
                        state = it.PaletState,
                        weightBarcode = it.barcode,
                        weightCoffProduct = getDecimalStr(it.weightCoffProduct).toFloatOrNull()?:0f,
                        weightEndProduct = getDecimalStr(it.weightEndProduct).toIntOrNull()?:0,
                        weightStartProduct = getDecimalStr(it.weightStartProduct).toIntOrNull()?:0
                    )
                }
            }
            .first(listOf())
    }


}