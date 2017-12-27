package com.vatcore.trainticket

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author xzy
 */
class LoginAndRegisterActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_and_register)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_login_and_register, LoginAndRegisterFragment.newInstance())
                .commit()

    }



}