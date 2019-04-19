package com.anit.fastpallet4.data.repositories.net.intity

import com.anit.fastpallet4.data.repositories.net.intity.common.Reqest
import com.anit.fastpallet4.data.repositories.net.intity.common.Response

class GetInfoPalletReqest(
    var codeTSD: String,
    var list: List<String>
): Reqest

class GetInfoPalletResponse(var list: List<Item>):Response

class Item(
    var PaletCode:String? = null,
    var PaletGuid:String? = null,
    var PaletTovar:String? = null,
    var PaletState:String? = null,
    var PaletStore:String? = null,
    var PaletWeight:String? = null,
    var PaletPlacesCount:String? = null,
    var nameProduct:String? = null,

    var barcode: String? = null,
    var weightStartProduct: String? = null,
    var weightEndProduct: String? = null,
    var weightCoffProduct:String? = null
)
