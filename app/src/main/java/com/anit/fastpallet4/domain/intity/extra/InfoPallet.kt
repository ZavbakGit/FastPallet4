package com.anit.fastpallet4.domain.intity.extra

data class InfoPallet(
    var code:String? = null,
    var guid:String? = null,
    var nameProduct:String? = null,
    var state:String? = null,
    var sclad:String? = null,
    var count:Float = 0f,
    var countBox:Int = 0,
    var weightBarcode: String? = null,
    var weightStartProduct: Int = 0,
    var weightEndProduct: Int = 0,
    var weightCoffProduct: Float = 0f

)