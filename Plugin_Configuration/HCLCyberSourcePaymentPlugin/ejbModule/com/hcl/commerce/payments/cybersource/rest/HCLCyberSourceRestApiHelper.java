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
package com.hcl.commerce.payments.cybersource.rest;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
import com.ibm.commerce.foundation.internal.server.services.registry.StoreConfigurationRegistry;
import com.ibm.commerce.foundation.logging.LoggingHelper;
/**
 * This helper class is used to generate CyberSource header for the request.
 *
 */
public class HCLCyberSourceRestApiHelper {

	
	/** The constant CLASS_NAME. */
	public static final String CLASSNAME = HCLCyberSourceRestApiHelper.class.getName();
	/** The constant LOGGER. */
	private static final Logger LOGGER = Logger .getLogger(HCLCyberSourceRestApiHelper.class.getName());
	
	/**
	 * This method create Header Map which used in the Cybersource Request
	 * @param request
	 * @param storeId
	 * @param resource
	 * @param httpType
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	public static Map<String, String> generateCyberSourceHeader(String request,Integer storeId,String resource,String httpType) throws NoSuchAlgorithmException, IOException, InvalidKeyException{
		String METHODNAME = "generateCyberSourceHeader";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASSNAME, METHODNAME);
		}
		Map<String, String> headerParams = new HashMap<String, String>();
		StoreConfigurationRegistry storeconfReg = StoreConfigurationRegistry.getSingleton();
		String	merchantId = storeconfReg.getValue(storeId, HCLCyberSourceConstants.CYBERSOURCE_MERCHANT_ID);
		String merchantsecretKey = storeconfReg.getValue(storeId, HCLCyberSourceConstants.CYBERSOURCE_SECRET_KEY);
		String merchantApiKey = storeconfReg.getValue(storeId, HCLCyberSourceConstants.CYBERSOURCE_API_KEY);
		String host = storeconfReg.getValue(storeId, HCLCyberSourceConstants.CYBERSOURCE_API_URL);
		String gmtDateTime = HCLCyberSourceHelper.getGMTdate();
		String digest = HCLCyberSourceHelper.getDigest(request);
		String signatureToken = HCLCyberSourceHelper.getSignatureToken(host, gmtDateTime, digest,httpType, merchantId, merchantsecretKey, resource);
		headerParams.put(HCLCyberSourceConstants.CONTENT_TYPE, HCLCyberSourceConstants.CONTENT_TYPE_JSON);
		headerParams.put(HCLCyberSourceConstants.V_C_MERCHANTID, merchantId);
		headerParams.put(HCLCyberSourceConstants.SIGNATURE_DATE, gmtDateTime);
		headerParams.put(HCLCyberSourceConstants.HOST, host);
		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.POST)
				|| httpType.equalsIgnoreCase(HCLCyberSourceConstants.PUT)
				|| (httpType.equalsIgnoreCase(HCLCyberSourceConstants.PATCH))) {
			headerParams.put(HCLCyberSourceConstants.DIGEST, digest);
		}
		
		headerParams.put(HCLCyberSourceConstants.SIGNATURE, HCLCyberSourceHelper.signatureHeader(signatureToken,httpType, merchantApiKey));
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASSNAME, METHODNAME);
		}
		 LOGGER.logp(Level.FINEST, CLASSNAME, METHODNAME, "<<CyberSource Service header parameter........>>"+headerParams);
		return headerParams;
	}
	

}
