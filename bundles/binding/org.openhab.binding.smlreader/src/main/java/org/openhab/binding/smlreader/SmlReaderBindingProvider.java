/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader;

import org.openhab.binding.smlreader.conversion.IUnitConverter;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Mathias Gilhuber
 * @since 1.7.0
 */

public interface SmlReaderBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configured device id for the given <code>itemName</code>. If
	 * no device id has been configured or the itemName is unknown,
	 * <code>null<code> is returned
	 * 
	 * @param itemName
	 *            the item to find the meter name for
	 * @return the configured device id or <code>null<code> if nothing is
	 *         configured or the itemName is unknown
	 */
	public String getDeviceNameFromConfiguration(String itemName);
	
	/**
	 * Returns the configured obis for the given <code>itemName</code>. If no
	 * obis has been configured or the itemName is unknown,
	 * <code>null<code> is returned
	 * 
	 * @param itemName
	 *            the item to find the obis for
	 * @return the configured obis or <code>null<code> if nothing is configured
	 *         or the itemName is unknown
	 */
	public String getObis(String itemName);	
	
	/**
	 * Returns the configured targetUnit for the given <code>itemName</code>. If no
	 * targetUnit has been configured or the itemName is unknown,
	 * <code>TargetUnit.none<code> is returned
	 * 
	 * @param itemName
	 *            the item to find the targetUnit for
	 * @return the configured targetUnit or <code>TargetUnit.none<code> if nothing is configured
	 *         or the itemName is unknown
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends IUnitConverter> getUnitConverter(String itemName);
	
	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

}
