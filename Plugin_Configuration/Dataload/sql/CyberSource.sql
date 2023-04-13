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



/*Payment Policies*/
insert into policy (POLICY_ID,POLICYNAME,POLICYTYPE_ID,STOREENT_ID,PROPERTIES) values((SELECT MAX(Policy_Id)+1 from policy),'CyberSource','Payment',<storeId>,'attrPageName=StandardCyberSource&paymentConfigurationId=default&display=true&compatibleMode=false&uniqueKey=tran_id');


insert into POLICYDESC values((select policy_id from policy where policyname='CyberSource' and storeent_id=<storeId>),<lang_id>,'Credit Cards','Credit Cards',CURRENT TIMESTAMP,CURRENT TIMESTAMP,0);

insert into POLICYCMD (POLICY_ID,BUSINESSCMDCLASS) values((select policy_id from policy where policyname='CyberSource' and storeent_id=<storeId>),'com.ibm.commerce.payment.actions.commands.DoPaymentActionsPolicyCmdImpl');

insert into POLICYCMD (POLICY_ID,BUSINESSCMDCLASS) values((select policy_id from policy where policyname='CyberSource' and storeent_id=<storeId>),'com.ibm.commerce.payment.actions.commands.EditPaymentInstructionPolicyCmdImpl');

insert into POLICYCMD (POLICY_ID,BUSINESSCMDCLASS) values((select policy_id from policy where policyname='CyberSource' and storeent_id=<storeId>),'com.ibm.commerce.payment.actions.commands.QueryPaymentsInfoPolicyCmdImpl');


/*Merchant Information*/
INSERT INTO STORECONF(STOREENT_ID, NAME, VALUE, OPTCOUNTER) VALUES(<storeId>,'CyberSoure_merchant_id',<cybersource_merchant_id>,0);

INSERT INTO STORECONF(STOREENT_ID, NAME, VALUE, OPTCOUNTER) VALUES(<storeId>,'CyberSource_api_key',<cybersource_api_key>,0);

INSERT INTO STORECONF(STOREENT_ID, NAME, VALUE, OPTCOUNTER) VALUES(<storeId>,'CyberSource_secret_key',<cybersource_secret_key>,0);

/* SANDBOX Account hostname is apitest.cybersource.com. Prodcution hostname is api.cybersource.com */
INSERT INTO STORECONF VALUES(<storeId>,'CyberSource_api_hostname','apitest.cybersource.com',0);

INSERT INTO STORECONF(STOREENT_ID, NAME, VALUE, OPTCOUNTER) VALUES(<storeId>, 'CyberSoure_hcl_solution_id', '2YET9D1Z', 0);

/*CmdReg for extension of PIADDCmd*/
INSERT INTO CMDREG (STOREENT_ID,INTERFACENAME,CLASSNAME,PROPERTIES,TARGET,OPTCOUNTER) VALUES(<storeId>,'com.ibm.commerce.edp.commands.PIAddCmd','com.hcl.commerce.edp.commands.HCLCyberSourcePIAddCmdImpl','retriable=1','Local',0);

