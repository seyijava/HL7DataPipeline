package com.bigdataconcept.bigdata.healthcare.hl7.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * @author ootun
 *
 */

@SpringBootApplication
public class HL7ServerReciever 
{

	
	
	 public static void main(String[] args) 
	 {
		
		 ConfigurableApplicationContext appContext = SpringApplication.run(HL7ServerReciever.class, args);
		 appContext.registerShutdownHook();
	 }

  
}
