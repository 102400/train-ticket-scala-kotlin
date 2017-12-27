package com.vatcore.trainticket

/**
 * @author xzy
 */
class SystemFinal {

    companion object {
        val HOST = "http://192.168.0.104:9000"
//        val JSON_PARAM: JsonParam<Any>? = JsonParam()
    }

}

class UserFinal {

//    class User(
//        val phone: String,
//        val token: String
//    )

    companion object {
        var PHONE = ""
        var TOKEN = ""

        fun init() {
            PHONE = ""
            TOKEN = ""
        }
    }

}