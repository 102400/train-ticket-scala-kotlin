package controllers

import java.util.Date
import javax.inject.{Inject, Singleton}

import actions.{GlobalAction, NoTokenAction, TokenAction}
import beans.{AjaxResult, _}
import models.{Location, User}
import org.redisson.Redisson
import org.redisson.api.{RMap, RedissonClient}
import play.api.mvc._
import utils.{JsonUtil, PlayUtil, SecurityUtil}

@Singleton
class AccountController @Inject()(
  tokenAction: TokenAction,
  noTokenAction: NoTokenAction,
  globalAction: GlobalAction,
  cc: ControllerComponents
) extends AbstractController(cc) {

  def login = globalAction {request =>

    val param = PlayUtil.getJson[PhoneAndPasswordJson](false, request)
    val phoneAndPasswordJson = param._1

//    val phoneAndPasswordJson = PlayUtil.getJson[PhoneAndPasswordJson](request)

    val phone: String = phoneAndPasswordJson.phone
    val password: String = phoneAndPasswordJson.password

    if (phone != null && password != null && phone.length == 11 && password.length >= 6) {

      val token: String = SecurityUtil.MD5(new Date().getTime + password)

      try {
        if (User.transaction[Boolean]({
          User.findBy("phone", phone) match {
            case (Some(user)) =>
              val redisson: RedissonClient = Redisson.create()
              val map: RMap[String, String] = redisson.getMap("train_ticket_phone_" + phone)
              map.clear()
              map.put("id", user.id.toString)
              map.put("token", token)
              true
            case None => false
          }
        })) {
          Ok(AjaxResult.newSuccess(Map(
            "token" -> token
          )).toString)
        }
        else Ok(AjaxResult.newError().toString)
      }
      catch {
        case e: Exception => Ok(AjaxResult.newError().toString)
      }
    }
    else {
      Ok(AjaxResult.newIllegalArgument().toString)
    }
  }

  def register = globalAction {request =>

//    val phoneAndPasswordJson: PhoneAndPasswordJson = PlayUtil.getJson[PhoneAndPasswordJson](request)

    val param = PlayUtil.getJson[PhoneAndPasswordJson](false, request)
    val phoneAndPasswordJson = param._1

    val phone: String = phoneAndPasswordJson.phone
    val password: String = phoneAndPasswordJson.password

    if (phone != null && password != null && phone.length == 11 && password.length >= 6) {

      try {
        User.transaction({
          var user = User(
            phone = phoneAndPasswordJson.phone,
            password = SecurityUtil.MD5(password)
          )
          user = user.create

          Ok(AjaxResult.newSuccess().toString)
        })
      }
      catch {
        case e: Exception => {
          e.printStackTrace()
          Ok(AjaxResult.newError().toString)
        }
      }
    }
    else {
      Ok(AjaxResult.newIllegalArgument().toString)
    }
  }

  def verifyToken = tokenAction {request =>
    Ok(AjaxResult.newSuccess().toString)
  }

}
