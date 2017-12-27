package com.vatcore.trainticket

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.AsyncTask
import com.vatcore.trainticket.util.HttpUtil
import com.vatcore.trainticket.util.JsonUtil

/**
 * @author xzy
 */
class StartPageActivity: AppCompatActivity() {

    lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_start_page)

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        UserFinal.PHONE = mSharedPreferences.getString("phone", "")
        UserFinal.TOKEN = mSharedPreferences.getString("token", "")

        if (UserFinal.TOKEN != "" && UserFinal.PHONE != "") VerifyTokenTask().execute()  // 服务器验证
        else {
            val intent = Intent(this@StartPageActivity, LoginAndRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private inner class VerifyTokenTask: AsyncTask<Void, Void, AjaxResult<Any>>() {

        private val url = SystemFinal.HOST + "/api/account/verifyToken"

        override fun doInBackground(vararg params: Void): AjaxResult<Any> {
            return AjaxResult.stringToAjaxResult<Any>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(""))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<Any>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    val intent = Intent(this@StartPageActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    mSharedPreferences.edit()
                        .remove("token")
                        .remove("phone")
                        .apply();
                    val intent = Intent(this@StartPageActivity, LoginAndRegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}