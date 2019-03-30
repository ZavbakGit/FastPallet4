package com.anit.fastpallet4.app.di.aplication


import com.anit.fastpallet4.app.di.activity.ActivityComponent
import com.anit.fastpallet4.app.di.activity.ActivityModule
import com.anit.fastpallet4.app.di.modules.MapingModule
import com.anit.fastpallet4.app.di.modules.RealmModule
import com.anit.fastpallet4.app.di.modules.RepositoryModule
import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetFlowableListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseMetaObj
import com.anit.fastpallet4.navigation.LocalNavigationModule
import com.anit.fastpallet4.navigation.NavigationModule
import com.anit.fastpallet4.presentaition.ui.base.BaseActivity
import com.anit.fastpallet4.presentaition.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = arrayOf(
        AppModule::class,
        NavigationModule::class,
        LocalNavigationModule::class,
        RealmModule::class,
        MapingModule::class,
        RepositoryModule::class
    )
)
@Singleton
interface AppComponent {
    fun createActivityComponent(activityModule: ActivityModule): ActivityComponent
    fun inject(dao: Dao)
    fun inject(interactorUseCaseGetFlowableListMetaObj: InteractorUseCaseGetFlowableListMetaObj)
    fun inject(interactorUseCaseGetMetaObj: InteractorUseCaseGetMetaObj)
    fun inject(interactorUseCaseMetaObj: InteractorUseCaseMetaObj)
    fun inject(baseActivity: BaseActivity)
    fun inject(mainPresenter: MainPresenter)

}


