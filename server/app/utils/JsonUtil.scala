package utils

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

//import scala.reflect.{ClassTag, _}

/**
  * @author xzy
  */
object JsonUtil {

  val jacksonMapper = new ObjectMapper() with ScalaObjectMapper
  jacksonMapper.registerModule(DefaultScalaModule)
//  jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def anyToString(value: Any): String = {
    jacksonMapper.writeValueAsString(value)
  }

  def stringToAny[T: Manifest](value: String): T ={
    try {
      jacksonMapper.readValue[T](value)
    }
    catch {
      case npe: NullPointerException => {
        null.asInstanceOf[T]
      }
    }
  }

//  def toJson(value: Map[Symbol, Any]): String = {
//    toJson(value map { case (k,v) => k.name -> v})
//  }
//
//  def toJson(value: Any): String = {
//    jacksonMapper.writeValueAsString(value)
//  }
//
//  //  def fromJson[T: ClassTag](json: String): T = {
//  //    jacksonMapper.readValue[T](json, classTag[T].runtimeClass.asInstanceOf[Class[T]])
//  //  }
//
//  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)
//
//  def fromJson[T: ClassTag](json: String): T = {
//    //    jacksonMapper.readValue[T](json)
//    jacksonMapper.readValue[T](json, classTag[T].runtimeClass.asInstanceOf[Class[T]])
//  }
//
//  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
//    jacksonMapper.readValue[T](json)
//    jacksonMapper.readValue[T](json, classTag[T].runtimeClass.asInstanceOf[Class[T]])
//  }

}
