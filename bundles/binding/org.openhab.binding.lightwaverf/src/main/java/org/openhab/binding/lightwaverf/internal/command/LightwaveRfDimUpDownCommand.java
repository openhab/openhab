package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfDimUpDownCommand implements LightwaveRFCommand {

	/**
	 * Commands could be but this currently doesnt work: 
	 *     100,!R2D3F> (Dim Up)
	 *     101,!R2D3F< (Dim down)
	 */ 
    
    public LightwaveRfDimUpDownCommand(String message) throws LightwaveRfMessageException{
    	throw new IllegalArgumentException("Not implemented");
    }
    
    public LightwaveRfDimUpDownCommand(int messageId, String roomId, String deviceId, boolean up) {
    	throw new IllegalArgumentException("Not implemented");
    }

    public String getLightwaveRfCommandString() {
    	throw new IllegalArgumentException("Not implemented");
    }

	public String getRoomId() {
    	throw new IllegalArgumentException("Not implemented");
	}

	public String getDeviceId() {
    	throw new IllegalArgumentException("Not implemented");
	}

	@Override
	public State getState(LightwaveRfType type) {
    	throw new IllegalArgumentException("Not implemented");
	}
	
	public LightwaveRfMessageId getMessageId() {
    	throw new IllegalArgumentException("Not implemented");
	}
}
