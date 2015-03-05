/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import org.openhab.binding.primare.internal.protocol.PrimareMessage;

import org.openhab.core.types.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class for Primare messages to be extended by model-specific
 * implementation classes
 * This class is used for converting OpenHAB commands
 * to a byte array representation to be sent to the Primare.
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareMessageFactory {
    
	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareMessageFactory.class);
    
	public abstract PrimareMessage getMessage(Command command, String deviceCmdString);
    
	// Initialize device to some sensible state
	// public abstract PrimareMessage getInitMessage();
	public abstract PrimareMessage[] getInitMessages();
	
	// A message triggering an asynchronous response
	// public abstract PrimareMessage getPingMessage();
	public abstract PrimareMessage[] getPingMessages();    
	
}
