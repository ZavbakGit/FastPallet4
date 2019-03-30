package com.anit.fastpallet4.model.metaobject

import com.anit.fastpallet4.domain.intity.Type.*
import com.anit.fastpallet4.domain.usecase.interactor.InteractorCreatorMetaObj
import org.junit.Before
import org.junit.Test

class IManagerTest {

    val creator = InteractorCreatorMetaObj(CREATE_PALLET)


    @Test
    fun save() {
        var creatPallet = creator.create()
        creatPallet.save()
    }

    @Test
    fun dell() {
        var creatPallet = creator.create()
        creatPallet.dell()
    }

    @Before
    fun setUp() {

    }

    @Test
    fun save1() {
    }

    @Test
    fun dell1() {
    }

    @Test
    fun getMetaObject() {
    }
}