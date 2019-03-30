package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseMetaObj
import javax.inject.Inject

class InteractorUseCaseMetaObj(val metaObj: MetaObj) : UseCaseMetaObj {

    @Inject
    lateinit var dao: Dao

    init {
        App.appComponent.inject(this)
    }

    override fun dell() {
        dao.dell(metaObj)
    }

    override fun save() {
        dao.save(metaObj)
    }

}
