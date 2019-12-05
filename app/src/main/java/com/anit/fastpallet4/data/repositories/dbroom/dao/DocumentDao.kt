package com.anit.fastpallet4.data.repositories.dbroom.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import io.reactivex.Flowable

@Dao
interface DocumentDao{

    @Query("SELECT * FROM CreatePalletDb")
    fun getListDocument(): Flowable<List<CreatePalletDb>>
}