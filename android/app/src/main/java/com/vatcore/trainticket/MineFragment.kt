package com.vatcore.trainticket

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * @author xzy
 */
class MineFragment: Fragment() {

    companion object {
        var sMineFragment: MineFragment? = null

        @Synchronized
        fun newInstance(): MineFragment {
            if (sMineFragment == null) {
                sMineFragment = MineFragment()
            }
            return sMineFragment as MineFragment
        }
    }

    private lateinit var mLogoutButton: Button
    private lateinit var mPhoneTextView: TextView
    private lateinit var mTokenTextView: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_mine, container, false)

        mPhoneTextView = v!!.findViewById(R.id.fragment_mine_phone_text_view)
        mTokenTextView = v.findViewById(R.id.fragment_mine_token_text_view)

        mPhoneTextView.text = "phone:" + UserFinal.PHONE
        mTokenTextView.text = "token:" + UserFinal.TOKEN

        mLogoutButton = v.findViewById<Button>(R.id.fragment_mine_logout_button)
        mLogoutButton.setOnClickListener({
            UserFinal.init()
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                    .remove("phone")
                    .remove("token")
                    .apply()

            val intent = Intent(activity, LoginAndRegisterActivity::class.java)
            startActivity(intent)
            activity.finish()
        })

        return v
    }

}