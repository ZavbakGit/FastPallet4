package com.gladkikh.netreqest.entity.model.taskfromserver

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ConfirmedGetCreatePalletDocModelSend(codeTSD:String,listGuid:List<DocConfirm>){
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





