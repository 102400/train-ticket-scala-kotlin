package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.annotations._
import com.github.aselab.activerecord.dsl._

case class Location(
  var name: String,
  var latitude: Double,  // 纬度
  var longitude: Double  // 经度
) extends ActiveRecord with Datestamps {
}

object Location extends ActiveRecordCompanion[Location] {

}