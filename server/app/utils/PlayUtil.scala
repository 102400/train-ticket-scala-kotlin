package utils

import beans.{JsonParam, UserBean}
import models.User
import org.redisson.Redisson
import org.redisson.api.{RMap, RedissonClient}
import play.api.mvc.{AnyContent, Request}

/**
  * @author xzy
  */
object PlayUtil {

  def getJson[T: Manifest](token: Boolean, request: Request[AnyContent]): (T, Option[UserBean]) = {

    var jsonParam: JsonParam[T] = null

    try {

      jsonParam = JsonUtil.stringToAny[JsonParam[T]](request.body.asJson.get.toString)

      (jsonParam.data, if (token) {
        val redisson: RedissonClient = Redisson.create()
        val map: RMap[String, String] = redisson.getMap("train_ticket_phone_" + jsonParam.phone)
        Option.apply(UserBean(id = map.get("id"), phone = jsonParam.phone))
      } else Option.empty)
    }
    catch {
      case e: Exception => (null.asInstanceOf[T], Option.empty)
    }
  }

//  def getJson[T: Manifest](request: Request[AnyContent]): T = {
//    JsonUtil.stringToAny[JsonParam[T]](request.body.asJson.get.toString).data
//  }

}