package com.vatcore.trainticket.util

import android.util.Log
import com.vatcore.trainticket.AjaxResult
import com.vatcore.trainticket.JsonParam
import com.vatcore.trainticket.SystemFinal
import okhttp3.OkHttpClient
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody


/**
 * @author xzy
 */
class HttpUtil {

    companion object {

        fun postJson(url: String, json: String): String {

            Log.i("url:", url)
            Log.i("json:", json)

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                    .build()
            val response = client.newCall(request).execute()

            return response.body()!!.string()
        }

    }

}