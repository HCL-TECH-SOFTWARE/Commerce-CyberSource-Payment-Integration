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


public class ApiException extends Exception{
	private int code = 0;
    private String responseBody = null;
	
    public int getCode() {
		return code;
	}
	public String getResponseBody() {
		return responseBody;
	}
    public ApiException() {}

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable throwable, int code, String responseBody) {
        super(message, throwable);
        this.code = code;
        this.responseBody = responseBody;
    }

    public ApiException(int code) {
        this.code = code;
    }
    
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(int code, String message, String responseBody) {
        this(code, message);
        this.responseBody = responseBody;
    }
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
    
    
}
