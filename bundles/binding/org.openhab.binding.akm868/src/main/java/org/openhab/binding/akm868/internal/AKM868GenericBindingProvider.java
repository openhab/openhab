/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.akm868.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.akm868.AKM868BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Michael Heckmann
 * @since 1.7.0
 */
public class AKM868GenericBindingProvider extends AbstractGenericBindingProvider implements AKM868BindingProvider {

	static final Logger logger = LoggerFactory.getLogger(AKM868GenericBindingProvider.class);
	private Map<String, Item> items = new HashMap<String, Item>();
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "akm868";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
//		logger.debug("AKM868GenericBindingProvider: validateItem");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}
	
	public String getId(String itemName) {
		AKM868BindingConfig config = (AKM868BindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.id : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		AKM868BindingConfig config = new AKM868BindingConfig(bindingConfig);
//		logger.debug("AKM868GenericBindingProvider: processBindingConfig");
		//parse bindingconfig here ...
		items.put(item.getName(), item);
		addBindingConfig(item, config);		
	}
	
	
	class AKM868BindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public String id;
		
		public AKM868BindingConfig(String id) {
			this.id = id;
		}
	}
	
	
}
