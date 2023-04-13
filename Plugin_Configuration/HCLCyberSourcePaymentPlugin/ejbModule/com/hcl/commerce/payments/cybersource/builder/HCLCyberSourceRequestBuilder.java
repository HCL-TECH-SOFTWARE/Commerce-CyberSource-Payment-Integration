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

package com.hcl.commerce.payments.cybersource.builder;

import com.hcl.commerce.payments.cybersource.constants.HCLCyberSourceConstants;
import com.ibm.commerce.payments.plugin.ExtendedData;
import com.ibm.commerce.payments.plugin.FinancialTransaction;

import Model.CreatePaymentRequest;
import Model.Ptsv2paymentsClientReferenceInformation;
import Model.Ptsv2paymentsClientReferenceInformationPartner;
import Model.Ptsv2paymentsOrderInformation;
import Model.Ptsv2paymentsOrderInformationAmountDetails;
import Model.Ptsv2paymentsOrderInformationBillTo;
import Model.Ptsv2paymentsPaymentInformation;
import Model.Ptsv2paymentsPaymentInformationInstrumentIdentifier;
import Model.Ptsv2paymentsPaymentInformationTokenizedCard;
/**
 * This class is used to compose Payment Request.
 *
 */
public class HCLCyberSourceRequestBuilder {

	/**
	 * Compose Payment Request 
	 * @param financialTransaction
	 * @param solutionId
	 * @return
	 */
	public static CreatePaymentRequest composePaymentRequest(FinancialTransaction financialTransaction,String solutionId) {
		CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();

		createPaymentRequest.setClientReferenceInformation(composeClientRequestInformation(financialTransaction.getPayment().getPaymentInstruction().getOrderId(),solutionId));
		createPaymentRequest.setOrderInformation(composeOrderInformation(financialTransaction));
		createPaymentRequest.paymentInformation(composePaymentInformation(financialTransaction.getPayment().getPaymentInstruction().getExtendedData()));
		
		return createPaymentRequest;
	}
	
	/**
	 * Compose payment information
	 * @param extData
	 * @return
	 */
	private static Ptsv2paymentsPaymentInformation composePaymentInformation(ExtendedData extData) {
		Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
		Ptsv2paymentsPaymentInformationInstrumentIdentifier identifier = new Ptsv2paymentsPaymentInformationInstrumentIdentifier();
		identifier.setId(extData.getString(HCLCyberSourceConstants.INTRUMENT_IDENTIIFIER_ID));
		paymentInformation.setInstrumentIdentifier(identifier);
		
		Ptsv2paymentsPaymentInformationTokenizedCard tokenizeCard = new Ptsv2paymentsPaymentInformationTokenizedCard();
		tokenizeCard.setExpirationMonth(extData.getString(HCLCyberSourceConstants.CARD_EXPIRE_MONTH));
		tokenizeCard.setExpirationYear(extData.getString(HCLCyberSourceConstants.CARD_EXPIRE_YEAR));
		tokenizeCard.securityCode(extData.getString(HCLCyberSourceConstants.CARD_CVV));
		paymentInformation.setTokenizedCard(tokenizeCard);
		return paymentInformation;
	}

	
	/**
	 * Compose order billing information
	 * @param extData
	 * @return
	 */
	private static Ptsv2paymentsOrderInformationBillTo composeOrderInformationBillTo(ExtendedData extData){
		Ptsv2paymentsOrderInformationBillTo orderInformationBillTo = new Ptsv2paymentsOrderInformationBillTo();
		orderInformationBillTo.firstName(extData.getString(HCLCyberSourceConstants.BILL_TO_FIRSTANME));
		orderInformationBillTo.lastName(extData.getString(HCLCyberSourceConstants.BILL_TO_LASTNAME));
		orderInformationBillTo.address1(extData.getString(HCLCyberSourceConstants.BILL_TO_STREET1));
		orderInformationBillTo.locality(extData.getString(HCLCyberSourceConstants.BILL_TO_CITY));
		orderInformationBillTo.administrativeArea(extData.getString(HCLCyberSourceConstants.BILL_TO_STATE));
		orderInformationBillTo.postalCode(extData.getString(HCLCyberSourceConstants.BILL_TO_POSTALCODE));
		orderInformationBillTo.country(extData.getString(HCLCyberSourceConstants.BILL_TO_COUNTRY));
		String emailId =(null != extData.getString(HCLCyberSourceConstants.BILL_TO_EMAIL))? extData.getString(HCLCyberSourceConstants.BILL_TO_EMAIL):HCLCyberSourceConstants.BILL_EMAIL_EMPTY;
		orderInformationBillTo.setEmail(emailId);
		return orderInformationBillTo;
	}
	
	/**
	 * Compose order information
	 * @param financialTransaction
	 * @return
	 */
	private static Ptsv2paymentsOrderInformation composeOrderInformation(FinancialTransaction financialTransaction){
		Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
		Ptsv2paymentsOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
		orderInformationAmountDetails.totalAmount(financialTransaction.getPayment().getPaymentInstruction().getAmount().toString());
		orderInformationAmountDetails.currency(financialTransaction.getPayment().getPaymentInstruction().getCurrency());
		orderInformation.amountDetails(orderInformationAmountDetails);
		ExtendedData extData = financialTransaction.getPayment().getPaymentInstruction().getExtendedData();
		orderInformation.setBillTo(composeOrderInformationBillTo(extData));
		return orderInformation;
	}

	/**
	 * Compose client request information
	 * @param orderId
	 * @param solutionId
	 * @return
	 */
	private static Ptsv2paymentsClientReferenceInformation composeClientRequestInformation(String orderId,String solutionId){
		
		Ptsv2paymentsClientReferenceInformation clientReferenceInformation=new Ptsv2paymentsClientReferenceInformation();
		Ptsv2paymentsClientReferenceInformationPartner partner = new Ptsv2paymentsClientReferenceInformationPartner();
		partner.setSolutionId(solutionId);
		clientReferenceInformation.setPartner(partner);
		clientReferenceInformation.setCode(orderId);
		return clientReferenceInformation;
	}
	
}
