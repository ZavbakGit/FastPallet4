package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet


class InteractorUseCaseSaveDellMetaObjDb(private val dao: CreatePalletUpdateDao) :
    UseCaseSaveDellMetaObj {
    override fun save(metaObject: MetaObj) {
        when (metaObject.type) {
            Type.CREATE_PALLET -> {
                val doc = metaObject as CreatePallet
                dao.insertOrUpdate(doc.toDb())
            }
        }
    }

    override fun dell(metaObject: MetaObj) {
        when (metaObject.type) {
            Type.CREATE_PALLET -> {
                val doc = metaObject as CreatePallet
                dao.delete(doc.toDb())
            }
        }
    }
}


fun CreatePallet.toDb(): CreatePalletDb {
    return CreatePalletDb(
        guid = this.guid!!,
        status = this.status.id,
        guidServer = this.guidServer,
        description = this.description,
        date = this.date?.time,
        barcode = this.barcode,
        number = this.number,
        dateChanged = this.dataChanged?.time,
        isLastLoad = this.isWasLoadedLastTime

    )
}
