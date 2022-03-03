package com.hcl.commerce.payments.cybersource.messages;

import com.ibm.commerce.ras.ECMessage;
import com.ibm.commerce.ras.ECMessageSeverity;
import com.ibm.commerce.ras.ECMessageType;

public class CyberSourceMessages {
	private static final String resource_bundle="com.hcl.commerce.payments.cybersource.messages.cybersourcePluginMessage";
	
	
	public static final ECMessage ERROR_KEY_API = new ECMessage(ECMessageSeverity.ERROR, ECMessageType.USER,
			CyberSourceMessagesKeys.ERROR_KEY_API, resource_bundle);
	public static final ECMessage ERROR_SINGNATURE_GENERATION = new ECMessage(ECMessageSeverity.ERROR, ECMessageType.USER,
			CyberSourceMessagesKeys.ERROR_SINGNATURE_GENERATION, resource_bundle);
	
}
