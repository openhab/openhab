/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cardio2e;

import java.util.Map;

import org.openhab.binding.cardio2e.internal.Cardio2eGenericBindingProvider.Cardio2eBindingConfig;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Manuel Alberto Guerrero Díaz
 * @since 1.7.0
 */
public interface Cardio2eBindingProvider extends BindingProvider {
    /**
     * @param itemName
     *            The name of the item
     * @return The item with the given name
     */
    Item getItem(String itemName);

    /**
     * @param itemName
     *            The name of the item
     * @return LinkedList of the BindingConfigs mapped to the item with the given name
     */
    Cardio2eBindingConfig getConfig(String itemName);
	
    /**
     * @param itemName
     *            The name of the item
     * @return Reverser Order LinkedList of the BindingConfigs mapped to the item with the given name
     */
    Cardio2eBindingConfig getReverseOrderConfig(String itemName);
	
    /**
     * @param transaction
     *            A valid Cardio2eTransaction
     * @return A HashMap that contains only the BindingConfigs with Cardio2eTransaction like "transaction" and their IntemNames
     */
    Map<String, BindingConfig> getMatchBindingConfigs (Cardio2eTransaction transaction);
}
