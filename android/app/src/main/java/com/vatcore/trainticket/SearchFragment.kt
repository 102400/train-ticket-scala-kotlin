package com.vatcore.trainticket

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.vatcore.trainticket.util.HttpUtil
import com.vatcore.trainticket.util.JsonUtil
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemSelectedListener
import java.time.LocalDate
import android.graphics.Movie
import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.vatcore.trainticket.SearchFragment.TrainNumberHolder
import java.util.logging.Logger
import android.content.DialogInterface
import android.R.string.cancel
import android.R.string.ok
import android.support.v7.app.AlertDialog
import com.vatcore.trainticket.util.DateUtil
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class SearchFragment: Fragment() {

    companion object {
        var sSearchFragment: SearchFragment? = null

        @Synchronized
        fun newInstance(): SearchFragment {
            if (sSearchFragment == null) {
                sSearchFragment = SearchFragment()
            }
            return sSearchFragment as SearchFragment
        }
    }

    private var mStartLocationSpinner: Spinner?= null
    private var mEndLocationSpinner: Spinner?= null
    private var mSearchButton: Button?= null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_search, container, false)

        mStartLocationSpinner = v!!.findViewById(R.id.fragment_search_start_location_spinner)
        mEndLocationSpinner = v.findViewById(R.id.fragment_search_end_location_spinner)
        mSearchButton = v.findViewById(R.id.fragment_search_search_button)
        mRecyclerView = v.findViewById(R.id.fragment_search_recycler_view)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity);

        mSearchButton!!.setOnClickListener({
            StartToEndTrainNumberListTask().execute()
        })

        mStartLocationSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                StartToLocationListTask().execute()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        LocationListTask().execute()

        return v
    }

    private inner class TrainNumberHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mLineNameTextView: TextView? = null
        private var mTrainNumberTextView: TextView?= null
        private var mTrainNameTextView: TextView?= null
        private var mDistanceTextView: TextView?= null
        private var mStartTimeTextView: TextView?= null
        private var mTravelTimeTextView: TextView?= null
        private var mEndTimeTextView: TextView?= null
        private var mPriceTextView: TextView?= null

        private var mTrainNumberList: List<TrainNumberJson>? = null
        private var mTrainNumber: TrainNumberJson? = null
        private var mPosition: Int = 0

        init {
            itemView.setOnClickListener(this)

            mLineNameTextView = itemView.findViewById(R.id.list_item_train_number_line_name_text_view)
            mTrainNumberTextView = itemView.findViewById(R.id.list_item_train_number_train_number_text_view)
            mTrainNameTextView = itemView.findViewById(R.id.list_item_train_number_train_name_text_view)
            mDistanceTextView = itemView.findViewById(R.id.list_item_train_number_distance_text_view)
            mStartTimeTextView = itemView.findViewById(R.id.list_item_train_number_start_time_text_view)
            mTravelTimeTextView = itemView.findViewById(R.id.list_item_train_number_travel_time_text_view)
            mEndTimeTextView = itemView.findViewById(R.id.list_item_train_number_end_time_text_view)
            mPriceTextView = itemView.findViewById(R.id.list_item_train_number_price_text_view)

        }

        fun bindTrainNumber(trainNumberList: List<TrainNumberJson>, position: Int) {
            mTrainNumberList = trainNumberList
            mPosition = position
            mTrainNumber = mTrainNumberList!![mPosition]

            mLineNameTextView?.text = mLineNameTextView?.text.toString() + mTrainNumber?.lineName
            mTrainNumberTextView?.text = mTrainNumberTextView?.text.toString() + mTrainNumber?.trainNumber
            mTrainNameTextView?.text = mTrainNameTextView?.text.toString() + mTrainNumber?.trainName
            mDistanceTextView?.text = mDistanceTextView?.text.toString() + mTrainNumber?.distance
            mStartTimeTextView?.text = mStartTimeTextView?.text.toString() + DateUtil.format(LocalDateTime.ofInstant(Date(mTrainNumber?.startTime!!).toInstant(), ZoneId.of("GMT+8")))
            mTravelTimeTextView?.text = mTravelTimeTextView?.text.toString() + mTrainNumber?.travelTime
            mEndTimeTextView?.text = mEndTimeTextView?.text.toString() + DateUtil.format(LocalDateTime.ofInstant(Date(mTrainNumber?.endTime!!).toInstant(), ZoneId.of("GMT+8")))
            mPriceTextView?.text = mPriceTextView?.text.toString() + mTrainNumber?.price
        }

        override fun onClick(v: View) {

            val builder = AlertDialog.Builder(activity)
            builder.setMessage("创建订单")
            builder.setPositiveButton("确认", DialogInterface.OnClickListener { dialog, id ->
                SubmitOrderTask().execute(mTrainNumber)
            })
            builder.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, id ->

            })

            builder.show()
        }

    }

    private inner class TrainNumberAdapter(private var mTrainNumberList: List<TrainNumberJson>?) : RecyclerView.Adapter<TrainNumberHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNumberHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.list_item_train_number, parent, false)
            return TrainNumberHolder(view)
        }

        override fun onBindViewHolder(holder: SearchFragment.TrainNumberHolder, position: Int) {
//            val trainNumber = mTrainNumberList!![position]
            holder.bindTrainNumber(mTrainNumberList!!, position)
        }

        override fun getItemCount(): Int {
            return mTrainNumberList!!.size
        }

