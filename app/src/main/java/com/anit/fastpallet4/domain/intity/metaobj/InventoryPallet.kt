package com.anit.fastpallet4.domain.intity.metaobj

import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type.*
import kotlinx.serialization.Serializable
import java.util.*


class InventoryPallet : MetaObj(type = INVENTORY_PALLET) {


    var numberPallet: String? = null
    var stringProduct: StringProduct = StringProduct()
    var barcodePallet: String? = null

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


