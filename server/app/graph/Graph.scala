package graph

import models.Location
import utils.MathUtil

import scala.collection.mutable

// nodeA -> nodeB
case class Edge[T](nodeA: T, nodeB: T, weight: Double)

trait RelationshipType

case class ToNode[T](node: Node[T], weight: Double)

class Relationship[T] (val relationshipType: RelationshipType) {
  val nodeList = new mutable.ListBuffer[ToNode[T]]
}

class Node[T] (val element: T) {
  val relationshipList = new mutable.ListBuffer[Relationship[T]]
}

object Graph {

  val Shanghai = Location("上海", 31.2304324029, 121.4737919321)
  val Beijing = Location("北京", 39.9047253699, 116.4072154982)
  val Guangzhou = Location("广州", 23.1290765766, 113.2643446427)
  val Shenzhen = Location("深圳", 22.5428750360, 114.0595699327)
  val HongKong = Location("香港", 22.2780757700, 114.1604896100)
  val Macau = Location("澳门", 22.2016754600, 113.5440083200)
  val Chengdu = Location("成都", 30.5702183724, 104.0647735044)
  val Chongqing = Location("重庆", 29.5647048135, 106.5507137149)
  val Harbin = Location("哈尔滨", 45.8021755616, 126.5358247345)
  val Hangzhou = Location("杭州", 30.2741702308, 120.1551656314)
  val Nanjing = Location("南京", 32.0584065670, 118.7964897811)
  val Wuhan = Location("武汉", 30.5927599029, 114.3052387810)
  val Changsha = Location("长沙", 28.2277765095, 112.9388453666)
  val Shijiazhuang = Location("石家庄", 38.0427810026, 114.5143212580)
  val Zhengzhou = Location("郑州", 34.7472541716, 113.6249284647)
  val Xian = Location("西安", 34.3412614674, 108.9398165260)
  val Lanzhou = Location("兰州", 36.0613769373, 103.8341600069)
  val Lhasa = Location("拉萨", 29.6441135160, 91.1144530801)
  val Shenyang = Location("沈阳", 41.6772012272, 123.4631317933)
  val Changchun = Location("长春", 43.8160040902, 125.3235505093)
  val Xuzhou = Location("徐州", 34.2043994527, 117.2857558529)
  val Jinan = Location("济南", 36.6518534720, 117.1200983354)
  val Nanchang = Location("南昌", 28.6820357017, 115.8579438581)

  private def distance(locationA: Location, locationB: Location): Double = {
    MathUtil.distanceSimplifyChina(locationA.latitude, locationA.longitude, locationB.latitude, locationB.longitude)
  }

  private def edge(nodeA: Location, nodeB: Location): Edge[Location] = {
    Edge(nodeA, nodeB, distance(nodeA, nodeB))
  }

  val locationList = List(
    Shanghai, Beijing, Guangzhou, Shenzhen, HongKong, Macau, Chengdu, Chongqing, Harbin, Hangzhou, Nanjing, Wuhan,
    Changsha, Shijiazhuang, Zhengzhou, Xian, Lanzhou, Lhasa, Shenyang, Changchun, Xuzhou, Jinan, Nanchang
  )

//  private val nodeMap = locationList.flatMap(x => Map(x -> new Node[Location](x))).toMap

  // "上海" -> node
  // location -> node
  val nodeMap = locationList.flatMap(x => {
    val node = new Node[Location](x)
    Map(
      x -> node,
      x.name -> node
    )
  }).toMap

