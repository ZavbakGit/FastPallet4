package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import io.reactivex.Flowable
import java.util.*

class InteractorUseCaseGetDocumentDb(private val dao: DocumentDao):UseCaseGetDocument{

    override fun get(guid: String): Flowable<MetaObj> {
        return dao.getDocument(guid).
                map {
                    it.toObject()
                }
    }
}
fun CreatePalletDb.toObject():CreatePallet{
    val doc = CreatePallet()
    doc.guid = this.guid
    doc.guidServer = this.guidServer
    doc.status = Status.getStatusById(this.status)!!
    doc.number = this.number
    doc.date = this.date?.let { Date(it) }
    doc.dataChanged = this.dateChanged?.let { Date(it) }
    doc.isWasLoadedLastTime = this.isLastLoad
    doc.description = this.description
    doc.barcode = this.barcode

    return doc
}