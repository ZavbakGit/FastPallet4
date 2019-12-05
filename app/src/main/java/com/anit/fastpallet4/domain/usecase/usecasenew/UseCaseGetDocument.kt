package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.domain.intity.MetaObj
import io.reactivex.Flowable

interface UseCaseGetDocument{
    fun get(guid:String):Flowable<MetaObj>
}