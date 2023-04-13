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


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
/**
 * This RestUtils use the HTTPURLConnection to call the CyberSource services. 
 *
 */
public class HCLRestUtils {

	/** The constant CLASS_NAME. */
	public static final String CLASSNAME = HCLRestUtils.class.getName();
	/** The constant LOGGER. */
	private static final Logger LOGGER = Logger .getLogger(HCLRestUtils.class.getName());
	
	/**
	 * This method call the CyberSource Service.
	 * @param url
	 * @param request
	 * @param header
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 */
	public static ApiResponse callCyberSourcePostAPI(String url, String request,Map<String,String> header) throws IOException, ApiException {
		final String METHOD_NAME = "callCyberSourceAPI";
		
		ApiResponse apiResponse=null;
	    
	    int responseCode = 0;
	    String output = null;
	    
	    URL apiURL = new URL(url);
	    //create connection
	    HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
	    connection.setRequestMethod(HCLCyberSourceConstants.POST);
	    LOGGER.logp(Level.FINEST, CLASSNAME, METHOD_NAME, "<<CyberSource Service Request........>>"+request);
	
	    for (Map.Entry<String, String> entry : header.entrySet()) {
	  	  connection.setRequestProperty(entry.getKey(), entry.getValue());
	  }
	   
	   
	    connection.setDoOutput(true);
	    connection.setUseCaches(false);
	    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	      wr.write(request.getBytes(HCLCyberSourceConstants.ENCODING_UTF_8));
	      wr.flush();
	      wr.close();
	      
	    LOGGER.logp(Level.INFO, CLASSNAME, METHOD_NAME, "<<CyberSource Service response code........>>"+connection.getResponseCode());
	    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK && connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
	    	ApiException apiException=new ApiException(connection.getResponseCode());
	    	if (null !=connection.getErrorStream()) {
				String errorOutput = readResponse(connection.getErrorStream());
				LOGGER.logp(Level.WARNING, CLASSNAME, METHOD_NAME, "<<CyberSource Service error response........>>"+errorOutput);
				apiException.setResponseBody(errorOutput);
	    	}
	    	
	    	throw apiException;
	    	
		} else {
			output = readResponse(connection.getInputStream());
			LOGGER.logp(Level.INFO, CLASSNAME, METHOD_NAME, "<<CyberSource Service response........>>"+output);
			 apiResponse = new ApiResponse(connection.getResponseCode(),output);

		}
    
	    return apiResponse;
	}
	
	/**
	 * Read the rest response from CyberSource service
	 * 
	 * @param inputStream
	 * @return responseString
	 * @throws IOException
	 */
	private static String readResponse(InputStream inputStream) throws IOException {
		final String METHODNAME = "readResponse";
		LOGGER.entering(CLASSNAME, METHODNAME, new Object[] { inputStream });
		String output = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		bufferedReader.close();
		if (null != response) {
			output = response.toString();

			LOGGER.logp(Level.FINEST, CLASSNAME, METHODNAME, "Response:" + output);
		}
		LOGGER.exiting(CLASSNAME, METHODNAME);
		return output;
	}
	
	
}
