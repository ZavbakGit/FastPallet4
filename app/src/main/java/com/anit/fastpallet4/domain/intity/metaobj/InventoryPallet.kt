package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type.*
import kotlinx.serialization.Serializable


class InventoryPallet : MetaObj(type = INVENTORY_PALLET) {

    var barcodePallet: String? = null
    var numberPallet: String? = null
    var stringProduct: StringProduct =
        StringProduct()

    fun addBox(box: Box){
        stringProduct.boxes.add(box)
    }

    fun dellBox(index:Int){
        stringProduct.boxes.removeAt(index)
    }

}


