package com.anit.fastpallet4.domain.usecase
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj
import io.reactivex.Flowable

interface UseCaseGetListMetaObj{
    fun get(): Flowable<List<ItemListMetaObj?>>
}