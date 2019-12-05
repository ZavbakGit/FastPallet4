package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.domain.intity.MetaObj


class InteractorUseCaseSaveDellMetaObj(private val dao: CreatePalletUpdateDao) : UseCaseSaveDellMetaObj {
    override fun save(metaObject: MetaObj) {
        dao.delete(metaObject as CreatePalletDb)
    }

    override fun dell(metaObject: MetaObj) {

    }


}
