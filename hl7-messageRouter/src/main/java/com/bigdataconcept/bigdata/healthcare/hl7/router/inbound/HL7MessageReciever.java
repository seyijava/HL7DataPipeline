package com.bigdataconcept.bigdata.healthcare.hl7.router.inbound;

import javax.annotation.PreDestroy;

import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.bigdataconcept.bigdata.healthcare.hl7.router.HL7MessageRouter;
import com.bigdataconcept.bigdata.healthcare.hl7.router.KafkaProducerFactory;
import com.google.gson.Gson;

import ca.uhn.hl7v2.model.Message;


/**
 * 
 * @author oluwasey Otun 
 *
 *  HL7MessageReciever message Recivier is TCP server that listening to incoming message
 *  and route to kafka message brovker 
 *
 */

@Component("hl7MessageReciever")
public class HL7MessageReciever {

	
	Logger logger = Logger.getLogger(HL7MessageReciever.class);
	
	private static final String MSG_TYPE_HEADER_KEY = "CamelHL7MessageType";
	
	private static final String TOPIC_NAME = "HL7-INGEST";
	final Base64 base64 = new Base64();
	
	@Autowired
	ThreadPoolTaskExecutor executorPool;
	
	
	
	@Autowired
	KafkaProducerFactory kafkaProducerFactory;
	
	
	  public Message recieveMessage(Exchange incoming) throws Exception {
		  
		   Message in = (Message)incoming.getMessage().getBody();
		   String headerInfo = (String)incoming.getMessage().getHeader(MSG_TYPE_HEADER_KEY);
		   String hl7rawMessage = in.toString();
		   logger.info(String.format("Thread [%s] Message Type[%s] Recieving HL7 message from HL7 Integration Engine [%s]\n", Thread.currentThread().getId(),headerInfo,hl7rawMessage));
		   String encodedHL7msg  = new String(base64.encodeBase64(hl7rawMessage.getBytes()));
		   HL7Message hl7Message = new HL7Message(encodedHL7msg, headerInfo);
		   String payload = new Gson().toJson(hl7Message);
		   HL7MessageRouter<String> messageSender = new HL7MessageRouter<String>(payload, TOPIC_NAME, kafkaProducerFactory.createNewProducer());
		 
		   executorPool.submit(messageSender);
	       return in.generateACK();

	    }
	  
	  
	    @PreDestroy
		public void cleanup()
		{
			executorPool.shutdown();
		}
}
