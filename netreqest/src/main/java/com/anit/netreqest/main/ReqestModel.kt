package com.gladkikh.netreqest.entity.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by user on 28.06.2017.
 */

class ReqestModel(command:String, strData:String) {

    @SerializedName("command")
    @Expose
    var command: String = command
    @SerializedName("str_data_in")
    @Expose
    var strDataIn: String? = strData

    override fun toString(): String {
        return "SendModelDataService{" +
                "command='" + command + '\''.toString() +
                ", strDataIn='" + strDataIn + '\''.toString() +
                '}'.toString()
    }
}
