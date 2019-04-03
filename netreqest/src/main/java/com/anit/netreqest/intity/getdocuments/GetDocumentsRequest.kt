package com.anit.netreqest.intity.getdocuments


import com.google.gson.annotations.SerializedName
import java.util.*

class GetDocumentsRequest(codeTSD: String) {
    var date: Date = Date()
    @SerializedName("code_tsd")
    var codeTSD: String = codeTSD
}