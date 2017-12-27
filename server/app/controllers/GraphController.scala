package controllers

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

import actions.{GlobalAction, NoTokenAction}
import beans.{AjaxResult, PhoneAndPasswordJson, StartToEndTrainNumberListJson, StartToLocationListJson}
import com.vatcore.util.DateUtil
import graph.Graph.{Line, ShanghaiToBeijingLine}
import graph.{Graph, Node, Relationship, RelationshipType}
import models.Location
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.PlayUtil

import scala.collection.mutable

@Singleton
class GraphController @Inject()(
  cc: ControllerComponents,
  noTokenAction: NoTokenAction,
  globalAction: GlobalAction
) extends AbstractController(cc) {

  def locationList = globalAction {request =>
    val param = PlayUtil.getJson[String](true, request)
    Ok((if (param._2.isEmpty) AjaxResult.newTokenError()
        else AjaxResult.newSuccess(Graph.locationList.map(x => x.name))).toString)
  }

  def startToLocationList = globalAction {request =>
    val param = PlayUtil.getJson[StartToLocationListJson](true, request)
    Ok((if (param._2.isEmpty) AjaxResult.newTokenError()
        else {
          val set = new mutable.HashSet[Node[Location]]
          reachableNodeSet(set, Graph.nodeMap(param._1.startLocation), null, !param._1.canTransfer)
          AjaxResult.newSuccess(set.filter(location => !location.element.name.equals(param._1.startLocation)).map(location => location.element.name))
    }).toString)
  }

  def reachableNodeSet(set: mutable.Set[Node[Location]], currentNode: Node[Location], relationshipType: RelationshipType, oneType: Boolean): Unit = {
    currentNode.relationshipList.foreach(r => {
      if (r.relationshipType.equals(relationshipType) || relationshipType == null) {
        r.nodeList.foreach(location => {
          if (!set.contains(location.node)) {  // 当这个节点没被访问过
            set += location.node
            reachableNodeSet(set, location.node, if (oneType) r.relationshipType else null, oneType)
          }
        })
      }
    })
  }

  // 先从开始节点(startNode)遍历找到所有能到终点节点(endNode)的关系(单向线路)以及权重(距离)
  // 从关系开始节点遍历到开始节点(startNode)计算得到权重
  def startToEndTrainNumberList = globalAction {request =>
    val param = PlayUtil.getJson[StartToEndTrainNumberListJson](true, request)
    Ok((if (param._2.isEmpty) AjaxResult.newTokenError()
        else {
//          param._1.canTransfer  // 无效参数

          val set = new mutable.HashSet[Node[Location]]
          val relationshipTypeAndWeightList = new mutable.ListBuffer[RelationshipTypeAndWeight]
          reachNodeSet(set, Graph.nodeMap(param._1.startLocation), Graph.nodeMap(param._1.endLocation), null, !param._1.canTransfer, 0, relationshipTypeAndWeightList)

          AjaxResult.newSuccess(relationshipTypeAndWeightList.flatMap(relationshipTypeAndWeight => {
            val startToEndWeight = relationshipTypeAndWeight.weight

            val line = relationshipTypeAndWeight.relationshipType.asInstanceOf[Line]

            val secondSet = new mutable.HashSet[Node[Location]]
            val weightList = new mutable.ListBuffer[RelationshipTypeAndWeight]

            val lineStartToStartWeight = if (!line.startLocation.equals(Graph.nodeMap(param._1.startLocation))) {
              reachNodeSet(secondSet, line.startLocation, Graph.nodeMap(param._1.startLocation), relationshipTypeAndWeight.relationshipType, true, 0, weightList)
              weightList.head.weight
            } else 0

            line.trainNumberList.map(trainNumber => {

              val lineStartTime = LocalDateTime.of(param._1.year, param._1.month, param._1.day, trainNumber.startHour, trainNumber.startMinute)

              val distance = startToEndWeight / 1000
              val startTime = lineStartTime.plusMinutes((lineStartToStartWeight / 1000 * 60 / trainNumber.train.speed).toLong)
              val travelTime = (distance * 60 / trainNumber.train.speed).toLong
              val endTime = startTime.plusMinutes((distance * 60 / trainNumber.train.speed).toLong)
              val price = distance * trainNumber.train.price

              Map(
                "lineName" -> line.name,
                "trainNumber" -> trainNumber.trainNumber,
                "trainName" -> trainNumber.train.trainName,
                "startTime" -> DateUtil.getLong(startTime),
                "distance" -> distance,
                "travelTime" -> travelTime,
                "endTime" -> DateUtil.getLong(endTime),
                "price" -> price
              )
            })
          }))
        }
    ).toString)
  }

  class RelationshipTypeAndWeight(val relationshipType: RelationshipType, val weight: Double)

  // currentNode 当前遍历节点(从线路起点开始遍历)
  // endNode 重点节
  // 可有环, 不适用起点终点都为一个节点, oneType 废弃, 入口方法relationshipType必须为null
  def reachNodeSet(set: mutable.Set[Node[Location]], currentNode: Node[Location], endNode: Node[Location], relationshipType: RelationshipType, oneType: Boolean, weight: Double, relationshipTypeAndWeightList: mutable.Buffer[RelationshipTypeAndWeight]): Unit = {
   currentNode.relationshipList.foreach(r => {
      if (r.relationshipType.equals(relationshipType) || relationshipType == null) {
        r.nodeList.foreach(location => {
          if (!set.contains(location.node)) {  // 当这个节点在此关系下没被访问过
            val rSet = if (relationshipType == null) new mutable.HashSet[Node[Location]] else set  // 当为起点时为每个关系new一个set, 保存关系的已访问节点
            rSet += location.node
            if (!endNode.equals(location.node)) reachNodeSet(rSet, location.node, endNode, if (oneType) r.relationshipType else null, oneType, weight + location.weight, relationshipTypeAndWeightList)
            else relationshipTypeAndWeightList += new RelationshipTypeAndWeight(r.relationshipType, weight + location.weight)
          }
        })
      }
    })
  }

}
