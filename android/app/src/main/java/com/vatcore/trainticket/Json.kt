package com.vatcore.trainticket

import android.util.Log
import com.vatcore.trainticket.util.JsonUtil
import java.util.*

/**
 * @author xzy
 */
class Json {

}

class JsonParam<T>(
    val clientType: String = "android",
    val phone: String = "",
    val token: String = "",
    val data: T
) {
//    val clientType: String = "android"
//    var phone: String = ""
//    var token: String = ""
//    var data: T = "" as T

    companion object {

        fun <T> build(phone: String, token: String, data: T): JsonParam<T> {
            return JsonParam(phone = phone, token = token, data = data)
        }

        fun <T> build(data: T): JsonParam<T> {
            return JsonParam(phone = UserFinal.PHONE, token = UserFinal.TOKEN, data = data)
        }

    }

}

class AjaxResult<T>(
    val code: String,
    val message: String,
    var data: T
) {

    companion object {

        val CODE_SUCCESS = "0"
        val CODE_ERROR = "-1"
        val CODE_IllegalArgument = "-2"
        val CODE_TokenError = "-3"

        fun <T> newError(data: T): AjaxResult<T> {
            return AjaxResult(code = "-1", message = "Error", data = data)
        }

        inline fun <reified T : Any> stringToAjaxResult(data: String): AjaxResult<T> {
            val ajaxResult = JsonUtil.stringToAny<AjaxResult<T>>(data)

            val data = JsonUtil.stringToAny<T>(JsonUtil.anyToString(ajaxResult.data))

            Log.i("HttpUtil.postJson code" , ajaxResult.code)
            Log.i("HttpUtil.postJson message" , ajaxResult.message)

            ajaxResult.data = data

            return ajaxResult
        }

    }

}



data class TokenJson(val token: String)

data class TrainNumberJson(
    val lineName: String,
    val trainNumber: String,
    val trainName: String,
    val startTime: Long,
    val distance: Double,
    val travelTime: Long,
    val endTime: Long,
    val price: Double
)