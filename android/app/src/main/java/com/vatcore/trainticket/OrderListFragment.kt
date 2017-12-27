package com.vatcore.trainticket

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.vatcore.trainticket.util.DateUtil
import com.vatcore.trainticket.util.HttpUtil
import com.vatcore.trainticket.util.JsonUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * @author xzy
 */
class OrderListFragment: Fragment() {

    companion object {
        var sOrderListFragment: OrderListFragment? = null

        @Synchronized
        fun newInstance(): OrderListFragment {
            if (sOrderListFragment == null) {
                sOrderListFragment = OrderListFragment()
            }
            return sOrderListFragment as OrderListFragment
        }
    }

    private var mRefreshButton: Button? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_order_list, container, false)

        mRefreshButton = v?.findViewById(R.id.fragment_order_list_refresh_button)

        mRecyclerView = v?.findViewById(R.id.fragment_order_list_recycler_view)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity);

        mRefreshButton!!.setOnClickListener({
            OrderListTask().execute()
        })

        OrderListTask().execute()

        return v
    }

    private inner class TrainNumberHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mLineNameTextView: TextView? = null
        private var mTrainNumberTextView: TextView? = null
        private var mTrainNameTextView: TextView? = null
        private var mDistanceTextView: TextView? = null
        private var mStartTimeTextView: TextView? = null
        private var mTravelTimeTextView: TextView? = null
        private var mEndTimeTextView: TextView? = null
        private var mPriceTextView: TextView? = null

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

        }
    }

    private inner class TrainNumberAdapter(private var mTrainNumberList: List<TrainNumberJson>?) : RecyclerView.Adapter<TrainNumberHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNumberHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.list_item_train_number, parent, false)
            return TrainNumberHolder(view)
        }

        override fun onBindViewHolder(holder: TrainNumberHolder, position: Int) {
//            val trainNumber = mTrainNumberList!![position]
            holder.bindTrainNumber(mTrainNumberList!!, position)
        }

        override fun getItemCount(): Int {
            return mTrainNumberList!!.size
        }

    }


    private inner class OrderListTask: AsyncTask<Void, Void, AjaxResult<List<TrainNumberJson>>>() {

        private val url = SystemFinal.HOST + "/api/order/orderList"

        override fun doInBackground(vararg params: Void): AjaxResult<List<TrainNumberJson>> {
            val localDate = LocalDate.now()
            return AjaxResult.stringToAjaxResult<List<TrainNumberJson>>(HttpUtil.postJson(url, JsonUtil.anyToString(JsonParam.build(""))))
        }

        override fun onPostExecute(ajaxResult: AjaxResult<List<TrainNumberJson>>) {
            when (ajaxResult.code) {
                AjaxResult.CODE_SUCCESS -> {
                    mRecyclerView?.adapter = TrainNumberAdapter(ajaxResult.data)
                }
            }
        }
    }
}