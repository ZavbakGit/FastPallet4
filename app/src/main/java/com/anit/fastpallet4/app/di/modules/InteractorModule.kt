package com.anit.fastpallet4.app.di.modules



import com.anit.fastpallet4.domain.usecase.UseCaseGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetFlowableListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorGetListDocFromServer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule{

    @Singleton
    @Provides
    fun provideUseCaseGetListMetaObj(): UseCaseGetListMetaObj
            = InteractorUseCaseGetFlowableListMetaObj()

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


}