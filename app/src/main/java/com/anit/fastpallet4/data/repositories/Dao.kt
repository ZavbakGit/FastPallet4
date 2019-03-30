package com.anit.fastpallet4.data.repositories

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.maping.Maping
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.data.db.intity.DocumentRm
import com.anit.fastpallet4.data.db.intity.ItemListRm
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj

import io.reactivex.Flowable
import io.realm.Realm
import javax.inject.Inject

class Dao {

    @Inject
    lateinit var realmInitLocal: RealmInitLocal

    //var realm:Realm

    @Inject
    lateinit var maping: Maping

    init {
       App.appComponent.inject(this)

    }

    fun save(metaObject: MetaObj) {
        var realm = realmInitLocal.getLocalInstance()
        realm.executeTransaction {
            //Сохранили объект

            var docRm =  maping.map(metaObject)

            realm.copyToRealmOrUpdate(docRm)
            //Сохранили журнал
            realm.copyToRealmOrUpdate(maping.map(maping.mapToList(metaObject)))
        }
    }

    fun dell(metaObject: MetaObj) {
        var realm = realmInitLocal.getLocalInstance()
        realm.executeTransaction {

            realm.where(DocumentRm::class.java)
                .equalTo("guid", metaObject.guid)
                .findAll().deleteAllFromRealm()

            realm.where(ItemListRm::class.java)
                .equalTo("guid", metaObject.guid)
                .findAll().deleteAllFromRealm()


        }
    }

    fun getMetaObj(guid: String): MetaObj? {
        var realm = realmInitLocal.getLocalInstance()
        var doc = realm.where(DocumentRm::class.java)
            .equalTo("guid", guid)
            .findFirst()

        return if (doc == null) {
            null
        } else {
            maping.map(doc)
        }

    }

    fun getFlowableList(): Flowable<List<ItemListMetaObj?>> {
        var realm = realmInitLocal.getLocalInstance()
        return realm.where(ItemListRm::class.java)
            .findAll()
            .asFlowable()
            .map { it ->
                it.map { doc ->
                    maping.map(doc)
                }
            }
    }

}


