package com.anit.fastpallet4.data.repositories.net

import com.anit.fastpallet4.app.App
import com.anit.fastpallet4.data.repositories.net.intity.common.Reqest
import com.anit.fastpallet4.data.repositories.net.intity.common.Response
import com.anit.netreqest.NetHelper
import com.gladkikh.netreqest.entity.model.ReqestModel
import com.gladkikh.netreqest.entity.model.ResponseModel
import com.google.gson.Gson
import io.reactivex.Flowable
import javax.inject.Inject

class ManagerNet {

    @Inject
    lateinit var netHelper: NetHelper

    @Inject
    lateinit var gson: Gson

    init {
        App.appComponent.inject(this)
    }


    fun <T: Response>reqest(command: String, objReqest: Reqest, classReqest: Class<T>): Flowable<Response> {

        var reqestModel = ReqestModel(
            command = command,
            strData = gson.toJson(objReqest)
        )

      return  netHelper.getFlowableResponse(reqestModel)
            .flatMap {
                if (!(it.success ?: true)) {
                    return@flatMap Flowable.error<Throwable>(Throwable(it.messError))
                } else {
                    return@flatMap Flowable.just(it)
                }
            }
            .map {
                gson.fromJson((it as ResponseModel).response ?: "", classReqest)
            }
    }

}