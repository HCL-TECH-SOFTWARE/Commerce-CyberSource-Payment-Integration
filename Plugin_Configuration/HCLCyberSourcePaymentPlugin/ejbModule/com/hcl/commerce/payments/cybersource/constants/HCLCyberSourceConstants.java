package com.hcl.commerce.payments.cybersource.constants;

public class HCLCyberSourceConstants {
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String PATCH = "PATCH";
	public static final String DELETE = "DELETE";
	public static final String SPACE = " ";
	public static final String NEXT_LINE = "\n";
	public static final String V_C_MERCHANTID = "v-c-merchant-id";
	public static final String DATE = "Date";
	public static final String HTTPS = "https://";
	
	public static final String HOST = "Host";
	public static final String DIGEST = "Digest";
	public static final String SIGNATURE = "Signature";
	public static String SHA_256 = "SHA-256";
	public static String ENCODING_UTF_8 = "UTF-8";
	
	public static final String SIGNATURE_HOST = "host";
	public static final String SIGNATURE_DATE = "date";
	public static final String SIGNATURE_REQUEST_TARGET = "(request-target)";
	public static final String SIGNATURE_DIGEST = "digest";
	public static final String SIGNATURE_V_C_MERCHANTID = "v-c-merchant-id";
	public static final String SIGNATURE_HMAC_SHA256 = "HmacSHA256";
	
	public static final String RESPONSE_HOST = "host";
	public static final String RESPONSE_DATE = "date";
	public static final String RESPONSE_DIGEST = "digest";
	public static final String RESPONSE_MERCHANTID = "merchantId";
	public static final String RESPONSE_API_KEY = "apiKey";
	public static final String RESPONSE_SIGNATURE = "signture";
	public static final String RESPONSE_TOKEN = "token";
	
	public static final String CYBERSOURCE_MERCHANT_ID = "CyberSoure_merchant_id";
	public static final String CYBERSOURCE_SECRET_KEY = "CyberSource_secret_key";
	public static final String CYBERSOURCE_API_KEY = "CyberSource_api_key";
	public static final String CYBERSOURCE_API_URL = "CyberSource_api_hostname";
	
	public static final String DEFAULT_ENCRYPTION_TYPE = "None";
	public static final String KEY_ENCRYPTION_TYPE = "encryptionType";
	public static final String TARGET_ORIGIN = "targetOrigin";
	public static final String EMPTY = "";
	public static final String RESOURCE_GENERATE_KEY = "/flex/v1/keys";
	public static final String RESOURCE_PAYMENT = "/pts/v2/payments";
	
	public static final int REASON_FIELD_LENGTH = 25;
	
	public static final String CONTENT_TYPE="Content-Type";
	public static final String CONTENT_TYPE_JSON="application/json";
	
	public static final String RES_FLEX_KEYID = "keyId";
	
	public static final String BILL_TO_FIRSTANME="billto_firstname";
	public static final String BILL_TO_LASTNAME="billto_lastname";
	public static final String BILL_TO_STREET1="billto_address1";
	public static final String BILL_TO_CITY="billto_city";
	public static final String BILL_TO_STATE="billto_stateprovince";
	public static final String BILL_TO_POSTALCODE="billto_zipcode";
	public static final String BILL_TO_COUNTRY="billto_country";
	public static final String BILL_TO_EMAIL="billto_email";
	public static final String INTRUMENT_IDENTIIFIER_ID="instrumentIdentifierId";
	public static final String HCL_SOULTION_ID="CyberSoure_hcl_solution_id";
	public static final String BILL_EMAIL_EMPTY="null@cybersource.com";
	public static final String CARD_CVV="cc_cvc";
	public static final String CARD_EXPIRE_MONTH="expire_month";
	public static final String CARD_EXPIRE_YEAR="expire_year";
	public static final String RES_SUCCESS="AUTHORIZED,PARTIAL_AUTHORIZED";
	
	public static final String RES_DECLINED="DECLINED";
	public static final String RES_AUTHORIZED_PENDING_REVIEW="AUTHORIZED_PENDING_REVIEW";
	public static final String URL = "URL";
	
}
