package com.anit.fastpallet4.domain.usecase

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.extra.InfoPallet
import io.reactivex.Single

interface UseCaseGetInfoPallet{
    fun load(listNumber:List<String>): Single<List<InfoPallet>>
}