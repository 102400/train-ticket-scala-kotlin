package com.vatcore.trainticket

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button


/**
 * @author xzy
 */
class MainActivity: AppCompatActivity() {

    companion object {
        private val sFragmentList = listOf(
            SearchFragment.newInstance(),
            OrderListFragment.newInstance(),
            MineFragment.newInstance()
        )
    }

    private var mViewPager: ViewPager? = null
    private val indexButton = arrayOfNulls<Button>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewPager = findViewById<ViewPager>(R.id.activity_main_view_pager);

        val fragmentManager = supportFragmentManager
        mViewPager?.adapter = object : FragmentStatePagerAdapter(fragmentManager) {

            override fun getItem(position: Int): Fragment {
                return sFragmentList[position]
            }

            override fun getCount(): Int {
                return sFragmentList.size
            }

            override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
                indexButton[position]?.setBackgroundColor(Color.GREEN)
                (0 until indexButton.size)
                        .filter { it != position }
                        .forEach { indexButton[it]?.setBackgroundColor(Color.TRANSPARENT) }
            }

        }

        mViewPager?.currentItem = 0

        indexButton[0] = findViewById<Button>(R.id.activity_main_search_button)
        indexButton[1] = findViewById<Button>(R.id.activity_main_order_list_button)
        indexButton[2] = findViewById<Button>(R.id.activity_main_mine_button)

        val i = intArrayOf(0)
        while (i[0] < indexButton.size) {
            indexButton[i[0]]?.setOnClickListener(object : View.OnClickListener{
                private val a = i[0]
                override fun onClick(v: View) {
                    mViewPager?.currentItem = a
                }
            })
            i[0]++
        }
    }
}
