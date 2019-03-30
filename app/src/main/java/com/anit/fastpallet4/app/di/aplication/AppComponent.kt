package com.anit.fastpallet4.app.di.aplication


import com.anit.fastpallet4.app.di.activity.ActivityComponent
import com.anit.fastpallet4.app.di.activity.ActivityModule
import com.anit.fastpallet4.app.di.modules.*
import com.anit.fastpallet4.data.repositories.Dao
import com.anit.fastpallet4.data.repositories.maping.Maping
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetFlowableListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseMetaObj
import com.anit.fastpallet4.presentaition.navigation.LocalNavigationModule
import com.anit.fastpallet4.presentaition.navigation.NavigationModule
import com.anit.fastpallet4.presentaition.presenter.ListDocPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseActivity
import com.anit.fastpallet4.presentaition.presenter.MainPresenter
import com.anit.fastpallet4.presentaition.presenter.Model
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = arrayOf(
        AppModule::class,
        NavigationModule::class,
        LocalNavigationModule::class,
        RealmModule::class,
        MapingModule::class,
        RepositoryModule::class,
        InteractorModule::class,
        ExtraModul::class
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
    fun inject(model: Model)
    fun inject(listDocPresenter: ListDocPresenter)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.Inventory.Model)
    fun inject(maping: Maping)

}


