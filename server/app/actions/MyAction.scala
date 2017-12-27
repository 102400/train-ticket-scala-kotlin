package actions

import javax.inject.Inject

import beans._
import org.redisson.Redisson
import org.redisson.api.{RMap, RedissonClient}
import play.api.mvc._
import utils.JsonUtil

import scala.concurrent.{ExecutionContext, Future}

object MyAction {

  def jsonHandler(request: Request[AnyContent]): JsonParam[Any] =  {

    println(request.body.asJson.get.toString())

    val json = request.body.asJson.get.toString()
    val jsonParam: JsonParam[Any] = JsonUtil.stringToAny[JsonParam[Any]](json)

    println(jsonParam.clientType)
    println(jsonParam.phone)
    println(jsonParam.token)
    println(jsonParam.data)

    jsonParam
  }

//  def noTokenAction = {
//
//  }
//
//  def tokenAction = {
//
//  }

}

@deprecated
class TokenAction @Inject()(parser: BodyParsers.Default)(implicit executionContext: ExecutionContext) extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {

    val jsonParam: JsonParam[Any] = MyAction.jsonHandler(request.asInstanceOf[Request[AnyContent]])

    if (jsonParam.token == null) {
      return Future.successful(Results.Forbidden)  // 强行403
    }

    val redisson: RedissonClient = Redisson.create()
    val map: RMap[String, String] = redisson.getMap("train_ticket_phone_" + jsonParam.phone)

    val userToken = map.get("token")
    val userId = map.get("id")

    if (!userToken.equals(map.get("token"))) {

      return Future.successful(Results.Ok(AjaxResult.newTokenError().toString))
//      return Future.successful(Results.Forbidden)  // 强行403
    }

//    Final.REQUEST_MAP.put(request.asInstanceOf[Request[AnyContent]], RequestAny(RequestUser(id = userId, phone = jsonParam.phone), RequestJsonData(jsonParam.data)))

    if (jsonParam.data == null) {
      return Future.successful(Results.BadRequest)  // 强行400
    }

    block(request)

  }

}

@deprecated
class NoTokenAction @Inject()(parser: BodyParsers.Default)(implicit executionContext: ExecutionContext) extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {

    val jsonParam: JsonParam[Any] = MyAction.jsonHandler(request.asInstanceOf[Request[AnyContent]])

    if (jsonParam.data == null) {
      Future.successful(Results.BadRequest)  // 强行400
    }
    else {
      block(request)
    }
  }

}

class GlobalAction @Inject()(parser: BodyParsers.Default)(implicit executionContext: ExecutionContext) extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {

//    val jsonParam: JsonParam[Any] = MyAction.jsonHandler(request.asInstanceOf[Request[AnyContent]])
//
//    if (jsonParam.data == null) {
//      Future.successful(Results.BadRequest)  // 强行400
//    }
//    else {
//      block(request)
//    }

    block(request)
  }

}