package com.anit.fastpallet4.data.repositories.net.intity

import com.anit.fastpallet4.data.repositories.net.intity.common.Reqest
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class ConfirmDocLoadRequest(codeTSD:String,listGuid:List<DocConfirm>): Reqest {
    @SerializedName("date")
    @Expose
    var date: Date = Date()

    @SerializedName("code_tsd")
    @Expose
    var codeTSD: String = codeTSD

    @SerializedName("list_doc")
    @Expose
    var listDoc: List<DocConfirm> = listGuid
}

data class DocConfirm(var guid:String,var type:String)



class ConfirmResponse:Response {
    @SerializedName("list_confirm")
    var listConfirm: List<ItemConfim> = listOf()
}

class ItemConfim{
    @SerializedName("guid")
    var guid: String? = null
    @SerializedName("status")
    var status: String? = null
}