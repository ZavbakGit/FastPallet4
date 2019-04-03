package com.gladkikh.netreqest.entity.model.taskfromserver
import com.google.gson.annotations.SerializedName

class ConfirmedTaskModelResponse {
    @SerializedName("list_confirm")
    var listConfirm: List<ItemConfim> = listOf()
}

class ItemConfim{
    @SerializedName("guid")
    var guid: String? = null
    @SerializedName("status")
    var status: String? = null
}