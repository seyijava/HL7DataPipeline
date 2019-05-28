package com.bigdataconcept.bigdata.healthcare.processor.hL7
import org.apache.spark.sql.SparkSession
import com.typesafe.config.{Config,ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import com.bigdataconcept.bigdata.healthcare.processor.hL7.model.IncomingHL7Message.HL7Message
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import com.bigdataconcept.bigdata.healthcare.processor.hL7.model.IncomingHL7Message
import com.bigdataconcept.bigdata.healthcare.processor.hL7.util.AppConfig
import java.io.File
import com.bigdataconcept.bigdata.healthcare.processor.hL7.model.HL7ETL



/**
 * Oluwaseyi Otun 
 * 
 * HL7ETLSparkProcessor serves and Real Time Streaming Data pipeline processing 
 * It consum HL7 messages from Kafka Topic and parse to Json and sink into NOsql 
 * Document data store.
 */

object HL7ETLSparkProcessor 
{

 

   def main(args : Array[String]) 
   {
    
     
     val bigdataPath = sys.env("BIGDATA_APP_HOME")
     
     
     println(bigdataPath);
     
     
     val configFile = new File(bigdataPath + "/hl7dataPipeline.conf")
     
     val configFactory = ConfigFactory.parseFile(configFile).getConfig("appConfig")
     
     val appConfig = AppConfig(configFactory.getString("kafka.host"),configFactory.getString("kafka.topic"),configFactory.getString("kafka.kafkaConsumerGrp"),configFactory.getLong("spark.windowLenght"),configFactory.getLong("spark.slideInterval"),configFactory.getString("spark.checkPointDir"),
         configFactory.getString("mogodb.host"),configFactory.getString("mogodb.database"))
     
     val sparkconf = new SparkConf().setMaster("local[2]").setAppName("HL7ETLSparkProcessor")
                                     
     
     val streamingContext = new StreamingContext(sparkconf, Seconds(5))
     
     //streamingContext.checkpoint(appConfig.checkPointDir)
     
     
     val kafkaParams = Map[String, Object]("bootstrap.servers" -> appConfig.kafkaBrokerAddress,
                    	"key.deserializer" -> classOf[StringDeserializer],
                    	"value.deserializer" -> classOf[StringDeserializer],
                    	"group.id" -> appConfig.kafkaConsumerGroupid,
                    	"auto.offset.reset" -> "latest",
                    	"enable.auto.commit" -> (false: java.lang.Boolean)
                       )

     val topics: Set[String] = appConfig.kafkatopic.split(",").map(_.trim).toSet
     
     
     val hl7MessageStream = KafkaUtils.createDirectStream[String, String](streamingContext,PreferConsistent, Subscribe[String, String](topics, kafkaParams))
     
    
     val dstreamHL7Message = hl7MessageStream.map(msg => IncomingHL7Message.parseIncomingHL7Message(msg.value()));
    
    
     
     val hl7ETL = new HL7ETL()
     
     hl7ETL.transformHL7MessageToJsonAndSaveToDataStore(dstreamHL7Message,false,appConfig)
     
     streamingContext.start();
     
     streamingContext.awaitTermination();
    
     streamingContext.stop(stopSparkContext = true, stopGracefully = true)
     
   }
  
    
}