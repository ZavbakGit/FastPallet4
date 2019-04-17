package com.anit.fastpallet4.maping

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.db.intity.DocumentRm
import com.anit.fastpallet4.data.repositories.db.intity.ItemListRm
import com.anit.fastpallet4.data.repositories.net.intity.DocResponse
import com.anit.fastpallet4.domain.intity.MetaObj
import com.anit.fastpallet4.domain.intity.Type
import com.anit.fastpallet4.domain.intity.Type.CREATE_PALLET
import com.anit.fastpallet4.domain.intity.Type.INVENTORY_PALLET
import com.anit.fastpallet4.domain.intity.listmetaobj.ItemListMetaObj
import com.anit.fastpallet4.domain.intity.metaobj.CreatePallet
import com.anit.fastpallet4.domain.intity.metaobj.InventoryPallet
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.intity.metaobj.StringProduct
import com.anit.fastpallet4.domain.utils.getDecimalStr
import com.google.gson.Gson
import javax.inject.Inject

class Maping {

    @Inject
    lateinit var gson: Gson

    init {
        App.appComponent.inject(this)
    }


    fun map(doc: MetaObj): DocumentRm {
        var data = gson.toJson(doc)

        return DocumentRm(
            guid = doc.guid,
            guidServer = doc.guidServer,
            typeDoc = doc.type!!.id,
            data = data
        )

    }

    fun map(doc: DocumentRm): MetaObj? {
        return when (Type.getTypeById(doc.typeDoc!!)) {
            CREATE_PALLET -> mapCreatePallet(doc)
            INVENTORY_PALLET -> mapCreateInventory(doc)
            else -> null
        }
    }

    fun map(item: ItemListMetaObj): ItemListRm {
        return ItemListRm(
            guid = item.getGuid(),
            guidServer = item.getGuidServer(),
            type = item.type.id,
            date = item.date,
            status = item.status.id,
            barcode = item.barcode,
            dataChanged = item.dataChanged,
            description = item.description,
            isWasLoadedLastTime = item.isWasLoadedLastTime,
            number = item.number
        )
    }

    fun map(item: ItemListRm): ItemListMetaObj {
        return ItemListMetaObj(
            guid = item.guid,
            guidServer = item.guidServer,
            type = Type.getTypeById(item.type!!)!!,

            date = item.date,
            status = Status.getStatusById(item.status)!!,
            barcode = item.barcode,
            dataChanged = item.dataChanged,
            description = item.description,
            isWasLoadedLastTime = item.isWasLoadedLastTime,
            number = item.number
        )

    }

    fun mapToList(doc: MetaObj): ItemListMetaObj {
        return ItemListMetaObj(
            guid = doc.guid,
            guidServer = doc.guidServer,
            type = doc.type!!,
            date = doc.date,
            status = doc.status,
            barcode = doc.barcode,
            dataChanged = doc.dataChanged,
            description = doc.description,
            isWasLoadedLastTime = doc.isWasLoadedLastTime,
            number = doc.number
        )
    }

    private fun mapCreatePallet(doc: DocumentRm): CreatePallet {
        //return Json.parse(CreatePallet.serializer(), doc.data ?: "")
        return gson.fromJson(doc.data, CreatePallet::class.java)

    }

    private fun mapCreateInventory(doc: DocumentRm): InventoryPallet {
        //return Json.parse(InventoryPallet.serializer(), doc.data ?: "")
        return gson.fromJson(doc.data, InventoryPallet::class.java)
    }

    fun map(docResponse: DocResponse): MetaObj? {
        return when {
            docResponse.type.equals("ФормированиеПалет", true) -> {

                if (docResponse.listStringsProduct?.map {
                        it.guidProduct
                    }?.distinct()?.size != docResponse.listStringsProduct?.size) {

                    throw Throwable("Дубли Номенклатуры! ${docResponse.description}")

                }

                var doc = CreatePallet()
                doc.guidServer = docResponse.guid
                doc.status = getStatusByString(docResponse.status)!!
                doc.number = docResponse.number
                doc.date = docResponse.date
                doc.description = docResponse.description

                var list = docResponse.listStringsProduct?.map {
                    var strProd = StringProduct()


                    strProd.guidProduct = it.guidProduct
                    strProd.nameProduct = it.nameProduct
                    strProd.codeProduct = it.codeProduct
                    strProd.ed = it.ed





                    strProd.weightStartProduct = getDecimalStr(it.weightStartProduct).toIntOrNull() ?: 0
                    strProd.weightEndProduct = getDecimalStr(it.weightEndProduct).toIntOrNull() ?: 0
                    strProd.weightCoffProduct = getDecimalStr(it.weightCoffProduct).toFloatOrNull() ?: 0f

                    strProd.edCoff = getDecimalStr(it.edCoff).toFloatOrNull() ?: 0f
                    strProd.count = getDecimalStr(it.count).toFloatOrNull() ?: 0f
                    strProd.countBox = getDecimalStr(it.countBox).toIntOrNull() ?: 0

                    strProd.isWasLoadedLastTime = true

                    return@map strProd

                }

                list?.let {
                    doc.stringProducts.addAll(it)
                }



                return doc

            }

            else -> null


        }


    }
}


