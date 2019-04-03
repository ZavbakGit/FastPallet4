package com.anit.fastpallet4.data.repositories.net


import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.intity.getlistdocs.GetListDocsRequest
import com.anit.fastpallet4.data.repositories.net.intity.getlistdocs.ListDocResponse
import com.anit.fastpallet4.data.repositories.preferense.DaoPref
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.maping.Maping
import io.reactivex.Flowable
import javax.inject.Inject

class DaoNet {

    @Inject
    lateinit var managerNet: ManagerNet

    @Inject
    lateinit var daoPref: DaoPref

    @Inject
    lateinit var maping: Maping


    init {
        App.appComponent.inject(this)
    }

    fun getListDocs(): Flowable<List<MetaObj?>> {
        var command = "command_confirm_doc_create_pallet"
        var codeTsd = daoPref.getCodeTsd()

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
                if (it.all { it == null}) {
                    return@flatMap Flowable.error<Throwable>(Throwable("Ошибка сериализации документа!"))
                }else{
                    return@flatMap Flowable.just(it)
                }
            }
            .map {
                it as List<MetaObj>
            }
    }
}