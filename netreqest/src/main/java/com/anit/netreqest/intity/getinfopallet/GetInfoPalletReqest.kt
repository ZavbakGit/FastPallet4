package com.anit.netreqest.intity.getinfopallet

data class GetInfoPalletReqest (
    var codeTSD: String? = null,
    var list: List<String> = listOf()
)

data class GetInfoPalletResponse(
    var list: List<Item>? = null
)

data class Item(
    var PaletCode:String? = null,
    var PaletGuid:String? = null,
    var PaletTovar:String? = null,
    var PaletState:String? = null,
    var PaletStore:String? = null,
    var PaletWeight:String? = null,
    var PaletPlacesCount:String? = null
)


