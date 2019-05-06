package com.anit.fastpallet4.app.di.aplication


import com.anit.fastpallet4.app.di.activity.ActivityComponent
import com.anit.fastpallet4.app.di.activity.ActivityModule
import com.anit.fastpallet4.app.di.modules.*
import com.anit.fastpallet4.data.repositories.db.DaoDb
import com.anit.fastpallet4.data.repositories.net.DaoNet
import com.anit.fastpallet4.data.repositories.net.ManagerNet
import com.anit.fastpallet4.data.repositories.preferense.DaoPref
import com.anit.fastpallet4.maping.Maping
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetFlowableListMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseGetMetaObjByGuidServer
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseMetaObj
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorGetInfoPallet
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorGetListDocFromServer
import com.anit.fastpallet4.domain.usecase.interactor.exchange.InteractorSendCreatePalletToServer
import com.anit.fastpallet4.presentaition.navigation.LocalNavigationModule
import com.anit.fastpallet4.presentaition.navigation.NavigationModule
import com.anit.fastpallet4.presentaition.navigation.Screens
import com.anit.fastpallet4.presentaition.presenter.ListDocPresenter
import com.anit.fastpallet4.presentaition.ui.base.BaseActivity
import com.anit.fastpallet4.presentaition.presenter.MainPresenter
import com.anit.fastpallet4.presentaition.presenter.Model
import com.anit.fastpallet4.presentaition.presenter.action.doc.ActionPalletPresenter
import com.anit.fastpallet4.presentaition.presenter.action.product.ProductActionPalletPresenter
import com.anit.fastpallet4.presentaition.presenter.createpallet.doc.CreatePalletPresenter
import com.anit.fastpallet4.presentaition.presenter.createpallet.pallet.PalletCreatePalletPresenter
import com.anit.fastpallet4.presentaition.presenter.createpallet.product.ProductCreatePalletPresenter
import com.anit.fastpallet4.presentaition.ui.mainactivity.MainActivity
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
    fun inject(dao: DaoDb)
    fun inject(interactorUseCaseGetFlowableListMetaObj: InteractorUseCaseGetFlowableListMetaObj)
    fun inject(interactorUseCaseGetMetaObj: InteractorUseCaseGetMetaObj)
    fun inject(interactorUseCaseMetaObj: InteractorUseCaseMetaObj)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(model: Model)
    fun inject(listDocPresenter: ListDocPresenter)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.inventory.Model)
    fun inject(maping: Maping)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.createpallet.doc.Model)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.createpallet.product.Model)
    fun inject(createPalletPresenter: CreatePalletPresenter)
    fun inject(productCreatePalletPresenter: ProductCreatePalletPresenter)
    fun inject(palletCreatePalletPresenter: PalletCreatePalletPresenter)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.createpallet.pallet.Model)
    fun inject(interactorGetListDoc: InteractorGetListDocFromServer)
    fun inject(daoPref: DaoPref)
    fun inject(managerNet: ManagerNet)
    fun inject(screens: Screens)
    fun inject(daoNet: DaoNet)
    fun inject(interactorUseCaseGetMetaObjByGuidServer: InteractorUseCaseGetMetaObjByGuidServer)
    fun inject(interactorSendCreatePalletToServer: InteractorSendCreatePalletToServer)
    fun inject(interactorGetInfoPallet: InteractorGetInfoPallet)
    fun inject(actionPalletPresenter: ActionPalletPresenter)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.action.doc.Model)
    fun inject(productActionPalletPresenter: ProductActionPalletPresenter)
    fun inject(model: com.anit.fastpallet4.presentaition.presenter.action.product.Model)

}


