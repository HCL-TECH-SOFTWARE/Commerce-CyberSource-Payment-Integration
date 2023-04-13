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
package com.hcl.commerce.payments.cybersource.commands;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import com.hcl.commerce.payments.cybersource.messages.CyberSourceMessages;
import com.hcl.commerce.payments.cybersource.rest.ApiException;
import com.hcl.commerce.payments.cybersource.rest.ApiResponse;
import com.hcl.commerce.payments.cybersource.rest.HCLCyberSourceRestApiHelper;
import com.hcl.commerce.payments.cybersource.rest.HCLRestUtils;
import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
import com.ibm.commerce.command.ControllerCommandImpl;
import com.ibm.commerce.datatype.TypedProperty;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.exception.ECException;
import com.ibm.commerce.foundation.internal.server.services.registry.StoreConfigurationRegistry;
import com.ibm.commerce.foundation.logging.LoggingHelper;

import Model.FlexV1KeysPost200Response;
import Model.GeneratePublicKeyRequest;

/**
 * This class used to get the Key from CyberSource from service.
 *
 */
public class HCLCyberSourceGenerateKeyCmdImpl extends ControllerCommandImpl implements HCLCyberSourceGenerateKeyCmd {

	private static final String CLASS_NAME = "HCLCyberSourceGenerateKeyCmdImpl";
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

	private String encryptionType;
	private String origin;

	@Override
	public void setRequestProperties(TypedProperty reqProperties) throws ECException {
		final String METHOD_NAME = "setRequestProperties";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		encryptionType = reqProperties.getString(HCLCyberSourceConstants.KEY_ENCRYPTION_TYPE,
				HCLCyberSourceConstants.DEFAULT_ENCRYPTION_TYPE);
		origin = reqProperties.getString(HCLCyberSourceConstants.TARGET_ORIGIN, HCLCyberSourceConstants.EMPTY);
		super.setRequestProperties(reqProperties);

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}

	/**
	 * This method is used to CyberSource generate Key API. 
	 * Before call the CyberSource API we have to generate the Header parameter.
	 * 
	 */
	@Override
	public void performExecute() throws ECException {
		final String METHOD_NAME = "performExecute";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		// compose public key request
		GeneratePublicKeyRequest generateKeyRequet = composeGeneratePublicKeyRequest();
		String request = new Gson().toJson(generateKeyRequet);
		try {
			//Build the header parameter map for request
			Map<String, String> headerMap = HCLCyberSourceRestApiHelper.generateCyberSourceHeader(request, getStoreId(),
					HCLCyberSourceConstants.RESOURCE_GENERATE_KEY, HCLCyberSourceConstants.POST);
			String flexGenerateKeyURL = HCLCyberSourceConstants.HTTPS + headerMap.get(HCLCyberSourceConstants.HOST)
					+ HCLCyberSourceConstants.RESOURCE_GENERATE_KEY;
			ApiResponse apiResponse = HCLRestUtils.callCyberSourcePostAPI(flexGenerateKeyURL, request, headerMap);
			
			//Converting JSON response into  FlexV1KeysPost200Response Class object
			FlexV1KeysPost200Response response = new Gson().fromJson(apiResponse.getMessage(),
					FlexV1KeysPost200Response.class);

			TypedProperty respProperty = new TypedProperty();
			StoreConfigurationRegistry storeconfReg = StoreConfigurationRegistry.getSingleton();
			String	apiURL = storeconfReg.getValue(getStoreId(), HCLCyberSourceConstants.CYBERSOURCE_API_URL);
			
			respProperty.put(HCLCyberSourceConstants.URL, apiURL);
			respProperty.put(HCLCyberSourceConstants.RES_FLEX_KEYID, response.getKeyId());
			super.setResponseProperties(respProperty);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {

			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME, "<<CyberSource Signature Generation Error........>>");
			e.printStackTrace();
			throw new ECApplicationException(CyberSourceMessages.ERROR_SINGNATURE_GENERATION, CLASS_NAME, METHOD_NAME);
		} catch (ApiException e) {
			e.printStackTrace();
			throw new ECApplicationException(CyberSourceMessages.ERROR_KEY_API, CLASS_NAME, METHOD_NAME);

		}

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
	}

	/**
	 * Compose the CyberSource Request.
	 * @return
	 */
	private GeneratePublicKeyRequest composeGeneratePublicKeyRequest() {
		final String METHOD_NAME = "composeGeneratePublicKeyRequest";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		GeneratePublicKeyRequest request = new GeneratePublicKeyRequest();
		request.setEncryptionType(encryptionType);
		if (StringUtils.isNotBlank(origin)) {
			request.setTargetOrigin(origin);
		}

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return request;
	}
}