  // 低速轨道
//  private val LowTrack = List(
//    edge(Shanghai, Nanjing),
//    edge(Shanghai, Hangzhou),
//    edge(Guangzhou, Macau),
//    edge(Guangzhou, Shenzhen),
//    edge(HongKong, Shenzhen),
//    edge(Harbin, Changchun),
//    edge(Changchun, Shenyang),
//    edge(Shenyang, Beijing),
//    edge(Nanjing, Xuzhou),
//    edge(Xuzhou, Jinan),
//    edge(Beijing, Jinan),
//    edge(Beijing, Shijiazhuang),
//    edge(Shijiazhuang, Jinan),
//    edge(Shijiazhuang, Zhengzhou),
//    edge(Zhengzhou, Xuzhou),
//    edge(Zhengzhou, Wuhan),
//    edge(Wuhan, Changsha),
//    edge(Changsha, Guangzhou),
//    edge(Nanjing, Hangzhou),
//    edge(Hangzhou, Nanchang),
//    edge(Nanchang, Shenzhen),
//    edge(Wuhan, Nanchang),
//    edge(Changsha, Nanchang),
//    edge(Zhengzhou, Xian),
//    edge(Xian, Lanzhou),
//    edge(Lhasa, Lanzhou),
//    edge(Chengdu, Chongqing),
//    edge(Chongqing, Wuhan),
//    edge(HongKong, Macau)
//  )

  private val shanghaiToBeijingEdgeList = List(
    edge(Shanghai, Nanjing),
    edge(Nanjing, Xuzhou),
    edge(Xuzhou, Jinan),
    edge(Jinan, Beijing)
  )

  private val shanghaiToLhasaEdgeList = List(
    edge(Shanghai, Nanjing),
    edge(Nanjing, Xuzhou),
    edge(Xuzhou, Zhengzhou),
    edge(Zhengzhou, Xian),
    edge(Xian, Lanzhou),
    edge(Lanzhou, Lhasa)
  )

  private val shanghaiToGuangzhouEdgeList = List(
    edge(Shanghai, Hangzhou),
    edge(Hangzhou, Nanchang),
    edge(Nanchang, Changsha),
    edge(Changsha, Guangzhou)
  )

  private val beijingToGuangzhouEdgeList = List(
    edge(Beijing, Shijiazhuang),
    edge(Shijiazhuang, Zhengzhou),
    edge(Zhengzhou, Wuhan),
    edge(Wuhan, Changsha),
    edge(Changsha, Guangzhou)
  )

  private val hongKongToHongkongEdgeList = List(
    edge(HongKong, Shenzhen),
    edge(Shenzhen, Guangzhou),
    edge(Guangzhou, Macau),
    edge(Macau, HongKong)
  )

  private val shenzhenToHarbinEdgeList = List(
    edge(Shenzhen, Nanchang),
    edge(Nanchang, Wuhan),
    edge(Wuhan, Zhengzhou),
    edge(Zhengzhou, Shijiazhuang),
    edge(Shijiazhuang, Beijing),
    edge(Beijing, Shenyang),
    edge(Shenyang, Changchun),
    edge(Changchun, Harbin)
  )

  private val shanghaiToChengduEdgeList = List(
    edge(Shanghai, Hangzhou),
    edge(Hangzhou, Nanchang),
    edge(Nanchang, Wuhan),
    edge(Wuhan, Chongqing),
    edge(Chongqing, Chengdu)
  )

  /**
    * @param trainName 车型
    * @param speed 000 km/h
    * @param price 000 yuan/km
    */
  class Train(val trainName: String, val speed: Double, val price: Double)

  // 火车型号
  case class DesireTrain() extends Train(trainName = "欲望号", speed = 350, price = 0.51)
  case class MagicTrain() extends Train(trainName = "膜法号", speed = 233, price = 0.33)
  case class CuckoldTrain() extends Train(trainName = "老实人号", speed = 166, price = 0.66)

  // 车次
  case class TrainNumber(train: Train, trainNumber: String, startHour: Int, startMinute: Int)

  class Line(val direction: Boolean, val startLocation: Node[Location], val name: String, val trainNumberList: List[TrainNumber]) extends RelationshipType

