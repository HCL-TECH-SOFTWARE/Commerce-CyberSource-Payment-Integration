package com.hcl.commerce.edp.commands;

import java.util.HashMap;
import java.util.logging.Logger;

import com.ibm.commerce.edp.commands.PIAddCmdImpl;
import com.ibm.commerce.exception.ECApplicationException;
import com.ibm.commerce.user.objects.AddressAccessBean;
/**
 * 
 * This class extended to add the email field in extended data
 *
 */
public class HCLCyberSourcePIAddCmdImpl extends PIAddCmdImpl {
	/** The constant CLASS_NAME. */
	public static final String CLASSNAME = HCLCyberSourcePIAddCmdImpl.class.getName();
	/** The constant LOGGER. */
	private static final Logger LOGGER = Logger .getLogger(HCLCyberSourcePIAddCmdImpl.class.getName());
	/**
	 * Add email for cybersource auhtorization request
	 */
	@Override
	protected void setBillingAddress(String billingAddressId, HashMap edpPIExtendedData) throws ECApplicationException {
		
		 AddressAccessBean abAddress = null;
	        if(billingAddressId != null && billingAddressId.trim().length() != 0)
	        {
	            abAddress = new AddressAccessBean();
	            abAddress.setInitKey_addressId(billingAddressId);
	            try
	            {
	                abAddress.instantiateEntity();
	                if(abAddress.getFirstName() != null)
	                    edpPIExtendedData.put("billto_firstname", abAddress.getFirstName());
	                if(abAddress.getMiddleName() != null)
	                    edpPIExtendedData.put("billto_middlename", abAddress.getMiddleName());
	                if(abAddress.getLastName() != null)
	                    edpPIExtendedData.put("billto_lastname", abAddress.getLastName());
	                if(abAddress.getAddress1() != null)
	                    edpPIExtendedData.put("billto_address1", abAddress.getAddress1());
	                if(abAddress.getAddress2() != null)
	                    edpPIExtendedData.put("billto_address2", abAddress.getAddress2());
	                if(abAddress.getAddress3() != null)
	                    edpPIExtendedData.put("billto_address3", abAddress.getAddress3());
	                if(abAddress.getCity() != null)
	                    edpPIExtendedData.put("billto_city", abAddress.getCity());
	                if(abAddress.getState() != null)
	                    edpPIExtendedData.put("billto_stateprovince", abAddress.getState());
	                if(abAddress.getCountry() != null)
	                    edpPIExtendedData.put("billto_country", abAddress.getCountry());
	                if(abAddress.getZipCode() != null)
	                    edpPIExtendedData.put("billto_zipcode", abAddress.getZipCode());
	                if(abAddress.getPhone1() != null)
	                    edpPIExtendedData.put("billto_phone_number", abAddress.getPhone1());
	                if(abAddress.getPhone1Type() != null)
	                    edpPIExtendedData.put("billto_telephone_type", abAddress.getPhone1Type());
	                if(abAddress.getEmail1() != null)
	                    edpPIExtendedData.put("billto_email", abAddress.getEmail1());
	            }
	            catch(Exception e)
	            {
	                throw new ECApplicationException(e);
	            }
	        }
	}
	
}
