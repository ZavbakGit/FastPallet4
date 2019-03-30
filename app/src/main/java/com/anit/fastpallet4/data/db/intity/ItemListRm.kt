package com.anit.fastpallet4.data.db.intity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class ItemListRm(

    @Required
    @PrimaryKey
    var guid: String? = null,
    @Required
    var type: Int? = null,
    var guidServer: String? = null,


    var status: Int = 0,
    var number: String? = null,
    var date: Date? = null,
    var dataChanged: Date? = null,
    var isWasLoadedLastTime: Boolean? = false,
    var description: String? = null,
    var barcode: String? = null

) : RealmObject()