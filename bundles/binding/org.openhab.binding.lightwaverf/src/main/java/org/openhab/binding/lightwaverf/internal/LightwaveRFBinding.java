/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.binding.lightwaverf.internal.LightwaveRFReceiver;
import org.openhab.binding.lightwaverf.internal.LightwaveRFSender;
import org.openhab.binding.lightwaverf.internal.LightwaveRFMessageListener;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.UnknownHostException;

/**
 * @author Neil Renaud
 * @since 1.6
 */
public class LightwaveRFBinding extends AbstractBinding<LightwaveRFBindingProvider> implements LightwaveRFMessageListener {

	private final Logger logger = LoggerFactory.getLogger(LightwaveRFBinding.class);
	private LightwaveRFReceiver receiver = null;
	private LightwaveRFSender sender = null;

	@Override
	public void activate() {
		try{
			receiver = new LightwaveRFReceiver();
			receiver.start();
			receiver.addListener(this);
			sender = new LightwaveRFSender();
			sender.start();
		}
		catch(UnknownHostException e){
			logger.error("Error creating LightwaveRFSender", e);
		}
	}

	@Override
	public void deactivate() {
		receiver.stop();
		receiver = null;
		sender.stop();
		sender = null;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand(" + itemName + ", " + command +") is called!");
        String roomId = getRoomId(itemName);
        String deviceId = getDeviceId(itemName);
        LightwaveRFCommand lightwaverfMessageString = LightwaverfConvertor.convertToLightwaveRfMessage(roomId, deviceId, command);
		sender.sendUDP(lightwaverfMessageString);
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate(" + itemName + ", " + newState + ") is called!");
                String roomId = getRoomId(itemName);
                String deviceId = getDeviceId(itemName);
                LightwaveRFCommand lightwaverfMessageString = LightwaverfConvertor.convertToLightwaveRfMessage(roomId, deviceId, newState);
                sender.sendUDP(lightwaverfMessageString);
	}


	private String getItemName(String message){
		return "itemName";
	}

	private State getState(String message){
		return OnOffType.ON;
	}

	private String getRoomId(String itemName){
		return "1";
	}

	private String getDeviceId(String itemName){
		return "1";
	}
	
	private String getItemName(String roomId, String deviceId){
		return "TestItemName";
	}

	public void messageRecevied(LightwaveRFCommand command) {
		String itemName = getItemName(command.getRoomId(), command.getDeviceId());
		State state = command.getState();

		eventPublisher.postUpdate(itemName, state);
	}
}
