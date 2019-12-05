package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import java.util.*

class InteractorUseCaseGetMetaObjDb(private val dao: CreatePalletUpdateDao):UseCaseGetMetaObj{
    override fun get(guid: String): MetaObj? {
        return dao.getDocByGuid(guid)
    }
}


fun CreatePalletDb.toObject():MetaObj{
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