package com.anit.fastpallet4.data.repositories.dbrealm.intity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class DocumentRm(
    @Required
    @PrimaryKey
    var guid: String? = null,
    @Required
    var typeDoc:Int? = null,
    var typeFromServer:String? = null,
    var guidServer: String? = null,
    var data: String? = null
): RealmObject()