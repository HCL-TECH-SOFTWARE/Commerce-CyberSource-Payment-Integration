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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
import com.ibm.commerce.foundation.logging.LoggingHelper;

/**
 * This helper class has methods to generate cybersource signature, message digest.
 *
 */
public class HCLCyberSourceHelper {

	private static final String CLASS_NAME = "HCLGetHeaderDetailsCmdImpl";
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
	
	public static String getGMTdate() {
	       /*  This Method returns Date in GMT format as defined by RFC7231. */
	       return(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT"))));
	 }
	
	
	/**
	 * 
	 * Signature string generated from parameters is Signed with SecretKey hased
	 * with SHA256 and base64 encoded. Secret Key is Base64 decoded before
	 * signing
	 * 
	 * @param host
	 * @param gmtDateTime
	 * @param digest
	 * @return signatureToken
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws Exception
	 */
	public static String getSignatureToken(String host, String gmtDateTime, String digest,String httpType,String merchantId,String merchantsecretKey,String resource) throws NoSuchAlgorithmException, InvalidKeyException  {
		final String METHOD_NAME = "getSignatureToken";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}

		StringBuilder signatureString = new StringBuilder();
		signatureString.append(HCLCyberSourceConstants.NEXT_LINE);
		signatureString.append(HCLCyberSourceConstants.SIGNATURE_HOST);
		signatureString.append(": ");
		signatureString.append(host);
		signatureString.append(HCLCyberSourceConstants.NEXT_LINE);
		signatureString.append(HCLCyberSourceConstants.SIGNATURE_DATE);
		signatureString.append(": ");
		signatureString.append(gmtDateTime);
		signatureString.append(HCLCyberSourceConstants.NEXT_LINE);
		signatureString.append(HCLCyberSourceConstants.SIGNATURE_REQUEST_TARGET);
		signatureString.append(": ");

		signatureString.append(getRequestTarget(httpType,resource));

		signatureString.append(HCLCyberSourceConstants.NEXT_LINE);
		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.POST)
				|| httpType.equalsIgnoreCase(HCLCyberSourceConstants.PUT)
				|| (httpType.equalsIgnoreCase(HCLCyberSourceConstants.PATCH))) {

			signatureString.append(HCLCyberSourceConstants.SIGNATURE_DIGEST);
			signatureString.append(": ");
			signatureString.append(digest);
			signatureString.append(HCLCyberSourceConstants.NEXT_LINE);
		}

		signatureString.append(HCLCyberSourceConstants.SIGNATURE_V_C_MERCHANTID);
		signatureString.append(": ");
		signatureString.append(merchantId);
		signatureString.delete(0, 1);

		String signatureStr = signatureString.toString();

		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME,
					"CyberSource Signature string befor encryption: " + signatureStr);
		}
		SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(merchantsecretKey),
				HCLCyberSourceConstants.SIGNATURE_HMAC_SHA256);
		Mac aKeyId = Mac.getInstance(HCLCyberSourceConstants.SIGNATURE_HMAC_SHA256);
		aKeyId.init(secretKey);
		aKeyId.update(signatureStr.getBytes());
		byte[] aHeaders = aKeyId.doFinal();
		String base64EncodedSignature = Base64.getEncoder().encodeToString(aHeaders);

		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME,
					"CyberSource Signature string after encryption: " + base64EncodedSignature);
		}
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return base64EncodedSignature;
	}

	/**
	 *
	 * @param requestType
	 *            - GET/PUT/POST
	 * @return request target as per request type.
	 */
	private static String getRequestTarget(String httpType,String resource) {

		String requestTarget;
		switch (httpType) {
		case HCLCyberSourceConstants.POST:
			requestTarget = HCLCyberSourceConstants.POST.toLowerCase() + HCLCyberSourceConstants.SPACE + resource;
			break;
		case HCLCyberSourceConstants.GET:
			requestTarget = HCLCyberSourceConstants.GET.toLowerCase() + HCLCyberSourceConstants.SPACE + resource;
			break;
		case HCLCyberSourceConstants.PUT:
			requestTarget = HCLCyberSourceConstants.PUT.toLowerCase() + HCLCyberSourceConstants.SPACE + resource;
			break;
		case HCLCyberSourceConstants.DELETE:
			requestTarget = HCLCyberSourceConstants.DELETE.toLowerCase() + HCLCyberSourceConstants.SPACE + resource;
			break;
		case HCLCyberSourceConstants.PATCH:
			requestTarget = HCLCyberSourceConstants.PATCH.toLowerCase() + HCLCyberSourceConstants.SPACE + resource;
			break;
		default:
			requestTarget = null;
			break;
		}
		return requestTarget;
	}

	
	/**
	 * @return headers to generate signature.
	 * @throws InvalidKeyException
	 *             - if key is not valid.
	 * @throws NoSuchAlgorithmException
	 *             - if algorith is not available.
	 */
	public static String signatureHeader(String signature,String httpType,String apiKey) throws InvalidKeyException, NoSuchAlgorithmException {
		final String METHOD_NAME = "signatureHeader";
		LOGGER.entering(CLASS_NAME, METHOD_NAME);
		StringBuilder signatureHeader = new StringBuilder();
		signatureHeader.append("keyid=\"" + apiKey + "\"");

		signatureHeader.append(", algorithm=\"HmacSHA256\"");

		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.GET))
			signatureHeader.append(", headers=\"" + getRequestHeaders(httpType) + "\"");
		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.DELETE))
			signatureHeader.append(", headers=\"" + getRequestHeaders(httpType) + "\"");
		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.POST))
			signatureHeader.append(", headers=\"" + getRequestHeaders(httpType) + "\"");
		if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.PATCH))
			signatureHeader.append(", headers=\"" + getRequestHeaders(httpType) + "\"");
		else if (httpType.equalsIgnoreCase(HCLCyberSourceConstants.PUT))
			signatureHeader.append(", headers=\"" + getRequestHeaders(httpType) + "\"");
		/*
		 * Get Value for paramter 'Signature' to be passed to Signature Header
		 */
		signatureHeader.append(", signature=\"" + signature + "\"");
		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME,
					"CyberSource Signature Header value: " + signatureHeader);
		}
		LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		return signatureHeader.toString();
	}

	/**
	 *
	 * @param requestType
	 *            : GET/POST/PUT
	 * @return request header as per request type.
	 */
	private static String getRequestHeaders(String httpType) {
		String requestHeader = null;
		switch (httpType) {
		case HCLCyberSourceConstants.GET:
			requestHeader = "host date (request-target)" + " " + "v-c-merchant-id";
			break;
		case HCLCyberSourceConstants.DELETE:
			requestHeader = "host date (request-target)" + " " + "v-c-merchant-id";
			break;
		case HCLCyberSourceConstants.POST:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		case HCLCyberSourceConstants.PUT:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		case HCLCyberSourceConstants.PATCH:
			requestHeader = "host date (request-target) digest v-c-merchant-id";
			break;
		default:
			return requestHeader;
		}
		return requestHeader;
	}
	
	/**
	 * 
	 * This method return Digest value which is SHA-256 hash of payload that is
	 * BASE64 encoded
	 */
	public static String getDigest(String messageBody) throws NoSuchAlgorithmException, IOException {
		final String METHOD_NAME = "getDigest";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}

		MessageDigest digestString = MessageDigest.getInstance(HCLCyberSourceConstants.SHA_256);

		byte[] digestBytes = digestString.digest(messageBody.getBytes(HCLCyberSourceConstants.ENCODING_UTF_8));

		String bluePrint = Base64.getEncoder().encodeToString(digestBytes);
		bluePrint = HCLCyberSourceConstants.SHA_256 + "=" + bluePrint;

		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME, "CyberSource Message Digest: " + bluePrint);
		}
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}
		return bluePrint;
	}
}
