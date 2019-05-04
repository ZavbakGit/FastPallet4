package com.anit.fastpallet4.data.repositories.net.intity

import com.anit.fastpallet4.data.repositories.net.intity.common.Reqest
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.anit.fastpallet4.domain.intity.MetaObj
import com.google.gson.annotations.SerializedName


class SendDocumentsReqest(@SerializedName("codeTSD") val codeTSD: String, val list:List<MetaObj>):Reqest

class SendCreatePalletDocModelResponse:Response {
    var listConfirm: List<ItemConfimResponse> = listOf()
}

class ItemConfimResponse{
    var guid: String? = null
    var type:String? = null
    var status: String? = null
}