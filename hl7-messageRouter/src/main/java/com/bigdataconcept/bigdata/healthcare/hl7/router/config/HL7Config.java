package com.bigdataconcept.bigdata.healthcare.hl7.router.config;

import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.camel.component.kafka.KafkaEndpoint;
import org.apache.camel.component.mina2.Mina2Component;
import org.apache.camel.component.mina2.Mina2Configuration;
import org.apache.camel.component.mina2.Mina2Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class HL7Config 
{

	@Value("${hl7ServerLister.host:0.0.0.0}")
	private String host;
	
	@Value("${hl7ServerLister.port:7060}")
	private int port;
	
	
	
	@Value("${kafka.host:127.0.0.1:9092}")
	private String kafkaBrokerHost;
	
	
	
	@Value("${CORE.THREAD.POOL.SIZE:5}")
    private int CORE_THREAD_POOL_SIZE = 5;
    
	
	@Value("${MAX.THREAD.POOL.SIZE:10}")
    private int MAX_THREAD_POOL_SIZE = 10;
    
    
    @Bean
	public HL7MLLPCodec hl7codec()
	{
		HL7MLLPCodec hl7MLLPCodec = new HL7MLLPCodec();
		hl7MLLPCodec.setCharset("iso-8859-1");
		return hl7MLLPCodec;
	}
	
	
	 @Bean(name="hl7listener")
	 public Mina2Endpoint mina2Endpoint() throws Exception
	 {
		   Mina2Component minaComponent = new Mina2Component();
		   Mina2Configuration minaConfiguration = new Mina2Configuration();
		   minaConfiguration.setPort(port);
		   minaConfiguration.setHost(host);
		   minaConfiguration.setSync(true);
		   minaConfiguration.setProtocol("tcp");
		   minaConfiguration.setCodec(hl7codec());
		   minaComponent.setConfiguration(minaConfiguration);
		   Mina2Endpoint minaEnpoint = new Mina2Endpoint("mina2",minaComponent,minaConfiguration);
		   return minaEnpoint;
		 
	}
	 
	 
	 
	   

		@Bean
		public ThreadPoolTaskExecutor executorPool() {
			ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
			pool.setCorePoolSize(5);
			pool.setMaxPoolSize(10);
			pool.setWaitForTasksToCompleteOnShutdown(true);
			return pool;
		}
	

}
