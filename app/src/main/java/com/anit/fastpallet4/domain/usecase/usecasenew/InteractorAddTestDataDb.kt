package com.anit.fastpallet4.domain.usecase.usecasenew

import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.BoxCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.PalletCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.ProductCreatePalletDb
import com.anit.fastpallet4.domain.intity.metaobj.Status
import com.anit.fastpallet4.domain.usecase.UseCaseAddTestData
import java.util.*

class InteractorAddTestDataDb(private val dao: CreatePalletUpdateDao) : UseCaseAddTestData {

    override fun add() {

        var countBox = 0

        val listDocuments =
            (0..5).map {
                CreatePalletDb(
                    guid = it.toString(),
                    status = Status.LOADED.id,
                    number = it.toString(),
                    guidServer = it.toString(),
                    isLastLoad = true,
                    description = "Формирование паллет №${it} от ${Date()}",
                    date = Date().time,
                    dateChanged = Date().time,
                    barcode = it.toString()
                )
            }



        listDocuments.forEach { doc ->
            dao.insertOrUpdate(doc)
            val listProduct = getListProduct(doc.guid)

            listProduct.forEach { prod ->
                dao.insertOrUpdate(prod)

                val listPallet = getListPallets(prod.guid)

                listPallet.forEach { pall ->
                    dao.insertOrUpdate(pall)
                    val listBox = getListBox(pall.guid)
                    listBox.forEach {
                        dao.insertOrUpdate(it)
                        println(it.guid)
                    }
                }
            }
        }

        println(countBox)
    }

    private fun getListProduct(guidDoc: String): List<ProductCreatePalletDb> {
        return (0..3).map {
            ProductCreatePalletDb(
                guid = guidDoc + "_" + it,
                guidDoc = guidDoc,
                nameProduct = "Продукт $it",
                dateChanged = Date().time,
                barcode = "3131116165165165",
                number = "1",
                isLastLoad = true,
                countBox = 10,
                count = 150f,
                codeProduct = "А00" + it,
                ed = "кг.",
                guidProductBack = "jhgkjhkj6455465",
                edCoff = 1f,
                weightBarcode = "12345515454",
                countPallet = 5,
                weightCoffProduct = 0.1f,
                weightStartProduct = 1,
                weightEndProduct = 4,
                countRow = null,
                countBoxBack = 50,
                countBack = 562.568f
            )
        }
    }

    private fun getListPallets(guidProduct: String): List<PalletCreatePalletDb> {
        return (0..3).map {
            PalletCreatePalletDb(
                guid = guidProduct + "_" + it,
                guidProduct = guidProduct,
                number = guidProduct + "_" + it,
                barcode = "65465546546548",
                sclad = "Основной",
                dateChanged = Date().time,
                count = null,
                countBox = null,
                state = null,
                nameProduct = "Продукт " + guidProduct + "_" + it,
                countRow = null
            )
        }
    }

    private fun getListBox(guidPallet: String): List<BoxCreatePalletDb> {
        return (0..3).map {
            BoxCreatePalletDb(
                guid = guidPallet + "_" + it,
                guidPallet = guidPallet,
                barcode = "654656516516516516",
                countBox = 1,
                dateChanged = Date().time,
                count = 25.50f
            )
        }
    }
}