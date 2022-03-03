# CyberSource Payment Integration with HCL Commerce
The HCL Commerce provide assets to integrate the CyberSource Payment. Merchants can integrate CyberSource to their commerce website and empower customers with new payment system. We have followed the CyberSource PCI compliance.

**DISCLAIMER: This asset is being provided as-is to help accelerate your projects. As such, we are unable to provide official support for this asset. We have provided documentation as well as any needed code artifacts for your use.**

We are using these CyberSource Rest services during the checkout flow-
* Generate Flex key 

https://api.cybersource.com/flex/v1/keys

* Tokenize card 

https://api.cybersource.com/flex/v1/tokens

* Authorize the Payment 

https://api.cybersource.com/pts/v2/payments

## Prerequisites
*	HCL Commerce V9.1.x/ HCL Commerce React Storefront

*	CyberSource Merchant Account. Create Merchant account using below link.

https://ebc2.cybersource.com/ebc2/registration/external
*	Create shared secret key and API key. Below is the URL which provide steps to generate the key.

https://developer.cybersource.com/api/developer-guides/dita-gettingstarted/authentication/createSharedKey.html

## Plugin_Configuration
Please refer the document [CyberSource_Plugin_Guide.docx](https://github.com/HCL-Commerce-Asset-Repository-Bullpen/CyberSource-Payment-Integration/blob/main/Plugin_Configuration/CyberSource_Integration_Developer_Guide.docx) to complete the Payment plugin changes.

## Aurora Storefront
Please refer the document [Aurora_UI_Guide.docx](https://github.com/HCL-Commerce-Asset-Repository-Bullpen/CyberSource-Payment-Integration/blob/main/Aurora_UI/CyberSource_Aurora_Store_Developer_guide.docx) to complete the Aurora store integration.

## React Storefront
Please refer the document [React_UI_Guide.docx](https://github.com/HCL-Commerce-Asset-Repository-Bullpen/CyberSource-Payment-Integration/blob/main/UI-react/Cybersource-React-UI-implementation-guide.docx) to complete the React store integration.
