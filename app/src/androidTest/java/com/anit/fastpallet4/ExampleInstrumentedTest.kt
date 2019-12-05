package com.anit.fastpallet4


import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.anit.fastpallet4.data.repositories.dbroom.AppDatabase
import com.anit.fastpallet4.data.repositories.dbroom.dao.CreatePalletUpdateDao
import com.anit.fastpallet4.data.repositories.dbroom.intity.BoxCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.CreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.PalletCreatePalletDb
import com.anit.fastpallet4.data.repositories.dbroom.intity.ProductCreatePalletDb
import org.junit.After
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private var db: AppDatabase? = null
    private var createPalletUpdateDao: CreatePalletUpdateDao? = null

    private val docTest = CreatePalletDb(
        guid = "0",
        dateChanged = null,
        barcode = null,
        date = null,
        description = "0",
        status = 1,
        number = "1",
        guidServer = "0",
        isLastLoad = false
    )


    private fun getListProduct(guidDoc: String): List<ProductCreatePalletDb> {
        return (0..3).map {
            ProductCreatePalletDb(
                guidDoc = guidDoc,
                guid = guidDoc + "_" + it,
                countRow = null,
                nameProduct = guidDoc + "_" + it,
                count = null,
                countBox = null,
                dateChanged = null,
                barcode = null,
                number = guidDoc + "_" + it,
                isLastLoad = false,
                weightEndProduct = null,
                weightStartProduct = null,
                weightCoffProduct = null,
                countPallet = null,
                weightBarcode = null,
                edCoff = null,
                ed = null,
                codeProduct = null,
                countBack = null,
                countBoxBack = null,
                guidProductBack = guidDoc + "_" + it
            )
        }
    }

    private fun getListPallet(guidProduct: String): List<PalletCreatePalletDb> {
        return (0..3).map {
            PalletCreatePalletDb(
                guid = guidProduct + "_" + it,
                guidProduct = guidProduct,
                number = guidProduct + "_" + it,
                barcode = null,
                dateChanged = null,
                countBox = null,
                count = null,
                nameProduct = guidProduct + "_" + it,
                sclad = null,
                countRow = null,
                state = null
            )
        }
    }

    private fun getListBox(guidPallet: String): List<BoxCreatePalletDb> {
        return (0..10).map {
            BoxCreatePalletDb(
                guid = guidPallet + "_" + it,
                barcode = null,
                dateChanged = null,
                countBox = 2,
                count = 10f,
                guidPallet = guidPallet
            )
        }
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db!!.close()
    }


    @Before
    @Throws(Exception::class)
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java
        ).addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

//                    getListTriggerCreatePallet().forEach {
//                        db.execSQL(it)
//                    }
                }
            }

        )
            .build()
        createPalletUpdateDao = db!!.getCreatePalletUpdateDao()
    }

    private fun testDoc(doc: CreatePalletDb, listProduct: List<ProductCreatePalletDb>) {
        val docDb = createPalletUpdateDao!!.getDocByGuid(doc.guid)

        if (docDb != doc) {
            assertFalse("Документы не равны!", true)
        }

        val listProductDb = createPalletUpdateDao!!.getProductListByGuidDoc(doc.guid)

        if (listProductDb.size != listProduct.size) {
            assertFalse("Документ Список продуктов не равены!", true)
        }
    }

    private fun testPallet(pallet: PalletCreatePalletDb, listBox: List<BoxCreatePalletDb>) {
        val palletDb = createPalletUpdateDao!!.getPalletByGuid(pallet.guid)

        if (palletDb.guid != pallet.guid) {
            assertFalse("Паллеты не равны!", true)
        }

        val listBoxDb = createPalletUpdateDao!!.getListBoxByGuidPallet(pallet.guid)

        val count = listBox.mapNotNull { it.count }
            .fold(0f) { total, next -> total + next }

        val countBox = listBox.mapNotNull { it.countBox }
            .fold(0) { total, next -> total + next }


        val countDb = listBoxDb.mapNotNull { it.count }
            .fold(0f) { total, next -> total + next }

        val countBoxDb = listBoxDb.mapNotNull { it.countBox }
            .fold(0) { total, next -> total + next }

        if (count != countDb) {
            assertFalse("Паллета Количество в списке не равно!", true)
        }
        if (countBox != countBoxDb) {
            assertFalse("Паллета Места в списке не равно!", true)
        }

        //Тригер
        if (palletDb.count != countDb) {
            assertFalse("Паллета Количество не равно c паллетой!", true)
        }
        if (palletDb.countBox != countBoxDb) {
            assertFalse("Паллета Мест не равно c паллетой!", true)
        }
        if (palletDb.countRow != listBoxDb.size) {
            assertFalse("Паллета Строк не равно c паллетой!", true)
        }


        if (listBoxDb.size != listBox.size) {
            assertFalse("Паллета Списоки коробок не равены!", true)
        }

    }

    private fun testProduct(
        product: ProductCreatePalletDb,
        listPallet: List<PalletCreatePalletDb>
    ) {
        val productDb = createPalletUpdateDao!!.getProductByGuid(product.guid)

        if (productDb.guid != product.guid) {
            assertFalse("Продукты не равны!", true)
        }

        val listPalletDb = createPalletUpdateDao!!.getListPalletByGuidProduct(product.guid)


        val countDb = listPalletDb.mapNotNull { it.count }
            .fold(0f) { total, next -> total + next }

        val countBoxDb = listPalletDb.mapNotNull { it.countBox }
            .fold(0) { total, next -> total + next }

        val countRowDb = listPalletDb.mapNotNull { it.countRow }
            .fold(0) { total, next -> total + next }


        //Тригер
        if (productDb.count != countDb) {
            assertFalse("Продукт Количество не равно c паллетой!", true)
        }
        if (productDb.countBox != countBoxDb) {
            assertFalse("Продукт Мест не равно c паллетой!", true)
        }
        if (productDb.countRow != countRowDb) {
            assertFalse("Продукт Строк не равно c паллетой!", true)
        }

        if (productDb.countPallet != listPallet.size) {
            assertFalse("Продукт Строк не равно c паллетой!", true)
        }


        if (listPalletDb.size != listPallet.size) {
            assertFalse("Продукт Списоки коробок не равены!", true)
        }
    }

    private fun cretTestData() {
        createPalletUpdateDao!!.insertOrUpdate(docTest)
        getListProduct(docTest.guid).forEach { prod ->
            createPalletUpdateDao!!.insertOrUpdate(prod)
            getListPallet(prod.guid).forEach { pal ->
                createPalletUpdateDao!!.insertOrUpdate(pal)
                getListBox(pal.guid).forEach { box ->
                    createPalletUpdateDao!!.insertOrUpdate(box)
                }
            }
        }
    }

    @Test
    fun reCalcTest() {
        cretTestData()

        createPalletUpdateDao!!.recalcPallet()
        createPalletUpdateDao!!.reCalcProduct()

        testDoc(docTest, getListProduct(docTest.guid))
        getListProduct(docTest.guid).forEach { prod ->
            testProduct(prod, getListPallet(prod.guid))
            getListPallet(prod.guid).forEach { pall ->
                testPallet(pall, getListBox(pall.guid))
            }
        }

        createPalletUpdateDao!!.delete(docTest)

        assertNull("Нe удалился документ", createPalletUpdateDao!!.getDocByGuid(docTest.guid))
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.anit.myapplication", appContext.packageName)
    }
}
