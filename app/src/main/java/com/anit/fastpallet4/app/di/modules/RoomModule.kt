package com.anit.fastpallet4.app.di.modules

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.anit.fastpallet4.data.repositories.dbroom.AppDatabase
import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule{

    @Provides
    @Singleton
    fun getDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mydatabase")
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

//                        getListTriggerCreatePallet().forEach {
//                            db.execSQL(it)
//                        }
                    }
                }

            )
            //.allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun getCreatePalletUpdateDao(database: AppDatabase): CreatePalletUpdateDao {
        return database.getCreatePalletUpdateDao()
    }

    @Provides
    @Singleton
    fun getDocumentDao(database: AppDatabase): DocumentDao {
        return database.getDocumentDao()
    }


}