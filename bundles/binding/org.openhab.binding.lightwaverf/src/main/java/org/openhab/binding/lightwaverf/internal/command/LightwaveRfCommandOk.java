package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfCommandOk extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),OK");
	
	private final LightwaveRfMessageId messageId;
	
	public LightwaveRfCommandOk(String message) throws LightwaveRfMessageException {
		try{
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			this.messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error converting message: " + message, e);
		}
	}

	public String getLightwaveRfCommandString() {
		return getOkString(messageId);
	}

	public String getRoomId() {
		return null;
	}

	public String getDeviceId() {
		return null;
	}

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}
	
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		Matcher m = REG_EXP.matcher(message);
		return m.matches();
	}
}
