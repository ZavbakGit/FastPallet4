package com.anit.fastpallet4.app.di.modules



import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import com.anit.fastpallet4.domain.usecase.UseCaseAddTestData
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import com.anit.fastpallet4.domain.usecase.usecasenew.InteractorAddTestDataDb
import com.anit.fastpallet4.domain.usecase.usecasenew.InteractorUseCaseGetDocumentDb
import com.anit.fastpallet4.domain.usecase.usecasenew.InteractorUseCaseGetFlowableListDocumentDb
import com.anit.fastpallet4.domain.usecase.usecasenew.UseCaseGetDocument
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModuleDb{


    @Singleton
    @Provides
    fun provideUseCaseGetListMetaObj(dao: DocumentDao): UseCaseGetListMetaObj
            = InteractorUseCaseGetFlowableListDocumentDb(dao)

    @Singleton
    @Provides
    fun provideUseCaseGetDocument(dao: DocumentDao): UseCaseGetDocument
            = InteractorUseCaseGetDocumentDb(dao)



    @Singleton
    @Provides
    fun provideUseCaseAddTestData(dao: CreatePalletUpdateDao): UseCaseAddTestData
            = InteractorAddTestDataDb(dao)

}