package com.bigdataconcept.bigdata.healthcare.processor.hL7.model

import java.util.Base64
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger
import java.sql.Date;
import com.google.gson.Gson

object IncomingHL7Message extends  Serializable    
{

   val log = Logger.getLogger(getClass.getName) 
   
   case class HL7Message(body: String,messageType: String) extends  Serializable    

   
   
  def parseIncomingHL7Message(message: String) : HL7Message =
  {
   
   
    
    var incomingHL7Data = new Gson().fromJson(message, classOf[HL7Message]);
    var decodedmsg = Base64.getDecoder().decode(incomingHL7Data.body);
    val hl7Message = HL7Message(new String(decodedmsg),incomingHL7Data.messageType);
    log.info(String.format("Incoming HL7Message [%s]\n\n\n\n", hl7Message.body));
    return hl7Message
  }
}