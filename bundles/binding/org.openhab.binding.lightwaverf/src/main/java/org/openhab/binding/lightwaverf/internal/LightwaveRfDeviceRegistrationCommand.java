package org.openhab.binding.lightwaverf.internal;

import org.openhab.core.types.State;

public class LightwaveRfDeviceRegistrationCommand implements LightwaveRFCommand {

	public String getLightwaveRfCommandString() {
		return "!F*p\n";
	}

	public String getRoomId() {
		return null;
	}

	public String getDeviceId() {
		return null;
	}

	public State getState() {
		return null;
	}

}
