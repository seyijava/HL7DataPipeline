package com.bigdataconcept.bigdata.healthcare.hl7.router;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author ootun
 *
 */


@Configuration
@SpringBootApplication
@ComponentScan(basePackages={"com.bigdataconcept.bigdata.healthcare.hl7.router", 
		"com.bigdataconcept.bigdata.healthcare.hl7.router.config","com.bigdataconcept.bigdata.healthcare.hl7.router.inbound"})
public class HL7ServerReciever 
{

	
	
	 public static void main(String[] args) 
	 {
		
		 ConfigurableApplicationContext appContext = SpringApplication.run(HL7ServerReciever.class, args);
		 appContext.registerShutdownHook();
		 
		 
	 }

  
}
