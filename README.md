# CyberSource Payment Integration with HCL Commerce

## WARRANTY & SUPPORT 
HCL Software provides HCL Commerce open source assets “as-is” without obligation to support them nor warranties or any kind, either express or implied, including the warranty of title, non-infringement or non-interference, and the implied warranties and conditions of merchantability and fitness for a particular purpose. HCL Commerce open source assets are not covered under the HCL Commerce master license nor Support contracts.

If you have questions or encounter problems with an HCL Commerce open source asset, please open an issue in the asset's GitHub repository. For more information about [GitHub issues](https://docs.github.com/en/issues), including creating an issue, please refer to [GitHub Docs](https://docs.github.com/en). The HCL Commerce Innovation Factory Team, who develops HCL Commerce open source assets, monitors GitHub issues and will do their best to address them. 

## HCLC Cybersource Payment Integration Asset
The HCL Commerce provide assets to integrate the CyberSource Payment. Merchants can integrate CyberSource to their commerce website and empower customers with new payment system. We have followed the CyberSource PCI compliance.

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
