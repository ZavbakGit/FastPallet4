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


    enum class HHH{
        TT,YY
    }

    abstract class Foo(var name:String,val born: Date?,val h:HHH = HHH.TT ){

        //@Expose(serialize = false)
        var  gason:Gson = GsonBuilder().create()
    }

    class Doo(name:String,val age:Int):Foo(name,Date()){
        override fun toString(): String {
            return "Doo(age=$age)"
        }
    }


    @Test
    fun addition_isCorrect() {


//        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//            .setPrettyPrinting().create()

        val gson = GsonBuilder()
            .addSerializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return f.name.toLowerCase().contains("gason")
                }

                override fun shouldSkipClass(aClass: Class<*>): Boolean {
                    return false
                }
            })
            .create()


        //var t = Doo("jhghj",10)
        //var str = gson.toJson(t)

        //var f = gson.fromJson(str,Doo::class.java)
        //println(f)



         var doc = InventoryPallet()
        doc.guid = "111"
        var str = gson.toJson(doc)


//        var str = Json.stringify(InventoryPallet.serializer(), doc)
//        var doc1 = Json.parse(InventoryPallet.serializer(), str)
//
        println(doc)

        assertEquals(4, 2 + 2)
    }
}
