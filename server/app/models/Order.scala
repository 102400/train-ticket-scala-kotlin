package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.annotations._
import com.github.aselab.activerecord.dsl._

case class Order(
  var userId: Long,
  var lineName: String,
  var trainNumber: String,
  var trainName: String,
  var startTime: Long,
  var distance: Double,
  var travelTime: Long,
  var endTime: Long,
  var price: Double
) extends ActiveRecord with Datestamps {

}

object Order extends ActiveRecordCompanion[Order] {

}