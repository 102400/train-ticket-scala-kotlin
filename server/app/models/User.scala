package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.annotations._
import com.github.aselab.activerecord.dsl._

case class User(
     @Unique var phone: String,
     @Length(min = 6) var password: String
) extends ActiveRecord with Datestamps {

  // Datestamps CRUD必备

}

object User extends ActiveRecordCompanion[User] {

}