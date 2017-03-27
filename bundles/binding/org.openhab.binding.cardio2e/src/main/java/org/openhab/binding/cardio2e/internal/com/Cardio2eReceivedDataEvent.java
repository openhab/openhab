/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.cardio2e.internal.com;

import java.util.EventObject;

/**
 * Cardio2e Received Data Event class.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.10.0
 */


public class Cardio2eReceivedDataEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String receivedData = null;

	public Cardio2eReceivedDataEvent(Object source, String receivedData) {
		super(source);
		this.receivedData = receivedData;
	}

	public String getReceivedData() {
		return receivedData;
	}
}