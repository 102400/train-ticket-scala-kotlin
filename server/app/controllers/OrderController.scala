package controllers

import javax.inject.{Inject, Singleton}

import actions.GlobalAction
import beans.{AjaxResult, StartToLocationListJson, TrainNumberBean}
import models.Order
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.PlayUtil

@Singleton
class OrderController @Inject()(
  cc: ControllerComponents,
  globalAction: GlobalAction
) extends AbstractController(cc) {

  def submitOrder = globalAction {request =>
    val param = PlayUtil.getJson[TrainNumberBean](true, request)
    Ok((if (param._2.isEmpty) AjaxResult.newTokenError()
        else {
          val trainNumber = param._1
          try {
            Order.transaction({

              var orderCreate = Order(
                userId = param._2.get.id.toLong,
                lineName = trainNumber.lineName,
                trainNumber = trainNumber.trainNumber,
                trainName = trainNumber.trainName,
                startTime = trainNumber.startTime,
                distance = trainNumber.distance,
                travelTime = trainNumber.travelTime,
                endTime = trainNumber.endTime,
                price = trainNumber.price
              )
              orderCreate = orderCreate.create

              println("AjaxResult.newSuccess")
              AjaxResult.newSuccess("")
            })
          }
          catch {
            case e: Exception => {
              e.printStackTrace()
              AjaxResult.newError()
            }
          }
    }).toString)
  }

  def orderList = globalAction {request =>
    val param = PlayUtil.getJson[String](true, request)
    Ok((if (param._2.isEmpty) AjaxResult.newTokenError()
        else AjaxResult.newSuccess(Order.findAllBy("userId", param._2.get.id.toLong).load.map(order => {
          Map(
            "lineName" -> order.lineName,
            "trainNumber" -> order.trainNumber,
            "trainName" -> order.trainName,
            "startTime" -> order.startTime,
            "distance" -> order.distance,
            "travelTime" -> order.travelTime,
            "endTime" -> order.endTime,
            "price" -> order.price
          )
    }))).toString)
  }

}