  // 车次 true 为 正向, false 反向
  case class ShanghaiToBeijingLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val shanghaiToBeijingLineTrue = ShanghaiToBeijingLine(true, Graph.nodeMap(Shanghai), "L1(上海 -> 北京)", List(
    TrainNumber(DesireTrain(), "X1", 7, 30), TrainNumber(MagicTrain(), "X2", 10, 30), TrainNumber(CuckoldTrain(), "X3", 10, 30), TrainNumber(MagicTrain(), "X4", 14, 30), TrainNumber(DesireTrain(), "X5", 18, 30))
  )
  private val shanghaiToBeijingLineFalse = ShanghaiToBeijingLine(false, Graph.nodeMap(Beijing), "L2(北京 -> 上海)", List(
    TrainNumber(DesireTrain(), "X11", 7, 25), TrainNumber(DesireTrain(), "X12", 10, 40), TrainNumber(CuckoldTrain(), "X13", 10, 20), TrainNumber(MagicTrain(), "X14", 13, 30), TrainNumber(CuckoldTrain(), "X15", 17, 30)
  ))
  case class ShanghaiToLhasaLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val shanghaiToLhasaLineTrue = ShanghaiToLhasaLine(true, Graph.nodeMap(Shanghai), "L10(上海 -> 拉萨)", List(
    TrainNumber(DesireTrain(), "X21", 6, 50), TrainNumber(CuckoldTrain(), "X22", 12, 20), TrainNumber(MagicTrain(), "X23", 18, 30)
  ))
  private val shanghaiToLhasaLineFalse = ShanghaiToLhasaLine(false, Graph.nodeMap(Lhasa), "L11(拉萨 -> 上海)", List(
    TrainNumber(MagicTrain(), "X31", 8, 30), TrainNumber(DesireTrain(), "X32", 13, 30), TrainNumber(CuckoldTrain(), "X33", 19, 30)
  ))
  case class ShanghaiToGuangzhouLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val shanghaiToGuangzhouLineTrue = ShanghaiToGuangzhouLine(true, Graph.nodeMap(Shanghai), "L20(上海 -> 广州)", List(
    TrainNumber(DesireTrain(), "X41", 9, 10), TrainNumber(MagicTrain(), "X42", 11, 20), TrainNumber(MagicTrain(), "X43", 17, 20)
  ))
  private val shanghaiToGuangzhouLineFalse = ShanghaiToGuangzhouLine(false, Graph.nodeMap(Guangzhou), "L21(广州 -> 上海)", List(
    TrainNumber(MagicTrain(), "X51", 8, 15), TrainNumber(DesireTrain(), "X52", 13, 30), TrainNumber(MagicTrain(), "X53", 17, 10)
  ))
  case class BeijingToGuangzhouLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val beijingToGuangzhouLineTrue = BeijingToGuangzhouLine(true, Graph.nodeMap(Beijing), "L30(北京 -> 广州)", List(
    TrainNumber(DesireTrain(), "X61", 6, 20), TrainNumber(MagicTrain(), "X62", 13, 10), TrainNumber(MagicTrain(), "X63", 18, 15)
  ))
  private val beijingToGuangzhouLineFalse = BeijingToGuangzhouLine(false, Graph.nodeMap(Guangzhou), "L31(广州 -> 北京)", List(
    TrainNumber(MagicTrain(), "X71", 5, 30), TrainNumber(DesireTrain(), "X72", 13, 20), TrainNumber(MagicTrain(), "X73", 18, 25)
  ))
  case class HongKongToHongkongLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val hongKongToHongkongLineTrue = HongKongToHongkongLine(true, Graph.nodeMap(HongKong), "L40(香港 -> 香港)", List(
    TrainNumber(MagicTrain(), "X81", 8, 10), TrainNumber(DesireTrain(), "X82", 12, 50), TrainNumber(CuckoldTrain(), "X83", 16, 10), TrainNumber(MagicTrain(), "X84", 20, 10)
  ))
  private val hongKongToHongkongLineFalse = HongKongToHongkongLine(false, Graph.nodeMap(HongKong), "L41(香港 -> 香港)", List(
    TrainNumber(DesireTrain(), "X91", 9, 40), TrainNumber(MagicTrain(), "X92", 12, 45), TrainNumber(CuckoldTrain(), "X93", 16, 40), TrainNumber(MagicTrain(), "X94", 20, 50)
  ))
  case class ShenzhenToHarbinLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val shenzhenToHarbinLineTrue = ShenzhenToHarbinLine(true, Graph.nodeMap(Shenzhen), "L50(深圳 -> 哈尔滨)", List(
    TrainNumber(DesireTrain(), "X101", 9, 30), TrainNumber(CuckoldTrain(), "X102", 11, 25), TrainNumber(CuckoldTrain(), "X103", 16, 45)
  ))
  private val shenzhenToHarbinLineFalse = ShenzhenToHarbinLine(false, Graph.nodeMap(Harbin), "L51(哈尔滨 -> 深圳)", List(
    TrainNumber(DesireTrain(), "X111", 9, 10), TrainNumber(CuckoldTrain(), "X112", 11, 35), TrainNumber(CuckoldTrain(), "X113", 16, 55)
  ))
  case class ShanghaiToChengduLine(override val direction: Boolean, override val startLocation: Node[Location],override val name: String, override val trainNumberList: List[TrainNumber]) extends Line(direction = direction, startLocation = startLocation, name = name, trainNumberList = trainNumberList)
  private val shanghaiToChengduLineTrue = ShanghaiToChengduLine(true, Graph.nodeMap(Shanghai), "L60(上海 -> 成都)", List(
    TrainNumber(CuckoldTrain(), "X121", 6, 45), TrainNumber(MagicTrain(), "X122", 12, 30), TrainNumber(DesireTrain(), "X123", 16, 10)
  ))
  private val shanghaiToChengduLineFalse = ShanghaiToChengduLine(false, Graph.nodeMap(Chengdu), "L61(成都 -> 上海)", List(
    TrainNumber(CuckoldTrain(), "X131", 8, 35), TrainNumber(MagicTrain(), "X132", 12, 30), TrainNumber(DesireTrain(), "X133", 16, 20)
  ))

