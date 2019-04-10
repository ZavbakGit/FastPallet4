package com.anit.fastpallet4.domain.usecase.interactor

import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.usecase.UseCreateMetaObj

class InteractorCreatorMetaObj(val type: Type) : UseCreateMetaObj {
    override fun create(): MetaObj {
        return when (type) {
            Type.CREATE_PALLET -> CreatePallet()
            Type.INVENTORY_PALLET -> InventoryPallet()

        }
    }
}