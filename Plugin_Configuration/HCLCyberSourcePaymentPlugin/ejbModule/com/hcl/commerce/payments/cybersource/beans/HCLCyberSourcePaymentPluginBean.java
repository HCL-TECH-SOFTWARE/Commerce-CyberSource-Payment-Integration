package com.hcl.commerce.payments.cybersource.beans;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.hcl.commerce.payments.cybersource.rest.ApiException;
import com.hcl.commerce.payments.cybersource.rest.ApiResponse;
import com.hcl.commerce.payments.cybersource.rest.HCLCyberSourceRestApiHelper;
import com.hcl.commerce.payments.cybersource.rest.HCLRestUtils;
import com.hcl.commerce.payments.cybersource.builder.HCLCyberSourceRequestBuilder;
import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
import com.ibm.commerce.foundation.internal.server.services.registry.StoreConfigurationRegistry;
import com.ibm.commerce.foundation.logging.LoggingHelper;
import com.ibm.commerce.payments.plugin.CommunicationException;
import com.ibm.commerce.payments.plugin.ConfigurationException;
import com.ibm.commerce.payments.plugin.FinancialTransaction;
import com.ibm.commerce.payments.plugin.FunctionNotSupportedException;
import com.ibm.commerce.payments.plugin.InvalidDataException;
import com.ibm.commerce.payments.plugin.InvalidPaymentInstructionException;
import com.ibm.commerce.payments.plugin.PaymentInstruction;
import com.ibm.commerce.payments.plugin.Plugin;
import com.ibm.commerce.payments.plugin.PluginContext;
import com.ibm.commerce.payments.plugin.PluginException;
import com.ibm.commerce.payments.plugincontroller.beans.PluginControllerJDBCHelperBean;

import Model.CreatePaymentRequest;
import Model.PtsV2PaymentsPost201Response;
import Model.PtsV2PaymentsPost201ResponseErrorInformation;

/**
 * Session Bean implementation class HCLCyberSourcePaymentPluginBean
 */
@Stateless(mappedName = "HCLCyberSourcePaymentPlugin")
@Local(Plugin.class)
@LocalBean
public class HCLCyberSourcePaymentPluginBean implements Plugin {

	private static final String CLASS_NAME = "HCLCyberSourcePaymentPluginBean";
	private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
	private static final String resource_bundle = "com.hcl.commerce.payments.cybersource.messages.cybersourcePluginMessage";
	private static PluginControllerJDBCHelperBean jdbcHelper = null;

	/**
	 * Authorize the amount from CyberSource payment service.
	 */
	@Override
	public FinancialTransaction approve(PluginContext pluginCtx, FinancialTransaction financialTransaction,
			boolean arg2) throws CommunicationException, PluginException {

		final String METHOD_NAME = "approve";
		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.entering(CLASS_NAME, METHOD_NAME);
		}
		Integer storeId = Integer.valueOf(financialTransaction.getPayment().getPaymentInstruction().getStore());

		StoreConfigurationRegistry storeconfReg = StoreConfigurationRegistry.getSingleton();

		CreatePaymentRequest paymentRequest = HCLCyberSourceRequestBuilder.composePaymentRequest(financialTransaction,
				storeconfReg.getValue(storeId, HCLCyberSourceConstants.HCL_SOULTION_ID));
		String request = new Gson().toJson(paymentRequest);

		try {
			// Build the header parameter map for request
			Map<String, String> headerMap = HCLCyberSourceRestApiHelper.generateCyberSourceHeader(request, storeId,
					HCLCyberSourceConstants.RESOURCE_PAYMENT, HCLCyberSourceConstants.POST);
			// Build Payment URL
			String paymentURL = HCLCyberSourceConstants.HTTPS + headerMap.get(HCLCyberSourceConstants.HOST)
					+ HCLCyberSourceConstants.RESOURCE_PAYMENT;
			// Call CyberSource Service
			ApiResponse apiResponse = HCLRestUtils.callCyberSourcePostAPI(paymentURL, request, headerMap);

			// setting the JSON response to PtsV2PaymentsPost201Response class
			PtsV2PaymentsPost201Response response = new Gson().fromJson(apiResponse.getMessage(),
					PtsV2PaymentsPost201Response.class);
			if (StringUtils.containsIgnoreCase(HCLCyberSourceConstants.RES_SUCCESS, response.getStatus())) {
				financialTransaction.setResponseCode(String.valueOf(apiResponse.getStatusCode()));
				String reasonCode = response.getStatus().length() > HCLCyberSourceConstants.REASON_FIELD_LENGTH
						? response.getStatus().substring(0, response.getStatus().length()) : response.getStatus();
				financialTransaction.setReasonCode(reasonCode);
				financialTransaction.setReferenceNumber(response.getId());
			} else if (HCLCyberSourceConstants.RES_DECLINED.equalsIgnoreCase(response.getStatus())
					|| HCLCyberSourceConstants.RES_AUTHORIZED_PENDING_REVIEW.equalsIgnoreCase(response.getStatus())) {
				PtsV2PaymentsPost201ResponseErrorInformation errorInformation = response.getErrorInformation();
				LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME,
						"<<CyberSource Response Error........>>:" + errorInformation.getMessage());

				handleException(METHOD_NAME, errorInformation.getMessage());
			} else {
				handleException(METHOD_NAME, "API_ERROR");
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {

			LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD_NAME, "<<CyberSource Signature Generation Error........>>");
			handleException(METHOD_NAME, "API_ERROR");
			e.printStackTrace();
		} catch (ApiException e) {
			e.printStackTrace();
			handleException(METHOD_NAME, String.valueOf(e.getCode()));

		}

		if (LoggingHelper.isEntryExitTraceEnabled(LOGGER)) {
			LOGGER.exiting(CLASS_NAME, METHOD_NAME);
		}

		return financialTransaction;
	}

	/**
	 * Handle the exception
	 * 
	 * @param methodName
	 * @param key
	 * @throws PluginException
	 */
	private void handleException(String methodName, String key) throws PluginException {

		PluginException pluginException = new PluginException();
		pluginException.setClassSource(CLASS_NAME);
		pluginException.setMethodSource(methodName);
		pluginException.setResourceBundleName(resource_bundle);
		pluginException.setMessageKey(key);
		throw pluginException;
	}

	@Override
	public FinancialTransaction approveAndDeposit(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws FunctionNotSupportedException, PluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkHealth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void checkPaymentInstruction(PluginContext arg0, PaymentInstruction arg1)
			throws InvalidPaymentInstructionException, ConfigurationException, InvalidDataException {
		// TODO Auto-generated method stub

	}

	@Override
	public FinancialTransaction credit(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws PluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialTransaction deposit(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws PluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage(PluginContext arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialTransaction reverseApproval(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws CommunicationException, PluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialTransaction reverseCredit(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws InvalidPaymentInstructionException, FunctionNotSupportedException, InvalidDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialTransaction reverseDeposit(PluginContext arg0, FinancialTransaction arg1, boolean arg2)
			throws FunctionNotSupportedException, PluginException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validatePaymentInstruction(PluginContext arg0, PaymentInstruction arg1)
			throws InvalidPaymentInstructionException, FunctionNotSupportedException {
		// TODO Auto-generated method stub

	}

}
