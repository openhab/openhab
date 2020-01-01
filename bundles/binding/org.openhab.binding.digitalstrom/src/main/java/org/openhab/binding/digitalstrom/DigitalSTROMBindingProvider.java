/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.digitalstrom;

import java.util.List;

import org.openhab.binding.digitalstrom.internal.config.DigitalSTROMBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 */
public interface DigitalSTROMBindingProvider extends BindingProvider {

    /**
     * Returns the configuration for the item with the given name.
     * 
     * @param itemName
     * @return The configuration if there is an item with the given name, null
     *         otherwise.
     */
    public DigitalSTROMBindingConfig getItemConfig(String itemName);

    public List<String> getItemNamesByDsid(String dsid);

    public List<DigitalSTROMBindingConfig> getAllCircuitConsumptionItems();

    public List<DigitalSTROMBindingConfig> getAllDeviceConsumptionItems();

}
