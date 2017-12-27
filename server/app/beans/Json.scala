package beans

object Json {

}

class JsonParam[T](
  val clientType: String,
  val phone: String,
  val token: String,
  val data: T
)

//class LoginJson(val phone: String, val password: String)
//case class LoginJson(phone: String, password: String)

case class PhoneAndPasswordJson(phone: String, password: String)

case class StartToLocationListJson(startLocation: String, canTransfer: Boolean)

case class StartToEndTrainNumberListJson(
  startLocation: String, endLocation: String, canTransfer: Boolean,
  year: Int, month: Int, day: Int)