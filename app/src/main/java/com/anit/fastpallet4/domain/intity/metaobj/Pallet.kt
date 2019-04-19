package com.anit.fastpallet4.domain.intity.metaobj

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.util.*


class Pallet {
    var number: String? = null
    var barcode: String? = null
    var guid:String? = null

    var dataChanged: Date? = null
    var count: Float = 0f
    var countBox: Int = 0

    val boxes: MutableList<Box> = mutableListOf()

    fun addBox(box: Box) {
        boxes.add(box)
    }

    fun dellBoxByGuid(guid: String) {
        boxes.removeAll { it.guid.equals(guid,true) }
    }

    fun getBoxByGuid(guid:String):Box?{
      return  boxes.find { it.guid.equals(guid,true)}
    }
}