package com.anit.fastpallet4.domain.usecase

import io.reactivex.Completable

interface UseCaseGetListDocFromServer{
    fun load():Completable
}