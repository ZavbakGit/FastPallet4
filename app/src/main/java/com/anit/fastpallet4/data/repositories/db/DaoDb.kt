package com.anit.fastpallet4.data.repositories.db

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.data.repositories.db.intity.DocumentRm
import com.anit.fastpallet4.data.repositories.db.intity.ItemListRm
import com.anit.fastpallet4.maping.Maping
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj

import io.reactivex.Flowable
import javax.inject.Inject

class DaoDb {

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

    fun getMetaObjByServerGuid(guidServer: String,typeFromServer:String): MetaObj? {
        var realm = realmInitLocal.getLocalInstance()
        var doc = realm.where(DocumentRm::class.java)
            .equalTo("typeFromServer", typeFromServer)
            .equalTo("guidServer", guidServer)
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


