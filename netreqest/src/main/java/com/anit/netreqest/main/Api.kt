package com.anit.netreqest.main


import com.gladkikh.netreqest.entity.model.ResponseModel
import com.gladkikh.netreqest.entity.model.ReqestModel
import io.reactivex.Flowable
import retrofit2.http.*

interface Api {

    @POST("host")
    fun getDataFromServer(
        @Header("Authorization") auth: String,
        @Body sendParamJson: ReqestModel
    ): Flowable<ResponseModel>

}