package org.openhab.binding.lightwaverf.internal;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDeviceRegistrationCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDimCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatingInfoResponse;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfOnOffCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Type;


public class LightwaverfConvertor {

	private static final Pattern REG_EXP = Pattern.compile(".*F(.).*\\s*");

	// LightwaveRF messageId
    private int nextMessageId = 0;
    private final Lock lock = new ReentrantLock();

    
    public LightwaveRFCommand convertToLightwaveRfMessage(String roomId, String deviceId, Type command){
    	if(deviceId == null){
    		return convertToLightwaveRfMessage(roomId, command);
    	}
    	
    	int messageId = getAndIncrementMessageId();
    	
    	if(command instanceof OnOffType){
            boolean on = (command == OnOffType.ON);
            return new LightwaveRfOnOffCommand(messageId, roomId, deviceId, on);
        }
        else if(command instanceof PercentType){
            int dimmingLevel = ((PercentType) command).intValue();
            return new LightwaveRfDimCommand(messageId, roomId, deviceId, dimmingLevel);
        }
        throw new RuntimeException("Unsupported Command: " + command);
    }

    /**
     * Increment message counter, so different messages have different IDs
     * Important for getting corresponding OK acknowledgements from port 9761 tagged with the same counter value
     */
    private int getAndIncrementMessageId() {
    	try{
    		lock.lock();
			int myMessageId = nextMessageId;
			if(myMessageId >= 999){
				nextMessageId = 0;
			}
			return myMessageId;
    	}
    	finally{
    		lock.unlock();
    	}
    }
	public LightwaveRFCommand convertToLightwaveRfMessage(String roomId, Type command){
    	if(roomId == null){
    		throw new IllegalArgumentException("Item not found");
    	}
    	throw new IllegalArgumentException("Not implemented yet");
    }
    
    public LightwaveRFCommand convertFromLightwaveRfMessage(String message) throws LightwaveRfMessageException {
    	if(LightwaveRfCommandOk.matches(message)){
    		return new LightwaveRfCommandOk(message);
    	}
    	else if(LightwaveRfVersionMessage.matches(message)){
    		return new LightwaveRfVersionMessage(message);
    	}
    	else if(LightwaveRfDeviceRegistrationCommand.matches(message)){
    		return new LightwaveRfDeviceRegistrationCommand(message);
    	}
    	else if(LightwaveRfHeatingInfoResponse.matches(message)){
    		return new LightwaveRfHeatingInfoResponse(message);
    	}
    	else if(LightwaveRfSetHeatingTemperatureCommand.matches(message)){
    		return new LightwaveRfSetHeatingTemperatureCommand(message);
    	}
    	else if(LightwaveRfHeatInfoRequest.matches(message)){
    		return new LightwaveRfHeatInfoRequest(message);
    	}
    	switch (getModeCode(message)) {
		case '0':
		case '1':
			return new LightwaveRfOnOffCommand(message);
		case 'd':
			return new LightwaveRfDimCommand(message);
		case ' ':
		default:
			throw new LightwaveRfMessageException("Message not recorgnised: " + message);
		}
    	
    }
    
    private char getModeCode(String message){
    	Matcher m = REG_EXP.matcher(message);
    	if(m.matches()){
    		String modeCode = m.group(1);
			return modeCode.charAt(0);
    	}
    	return ' ';
    }

	public LightwaveRFCommand getRegistrationCommand() {
		return new LightwaveRfDeviceRegistrationCommand(getAndIncrementMessageId());
	}
}
