package com.anit.fastpallet4.app.di.modules



import com.anit.fastpallet4.data.repositories.maping.Maping
import com.anit.fastpallet4.domain.usecase.UseCaseGetListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetFlowableListMetaObj
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule{

    @Singleton
    @Provides
    fun provideUseCaseGetListMetaObj(): UseCaseGetListMetaObj = InteractorUseCaseGetFlowableListMetaObj()


}