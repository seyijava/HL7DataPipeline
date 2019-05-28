package com.bigdataconcept.bigdata.healthcare.hl7.route.inbound;

import java.io.Serializable;

/**
 * 
 * @author otun Oluwaseyi.
 * HL7Message 
 *
 */

public class HL7Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String body;
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	private String messageType;
	
	
	
	HL7Message()
	{}
	
	public HL7Message(String body,String messageType)
	{
		this.messageType = messageType;
		this.body = body;
	}
}
