/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

/**
 * Typical T53
 * Humidity Sensor
 * Derived from T51
 * Analog input, half-precision floating point
 * 
 * @author Antonino-Fazio
 */
public class SoulissT53 extends SoulissT51 {
	
	public SoulissT53(String sSoulissNodeIPAddress, String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType) {
		super(sSoulissNodeIPAddress,sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
		this.setType(Constants.Souliss_T53_HumiditySensor);
	}
}
