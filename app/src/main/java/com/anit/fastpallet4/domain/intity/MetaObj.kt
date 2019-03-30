package com.anit.fastpallet4.domain.intity


import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.Status.*
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseMetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseMetaObj
import java.util.*



abstract class MetaObj(){

    private var guid: String? = null
    private var guidServer: String? = null
    var type: Type? = null


    var status: Status = NEW
    var number: String? = null
    var date: Date? = null
    var dataChanged: Date? = null
    var isWasLoadedLastTime: Boolean? = false
    var description: String? = null
    var barcode: String? = null

    private val manager: UseCaseMetaObj

    constructor(type: Type):this(){
        this.type = type
    }

    init {
        manager = InteractorUseCaseMetaObj(this)
    }

    fun getGuid() = guid
    fun getGuidServer() = guidServer

    open fun save() {
        if (isNew()) {
            guid = UUID.randomUUID().toString()
        }
        dataChanged = Date()
        manager.save()
    }

    open fun dell() {
        manager.dell()
    }

    fun isNew(): Boolean {
        return guid.isNullOrEmpty()
    }
}














