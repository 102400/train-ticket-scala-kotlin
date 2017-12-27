package beans

import java.util.concurrent.ConcurrentHashMap

import play.api.mvc.{AnyContent, Request}

import scala.collection.mutable

case class RequestAny(requestUser: RequestUser, requestJsonData: RequestJsonData)

case class RequestUser(id: String, phone: String)

case class RequestJsonData(any: Any)

object Final {

//  val REQUEST_MAP = new mutable.HashMap[Request[AnyContent], RequestAny]

}
