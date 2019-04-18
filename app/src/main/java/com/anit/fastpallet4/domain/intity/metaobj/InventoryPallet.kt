package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type.*
import kotlinx.serialization.Serializable
import java.util.*


class InventoryPallet : MetaObj(type = INVENTORY_PALLET) {

    var barcodePallet: String? = null
    var numberPallet: String? = null
    var stringProduct: StringProduct =
        StringProduct()

    fun addBox(box: Box) {
        stringProduct.boxes.add(box)
    }

    fun dellBox(index: Int) {
        stringProduct.boxes.removeAt(index)
    }

    fun getBox(index: Int): Box {
        return stringProduct.boxes.get(index)
    }

    override fun save() {
        stringProduct.pallets
            .forEach {
                if (it.guid.isNullOrEmpty()) {
                    it.guid = UUID.randomUUID().toString()
                }
            }

        stringProduct.boxes
            .forEach {
                if (it.guid.isNullOrEmpty()) {
                    it.guid = UUID.randomUUID().toString()
                }
            }

        super.save()
    }
}


