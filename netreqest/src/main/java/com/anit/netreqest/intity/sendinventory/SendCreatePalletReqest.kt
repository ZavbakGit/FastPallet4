package com.anit.netreqest.intity.sendinventory

class SendDocModelResponse {
    var listConfirm: List<ItemConfimResponse> = listOf()
}

class ItemConfimResponse{
    var guid: String? = null
    var status: String? = null
    var number:String? = null
}