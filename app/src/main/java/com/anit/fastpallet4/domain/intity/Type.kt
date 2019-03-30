package com.anit.fastpallet4.domain.intity

enum class Type(val id: Int) {
    CREATE_PALLET(1),
    INVENTORY_PALLET(2);

    companion object {
        fun getTypeById(id: Int): Type? {
            return when (id) {
                1 -> CREATE_PALLET
                2 -> INVENTORY_PALLET
                else -> null
            }
        }
    }


}