import com.google.inject.AbstractModule
import java.time.Clock

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import play.libs.Json

import scala.reflect.ClassTag

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {

//    val mapper = new ObjectMapper with ScalaObjectMapper
//    mapper.registerModule(DefaultScalaModule)
//
//    bind(classOf[MyMapper]).asEagerSingleton()
  }

}

//class MyMapper {
//
//    Json.newDefaultMapper()
//    val mapper = new ObjectMapper with ScalaObjectMapper
//    mapper.registerModule(DefaultScalaModule)
//
//    Json.setObjectMapper(mapper)
//
//    def fromJson[T: ClassTag](json: String): T = {
//      mapper.readValue[T](json, classTag[T].runtimeClass.asInstanceOf[Class[T]])
//    }
//}
//