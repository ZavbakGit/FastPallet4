package com.anit.fastpallet4

import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import com.google.gson.FieldAttributes
import com.google.gson.ExclusionStrategy


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {
        var listDb = listOf("Вася","Петя")
        var listServ = listOf("Вася","Вася","Саша")

       listServ.distinct()



        listServ.filter { it !in listDb }




        assertEquals(4, 2 + 2)
    }
}
