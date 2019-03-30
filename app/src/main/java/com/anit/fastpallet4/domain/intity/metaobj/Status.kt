package com.anit.fastpallet4.domain.intity.metaobj


enum class Status(val id: Int, val fullName: String) {
    NEW(1, "Новый"), READY(2, "Подготовлен"),
    LOADED(3, "Выгружен"), UNLOADED(4, "Загружен");

    companion object {
        fun getStatusById(id: Int): Status? {
            return when (id) {
                1 -> NEW
                2 -> READY
                3 -> LOADED
                4 -> UNLOADED
                else -> null
            }
        }
    }
}