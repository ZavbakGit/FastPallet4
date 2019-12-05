package com.anit.fastpallet4.data.repositories.dbroom

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.BoxCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.PalletCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.ProductCreatePalletDb


@Database(
    entities = [
        CreatePalletDb::class,
        ProductCreatePalletDb::class,
        PalletCreatePalletDb::class,
        BoxCreatePalletDb::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCreatePalletUpdateDao(): CreatePalletUpdateDao
    abstract fun getDocumentDao(): DocumentDao
}