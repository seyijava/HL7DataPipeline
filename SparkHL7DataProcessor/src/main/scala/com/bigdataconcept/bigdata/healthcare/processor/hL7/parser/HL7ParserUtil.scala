package com.bigdataconcept.bigdata.healthcare.processor.hL7.parser

import com.fasterxml.jackson.databind.ObjectMapper
import scala.collection.JavaConverters.asJavaIterableConverter
import scala.collection.JavaConverters.mapAsJavaMapConverter
import scala.collection.JavaConverters.setAsJavaSetConverter

object HL7ParserUtil {
  
  
  
  val mapper = new ObjectMapper()



  val writer = mapper.writerWithDefaultPrettyPrinter()

  def toJson(map: java.util.Map[String,Any]) : String = {
    mapper.writeValueAsString(map)
  }

  def toJsonPretty(map: java.util.Map[String,Any]) : String = {
    writer.writeValueAsString(map)
  }

  def toJava(x: Any): Any = {
    import scala.collection.JavaConverters._
    x match {
      case y: scala.collection.MapLike[_, _, _] => y.map { case (d, v) => toJava(d) -> toJava(v) } asJava
      case y: scala.collection.SetLike[_,_] => y map { item: Any => toJava(item) } asJava
      case y: Iterable[_] => y.map { item: Any => toJava(item) } asJava
      case y: Iterator[_] => toJava(y.toIterable)
      case _ => x
    }
  }

}