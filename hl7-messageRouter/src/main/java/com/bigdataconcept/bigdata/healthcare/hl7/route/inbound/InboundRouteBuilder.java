package com.bigdataconcept.bigdata.healthcare.hl7.route.inbound;

import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InboundRouteBuilder extends SpringRouteBuilder
{
	
   
 private static final Logger logger = LoggerFactory.getLogger(InboundRouteBuilder.class);

   @Override
    public void configure() throws Exception {

        from("hl7listener")
          .routeId("hl7Messagelistener").startupOrder(1)
            .startupOrder(997)
              .unmarshal()
               .hl7(true)
                .to("bean:hl7MessageReciever?method=recieveMessage").end();
	
    }
}