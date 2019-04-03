package com.anit.netreqest.intity.getdocuments

import java.util.*

data class GetDocumentsResponse(
    var codeTsd: String? = null,
    var listDocuments:List<DocumentRs>? = null
)

class DocumentRs {
    var guid: String? = null
    var type: String? = null
    var status: String? = null
    var number: String? = null
    var date: Date? = null
    var description: String? = null
    var listStringsProduct: List<StringProductRs>? = null
}

class StringProductRs(

    var guidProduct: String? = null,
    var nameProduct: String? = null,
    var number:String? = null,
    var codeProduct: String? = null,
    var ed: String? = null,

    var barcode: String? = null,
    var weightStartProduct: String? = null,
    var weightEndProduct: String? = null,
    var weightCoffProduct:String? = null,

    var edCoff: String? = null,
    var count: String? = null,
    var countBox: String? = null
)
