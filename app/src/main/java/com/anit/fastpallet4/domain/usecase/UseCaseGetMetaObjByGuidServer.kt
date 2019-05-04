package com.anit.fastpallet4.domain.usecase

import com.anit.fastpallet4.domain.intity.MetaObj

interface UseCaseGetMetaObjByGuidServer{
    fun get(guid:String,typeFromServer:String): MetaObj?
}