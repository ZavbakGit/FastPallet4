package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import io.reactivex.Flowable
import java.util.*

class InteractorUseCaseGetFlowableListDocument(val dao: DocumentDao) : UseCaseGetListMetaObj {

    override fun get(): Flowable<List<ItemListMetaObj?>> {
        return dao.getListDocument()
            .map {
                it.map { doc ->
                    ItemListMetaObj(
                        guid = doc.guid,
                        number = doc.number,
                        barcode = doc.barcode,
                        dataChanged = doc.dateChanged?.let { it1 -> Date(it1) },
                        date = doc.date?.let { it1 -> Date(it1) },
                        description = doc.description,
                        guidServer = doc.guidServer,
                        isWasLoadedLastTime = doc.isLastLoad,
                        status = Status.getStatusById(doc!!.status)!!,
                        type = Type.CREATE_PALLET,
                        typeFromServer = Type.CREATE_PALLET.nameServer
                    )
                }
            }
    }

}