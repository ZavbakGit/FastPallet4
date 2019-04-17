package com.anit.fastpallet4.data.repositories.net.intity


import com.anit.fastpallet4.data.repositories.net.intity.common.Reqest
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.anit.fastpallet4.data.repositories.net.intity.common.StringProductResponse
import com.google.gson.annotations.SerializedName
import java.util.*

class GetListDocsRequest(codeTSD: String): Reqest {
    var date: Date = Date()
    @SerializedName("code_tsd")
    var codeTSD: String = codeTSD
}



class ListDocResponse: Response{
    var codeTsd: String? = null
    var listDocuments:List<DocResponse>? = null
}



class DocResponse {
    var guid: String? = null
    var type: String? = null
    var status: String? = null
    var number: String? = null
    var date: Date? = null
    var description: String? = null
    var listStringsProduct: List<StringProductResponse>? = null
}



