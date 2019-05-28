package com.bigdataconcept.bigdata.healthcare.processor.hL7.model

import org.apache.log4j.Logger
import com.bigdataconcept.bigdata.healthcare.processor.hL7.model.IncomingHL7Message.HL7Message
import org.apache.spark.streaming.dstream.DStream
import com.bigdataconcept.bigdata.healthcare.processor.hL7.util.HL7JsonMessage
import java.util.Date
import com.bigdataconcept.bigdata.healthcare.processor.hL7.parser.HapiParser
import com.bigdataconcept.bigdata.healthcare.processor.hL7.parser.HL7ParserUtil
import com.bigdataconcept.bigdata.healthcare.processor.hL7.util.AppConfig
import com.bigdataconcept.bigdata.healthcare.processor.hL7.util.HL7JsonMessage
import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.java.document.json.JsonObject
import java.util.concurrent.ThreadLocalRandom;
import org.apache.spark.sql.SparkSession
import org.bson.Document
import com.mongodb.spark._
import java.text.SimpleDateFormat
import org.apache.commons.text.StringEscapeUtils
import com.mongodb.BasicDBObject;
import com.mongodb.spark.config.WriteConfig
import java.util.HashMap

/**
 * Oluwasey otun
 * Hl7ETL transformed and parsed incoming HL7 message and store it into mongoDB
 */
class HL7ETL extends Serializable 
{
  
 
  @transient lazy val logger = Logger.getLogger(getClass.getName)    
  
  def transformHL7MessageToJsonAndSaveToDataStore(HL7MessageDStream: DStream[HL7Message],validate: Boolean, config: AppConfig) 
  {
    
    HL7MessageDStream.foreachRDD(
    	rdd=> 
    	if(!rdd.isEmpty)
    	{
    	 
    	 val hapiParser = new HapiParser(validate)
    	 val hl7JsonMessageDF = rdd.map(msg=>
    	                               {
    		                              val jsonData = StringEscapeUtils.unescapeJson(HL7ParserUtil.toJsonPretty(HL7ParserUtil.toJava(hapiParser.parse(msg.body)).asInstanceOf[java.util.Map[String,Any]]))
    		                              val jsonMessage = jsonData.replaceAll("\\\\", "");
    		                              logger.info(jsonMessage + "\n");
    		                              val id = msg.messageType  + "/" + ThreadLocalRandom.current().nextLong();
    		                              val mongdburl  = config.mongoDBHost + config.mogoDBCollection
    		                              val document = new Document()
    		                              document.append("createdDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
    		                              document.append("messageType",  msg.messageType)
    		                              document.append("messageBody", jsonMessage);
    		                             
    		                           }).saveToMongoDB(WriteConfig(Map("uri" ->  (config.mongoDBHost + config.mogoDBCollection))))
    	}
    	else
    	{
    	     logger.info("RDD is Empty Nothing to Process\\n\n\n\n\n")
    	})
   

  }
  
 

}