package com.anit.netreqest.intity.sendcreatepallet

class SendCreatePalletDocModelResponse {
    var listConfirm: List<ItemConfimResponse> = listOf()
}

class ItemConfimResponse{
    var guid: String? = null
    var status: String? = null
}