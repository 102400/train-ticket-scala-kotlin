package com.vatcore.trainticket.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.vatcore.trainticket.AjaxResult
import java.io.InputStream

/**
 * @author xzy
 */
class JsonUtil {

    companion object {

        val mapperJson = ObjectMapper().registerModule(KotlinModule())

        fun <T> anyToString(data: T): String {
            return mapperJson.writeValueAsString(data)
        }

        inline fun <reified T : Any> stringToAny(data: String): T {
//            try {
            var result = mapperJson.readValue<T>(data)

//            }
//            catch (npe: NullPointerException) {
//                result = AjaxResult.newError(null as T);
//            }

            return result as T
        }

    }

}