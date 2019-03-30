package com.anit.fastpallet4.domain.intity


import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.Status.*
import com.anit.fastpallet4.domain.usecase.interactor.InteractorUseCaseMetaObj
import com.anit.fastpallet4.domain.usecase.UseCaseMetaObj
import kotlinx.serialization.Transient
import java.util.*



abstract class MetaObj(){

    var guid: String? = null
    var guidServer: String? = null
    var type: Type? = null

    var status: Status = NEW
    var number: String? = null


    var date: Date? = null


    var dataChanged: Date? = null

    var isWasLoadedLastTime: Boolean? = false
    var description: String? = null
    var barcode: String? = null



    //private var useCaseMetaObj: UseCaseMetaObj? = null

    constructor(type: Type):this(){
        this.type = type
    }

    init {
        //useCaseMetaObj = InteractorUseCaseMetaObj(this)
    }

    open fun save() {
        var useCaseMetaObj = InteractorUseCaseMetaObj(this)
        if (isNew()) {
            guid = UUID.randomUUID().toString()
        }
        dataChanged = Date()
        useCaseMetaObj!!.save()
    }

    open fun dell() {
        var useCaseMetaObj: UseCaseMetaObj = InteractorUseCaseMetaObj(this)
        useCaseMetaObj!!.dell()
    }

    fun isNew(): Boolean {
        return guid.isNullOrEmpty()
    }
}