//        fun setTrainNumberList(trainNumberList: List<TrainNumberJson>) {
//            mTrainNumberList = trainNumberList
//        }
    }

    private inner class LocationListTask: AsyncTask<Void, Void, AjaxResult<List<String>>>() {

        private val url = SystemFinal.HOST + "/api/graph/locationList"

        override fun doInBackground(vararg params: Void): AjaxResult<List<String>> {
            return AjaxResult.stringToAjaxResult<List<String>>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(""))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<List<String>>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    mStartLocationSpinner?.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, ajaxResult.data)
                    StartToLocationListTask().execute()
                }
            }
        }
    }

    private inner class StartToLocationListTask: AsyncTask<Void, Void, AjaxResult<List<String>>>() {

        private val url = SystemFinal.HOST + "/api/graph/startToLocationList"

        override fun doInBackground(vararg params: Void): AjaxResult<List<String>> {
            return AjaxResult.stringToAjaxResult<List<String>>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(mapOf(
                    "startLocation" to mStartLocationSpinner?.selectedItem.toString(),
                    "canTransfer" to false  // 暂时写死
            )))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<List<String>>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    mEndLocationSpinner?.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, ajaxResult.data)
                }
            }
        }
    }

    private inner class StartToEndTrainNumberListTask: AsyncTask<Void, Void, AjaxResult<List<TrainNumberJson>>>() {

        private val url = SystemFinal.HOST + "/api/graph/startToEndTrainNumberList"

        override fun doInBackground(vararg params: Void): AjaxResult<List<TrainNumberJson>> {
            val localDate = LocalDate.now()
            return AjaxResult.stringToAjaxResult<List<TrainNumberJson>>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(mapOf(
                    "startLocation" to mStartLocationSpinner?.selectedItem.toString(),
                    "endLocation" to mEndLocationSpinner?.selectedItem.toString(),
                    "canTransfer" to false,  // 暂时写死
                    "year" to localDate.year,
                    "month" to localDate.monthValue,
                    "day" to localDate.dayOfMonth
            )))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<List<TrainNumberJson>>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    mRecyclerView?.adapter = TrainNumberAdapter(ajaxResult.data)
                }
            }
        }
    }

    private inner class SubmitOrderTask: AsyncTask<TrainNumberJson, Void, AjaxResult<String>>() {

        private val url = SystemFinal.HOST + "/api/order/submitOrder"

        override fun doInBackground(vararg params: TrainNumberJson): AjaxResult<String> {
            return AjaxResult.stringToAjaxResult<String>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(params[0]))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<String>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("创建订单成功")
                    builder.show()
                }
            }
        }
    }

}