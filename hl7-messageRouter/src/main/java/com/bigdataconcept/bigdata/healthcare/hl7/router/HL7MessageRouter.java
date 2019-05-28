package com.bigdataconcept.bigdata.healthcare.hl7.router;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.log4j.Logger;

/** 
 * 
 * @author Oluwaseyi Otun
 * 
 * HL7MessageRouter implements Kafka message Sender implementing Runnable 
 * using Task Executor to dispatch the message to Kafka as to handle message
 * ingest in multithreading manner
 *
 * 
 */
public class HL7MessageRouter<M> implements Runnable {

	Logger logger = Logger.getLogger(HL7MessageRouter.class);
	private final M m;
	private final String topic;
	private final Producer<String, M> producer;
	
	public HL7MessageRouter(final M m, final String topic, final Producer<String, M> producer) {
		this.m = m;
		this.topic = topic;
		this.producer = producer;
	
	}

	public void run() {
		try 
		{
			
			final ProducerRecord<String, M> record = new ProducerRecord<String, M>(topic, m);
		
			logger.info(String.format("Thread [%s] Message [%s] sending  HL7 message to Kafka Ingest Topic for Datapipeline and Analystics [%s]\n", Thread.currentThread().getId(),m,topic));
				 
			final Future<RecordMetadata> future = producer.send(record);
			 displayRecordMetaData(record, future);
		    logger.info(String.format("Thread [%s] Message HL7 Sent message to Kafka Ingest Topic\n", Thread.currentThread().getId()));
	   		 
		} catch (Exception e) {
			logger.error("problem sending record to producer", e);
		}
	}
	
	 private void displayRecordMetaData(final ProducerRecord<String, M> record,final Future<RecordMetadata> future)  throws InterruptedException, ExecutionException
	 {
			 																				
		 final RecordMetadata recordMetadata = future.get();
		 logger.info(String.format("\n\t\t\tkey=%s, value=%s " +
				 "\n\t\t\tsent to topic=%s part=%d off=%d at time=%s",
				 record.key(),
				 record.value(),
				 recordMetadata.topic(),
				 recordMetadata.partition(),
				 recordMetadata.offset(),
				 new Date(recordMetadata.timestamp())
	));
  }

}
