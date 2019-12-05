package com.anit.fastpallet4.app.di.modules



import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.dao.DocumentDao
import com.anit.fastpallet4.domain.usecase.*
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorGetInfoPallet
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorSendCreatePalletToServer
import com.anit.fastpallet4.domain.usecase.usecasenew.InteractorAddTestDataDb
import com.anit.fastpallet4.domain.usecase.usecasenew.InteractorUseCaseGetFlowableListDocumentDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule{

//    @Singleton
//    @Provides
//    fun provideUseCaseGetListMetaObj(): UseCaseGetListMetaObj
//            = InteractorUseCaseGetFlowableListMetaObj()


    @Singleton
    @Provides
    fun provideUseCaseGetListMetaObj(dao: DocumentDao): UseCaseGetListMetaObj
            = InteractorUseCaseGetFlowableListDocumentDb(dao)


    @Singleton
    @Provides
    fun provideUseCaseGetMetaObj(): UseCaseGetMetaObj
            = InteractorUseCaseGetMetaObj()


    @Singleton
    @Provides
    fun provideInteractorGetListDocFromServer(): UseCaseGetListDocFromServer
            = InteractorGetListDocFromServer()


    @Singleton
    @Provides
    fun provideInteractorUseCaseGetMetaObjByGuidServer(): UseCaseGetMetaObjByGuidServer
            = InteractorUseCaseGetMetaObjByGuidServer()

    @Singleton
    @Provides
    fun provideUseCaseSendCreatePalletToServer(): UseCaseSendCreatePalletToServer
            = InteractorSendCreatePalletToServer()


    @Singleton
    @Provides
    fun provideUseCaseGetInfoPallet(): UseCaseGetInfoPallet
            = InteractorGetInfoPallet()


    @Singleton
    @Provides
    fun provideUseCaseAddTestData(dao: CreatePalletUpdateDao): UseCaseAddTestData
            = InteractorAddTestDataDb(dao)



}