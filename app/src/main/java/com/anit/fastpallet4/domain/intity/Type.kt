package com.anit.fastpallet4.domain.intity

enum class Type(val id: Int, val fullName: String, val nameServer: String) {
    CREATE_PALLET(1, "Создание паллет", "ФормированиеПалет"),
    INVENTORY_PALLET(2, "Инвентаризация", "ИнвентаризацияПалет"),
    TRANSFER_PALLET(3, "Перемещение", "ПеремещениеТоваров"),
    SALE_PALLET(4, "Реализация", "РеализацияТоваровУслуг"),
    CANCELLATION_PALLET(5, "Списание", "СписаниеТоваров");

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