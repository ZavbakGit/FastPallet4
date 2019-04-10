package com.anit.fastpallet4.maping

import com.anit.fastpallet4.domain.intity.metaobj.Status

fun getStatusByString(str: String?): Status? {
    return when {
        str.equals("Новый", true) -> Status.NEW
        str.equals("Готов к выгрузке", true) -> Status.READY
        str.equals("Выгружен", true) -> Status.LOADED
        str.equals("Загружен", true) -> Status.UNLOADED
        else -> null
    }

}