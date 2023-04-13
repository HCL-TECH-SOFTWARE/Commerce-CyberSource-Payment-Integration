/**
*==================================================
Copyright [2022] [HCL America, Inc.]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*==================================================
**/
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
