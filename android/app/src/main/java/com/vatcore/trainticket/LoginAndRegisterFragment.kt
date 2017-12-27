package com.vatcore.trainticket

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import com.vatcore.trainticket.util.HttpUtil
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.vatcore.trainticket.util.JsonUtil


/**
 * @author xzy
 */
class LoginAndRegisterFragment: Fragment() {

    companion object {
        var sLoginAndRegisterFragment: LoginAndRegisterFragment? = null

        @Synchronized
        fun newInstance(): LoginAndRegisterFragment {
            if (sLoginAndRegisterFragment == null) {
                sLoginAndRegisterFragment = LoginAndRegisterFragment()
            }
            return sLoginAndRegisterFragment as LoginAndRegisterFragment
        }
    }

    lateinit var mPhoneEditText: EditText
    lateinit var mPasswordEditText: EditText
    lateinit var mMessageTextView: TextView
    lateinit var mRegisterButton: Button
    lateinit var mLoginButton: Button

    var mCanRegister = true
    var mCanLogin = true

    lateinit var mEditor: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_login_and_register, container, false)

//        mEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

        mPhoneEditText = v!!.findViewById<EditText>(R.id.fragment_login_and_register_phone_edit_text)
        mPasswordEditText = v.findViewById<EditText>(R.id.fragment_login_and_register_password_edit_text)
        mMessageTextView = v.findViewById<TextView>(R.id.fragment_login_and_register_message_text_view)
        mRegisterButton = v.findViewById<Button>(R.id.fragment_login_and_register_register_button)
        mLoginButton = v.findViewById<Button>(R.id.fragment_login_and_register_login_button)

        mRegisterButton.setOnClickListener({
            if (mCanRegister) {
                RegisterTask().execute()
                mCanRegister = false
            }
            else mMessageTextView.setText("请求太频繁")
        })

        mLoginButton.setOnClickListener({
            if (mCanLogin) {
                LoginTask().execute()
                mCanLogin = false
            }
            else mMessageTextView.setText("请求太频繁")
        })


        return v
    }

    private inner class LoginTask: AsyncTask<Void, Void, AjaxResult<TokenJson>>() {

        private val url = SystemFinal.HOST + "/api/account/login"

        val phone = mPhoneEditText.text.toString()
        val password = mPasswordEditText.text.toString()

        override fun doInBackground(vararg params: Void): AjaxResult<TokenJson> {

            val ajaxResult = AjaxResult.stringToAjaxResult<TokenJson>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(mapOf(
//                "phone" to "55345678911",
//                "password" to "qwert0"
                    "phone" to phone,
                    "password" to password
            )))))

            Log.i("LoginTask.data.token:", ajaxResult.data.token)

            return ajaxResult
        }

        override fun onPostExecute(ajaxResult: AjaxResult<TokenJson>) {
            mCanLogin = true
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    UserFinal.TOKEN = ajaxResult.data.token
                    UserFinal.PHONE = phone
                    PreferenceManager.getDefaultSharedPreferences(activity).edit()
                        .putString("token", ajaxResult.data.token)
                        .putString("phone", phone)
                        .apply();

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity.finish()
                }
                else -> mMessageTextView.text = "登陆失败"
            }
        }
    }

    private inner class RegisterTask: AsyncTask<Void, Void, String>() {

        private val url = SystemFinal.HOST + "/api/account/register"

        val phone = mPhoneEditText.text.toString()
        val password = mPasswordEditText.text.toString()

        override fun doInBackground(vararg params: Void): String {

            val ajaxResult = AjaxResult.stringToAjaxResult<Any>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(mapOf(
                "phone" to phone,
                "password" to password
            )))))

            return ajaxResult.code
        }

        override fun onPostExecute(success: String) {
            mCanRegister = true
            mMessageTextView.text  = when (success) {
                AjaxResult.CODE_SUCCESS -> "注册成功"
                AjaxResult.CODE_IllegalArgument -> "非法参数"
                else -> "注册失败"
            }
        }
    }

}