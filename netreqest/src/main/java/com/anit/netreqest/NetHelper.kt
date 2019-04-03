package com.anit.netreqest

import com.anit.netreqest.main.Api
import com.anit.netreqest.main.AutoritationManager
import com.gladkikh.netreqest.entity.model.ResponseModel
import com.gladkikh.netreqest.entity.model.ReqestModel
import com.google.gson.Gson
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class NetHelper(private val netSettings: NetSettings, private val gson: Gson) {


    fun getFlowableResponse(reqestModel: ReqestModel): Flowable<ResponseModel> {

        var baseUrl = netSettings.getHost() ?: "http://172.31.255.139:45250/RMMT/hs/api/"
        //var url = netSettings.getHost() ?: "http://172.31.255.150/RMMT/hs/api/"
        //var url = netSettings.getHost() ?: "http://5.8.207.81:45250/RMMT/hs/api/"


        return Flowable.just(baseUrl)
            .map {
                provideRestAdapter(it)
            }
            .map {
                it.create(Api::class.java)
            }
            .flatMap {
                var auth = AutoritationManager
                    .getStringAutorization(
                        netSettings.getLogin() ?: "Администратор",
                        netSettings.getPass() ?: ""
                    )

                return@flatMap it.getDataFromServer(auth = auth, sendParamJson = reqestModel)
            }
    }


    private fun provideRestAdapter(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl) //Базовая часть адреса
            .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер, необходимый для преобразования JSON'а в объекты
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
