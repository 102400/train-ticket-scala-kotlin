package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._  // 此行必须有

object Tables extends ActiveRecordTables with PlaySupport {

  val orders = table[Order]
  val users = table[User]
//  val locations = table[Location]

}