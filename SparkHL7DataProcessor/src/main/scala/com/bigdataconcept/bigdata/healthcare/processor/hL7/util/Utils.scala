package com.bigdataconcept.bigdata.healthcare.processor.hL7.util

case class AppConfig(kafkaBrokerAddress:  String, kafkatopic: String, kafkaConsumerGroupid: String,windowLenght: Long, slideInterval: Long, checkPointDir: String ,
    mongoDBHost: String , mogoDBCollection: String)
extends Serializable


case class HL7JsonMessage(message: String, messagetype: String, createddate:java.util.Date)  extends Serializable