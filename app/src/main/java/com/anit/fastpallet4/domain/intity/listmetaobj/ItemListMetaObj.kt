package com.anit.fastpallet4.domain.intity.listmetaobj

import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.metaobj.Status
import java.util.*

class ItemListMetaObj(
    private var guid: String? = null,
    private var guidServer: String? = null,
    val type: Type,
    var typeFromServer:String? = null,
    var status: Status,
    var number: String? = null,
    var date: Date? = null,
    var dataChanged: Date? = null,
    var isWasLoadedLastTime: Boolean? = false,
    var description: String? = null,
    var barcode: String? = null

){

    fun getGuid() = guid
    fun getGuidServer() = guidServer



}