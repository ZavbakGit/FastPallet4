package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.domain.intity.MetaObj

interface UseCaseSaveDellMetaObj{
    fun save(metaObject: MetaObj)
    fun dell(metaObject: MetaObj)
}