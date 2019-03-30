package com.anit.fastpallet4.domain.intity.metaobj

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Box {
    var barcode: String? = null
    var weight: Float = 0f
    @ContextualSerialization
    var data: Date? = null
}