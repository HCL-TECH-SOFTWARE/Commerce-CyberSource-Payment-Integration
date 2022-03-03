package com.hcl.commerce.payments.cybersource.commands;

import com.ibm.commerce.command.ControllerCommand;

public interface HCLCyberSourceGenerateKeyCmd extends ControllerCommand {
	public static final String defaultCommandClassName = HCLCyberSourceGenerateKeyCmdImpl.class.getName();

}