  {
    addEdgeListToRelationship(shanghaiToBeijingEdgeList, shanghaiToBeijingLineTrue, shanghaiToBeijingLineFalse)  // 上海 - 北京
    addEdgeListToRelationship(shanghaiToLhasaEdgeList, shanghaiToLhasaLineTrue, shanghaiToLhasaLineFalse)  // 上海 - 拉萨
    addEdgeListToRelationship(shanghaiToGuangzhouEdgeList, shanghaiToGuangzhouLineTrue, shanghaiToGuangzhouLineFalse)  // 上海 - 广州
    addEdgeListToRelationship(beijingToGuangzhouEdgeList, beijingToGuangzhouLineTrue, beijingToGuangzhouLineFalse)  // 北京 - 广州
    addEdgeListToRelationship(hongKongToHongkongEdgeList, hongKongToHongkongLineTrue, hongKongToHongkongLineFalse)  // 香港 - 香港
    addEdgeListToRelationship(shenzhenToHarbinEdgeList, shenzhenToHarbinLineTrue, shenzhenToHarbinLineFalse)  // 深圳 - 哈尔滨
    addEdgeListToRelationship(shanghaiToChengduEdgeList, shanghaiToChengduLineTrue, shanghaiToChengduLineFalse)  // 上海 - 成都
  }


  /**
    * 将边批量添加到某关系中
    * @param list 边列表
    * @param lineTrue 关系类型(方向正)
    * @param lineFalse 关系类型(方向反)
    */
  private def addEdgeListToRelationship(list: List[Edge[Location]], lineTrue: RelationshipType, lineFalse: RelationshipType): Unit = {
    list.foreach(edge => {
      // 互相添加节点
      val locationA = edge.nodeA
      val locationB = edge.nodeB

      val distance = edge.weight

      val relationshipA = new Relationship[Location](lineTrue)
      relationshipA.nodeList += ToNode(nodeMap(locationB), distance)

      val relationshipB = new Relationship[Location](lineFalse)
      relationshipB.nodeList += ToNode(nodeMap(locationA), distance)

      nodeMap(locationA).relationshipList += relationshipA
      nodeMap(locationB).relationshipList += relationshipB
    })
  }

  def main(args: Array[String]): Unit = {

//    println(nodeMap("上海").element.name)

//    nodeMap.foreach(x => {
//      val node = x._2.asInstanceOf[Node[Location]]
//      println("node -> " + x._1 + ":" + node.relationshipList.foreach(y => {
//        println(y.relationshipType)
//        println(y.nodeList.foreach(z => {
//          println(z.element.name)
//        }))
//      }))
//      println("***")
//    })
  }

  //  locationList.map(x => Map(x -> new Node[Location](x)))

}