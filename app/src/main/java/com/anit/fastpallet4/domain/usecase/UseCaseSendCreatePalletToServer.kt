package com.anit.fastpallet4.domain.usecase

import com.anit.fastpallet4.domain.intity.MetaObj
import io.reactivex.Completable

interface UseCaseSendCreatePalletToServer{
    fun send(list:List<MetaObj>):Completable
}