package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfHeatInfoRequest extends AbstractLightwaveRfCommand implements LightwaveRfRoomMessage {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])F\\*r\\s*");
	private static final String FUNCTION = "*r";
	
	private final LightwaveRfMessageId messageId;
	private final String roomId;
	
	public LightwaveRfHeatInfoRequest(int messageId, String roomId) {
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
		this.roomId = roomId;
	}
	
	
	public LightwaveRfHeatInfoRequest(String message) throws LightwaveRfMessageException {
		try{
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
			roomId = m.group(2);
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error converting message: " + message);
		}
	}
	
	@Override
	public String getLightwaveRfCommandString() {
		return getFunctionMessageString(messageId, roomId, FUNCTION);
	}

	@Override
	public String getRoomId() {
		return roomId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		return message.contains(FUNCTION);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.HEAT_REQUEST;
	}

}
