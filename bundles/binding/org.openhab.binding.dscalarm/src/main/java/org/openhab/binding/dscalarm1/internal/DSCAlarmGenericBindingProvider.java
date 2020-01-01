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
package org.openhab.binding.dscalarm1.internal;

import java.util.Set;

import org.openhab.binding.dscalarm1.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm1.DSCAlarmBindingProvider;
import org.openhab.binding.dscalarm1.internal.model.DSCAlarmDeviceType;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * DSC Alarm device binding information from it.
 * </p>
 *
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * </p>
 * <p>
 * dscalarm="device type:&lt;partition#>:&lt;zone#>:item type"
 * </p>
 * 
 * @author Russell Stephens
 * @since 1.6.0
 *
 */
public class DSCAlarmGenericBindingProvider extends AbstractGenericBindingProvider implements DSCAlarmBindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(DSCAlarmGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "dscalarm";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // All types are valid
        logger.trace("validateItemType({}, {})", item.getName(), bindingConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

        String[] sections = bindingConfig.split(":");

        if (sections.length < 2 || sections.length > 4) {
            throw new BindingConfigParseException("Invalid number of sections in the binding: " + bindingConfig);
        }

        DSCAlarmDeviceType dscAlarmDeviceType = null;
        int partitionId = 0;
        int zoneId = 0;
        DSCAlarmItemType dscAlarmItemType = null;

        try {
            dscAlarmDeviceType = DSCAlarmDeviceType.getDSCAlarmDeviceType(sections[0]);

            switch (dscAlarmDeviceType) {
                case PANEL:
                    dscAlarmItemType = DSCAlarmItemType.getDSCAlarmItemType(sections[1]);
                    break;
                case PARTITION:
                    partitionId = Integer.parseInt(sections[1]);
                    dscAlarmItemType = DSCAlarmItemType.getDSCAlarmItemType(sections[2]);
                    break;
                case ZONE:
                    partitionId = Integer.parseInt(sections[1]);
                    zoneId = Integer.parseInt(sections[2]);
                    dscAlarmItemType = DSCAlarmItemType.getDSCAlarmItemType(sections[3]);
                    break;
                case KEYPAD:
                    dscAlarmItemType = DSCAlarmItemType.getDSCAlarmItemType(sections[1]);
                    break;
                default:
                    logger.debug("Invalid Device Type in binding configuration: {}", dscAlarmDeviceType);
                    break;
            }

        } catch (Exception e) {
            throw new BindingConfigParseException("Binding Configuration Error: deviceType: " + dscAlarmDeviceType);
        }

        if (dscAlarmItemType == null) {
            logger.error("processBindingConfiguration(): {}: DSC Alarm Item Type is NULL! Item Not Added!", item.getName());
            return;
        }

        DSCAlarmBindingConfig config = new DSCAlarmBindingConfig(dscAlarmDeviceType, partitionId, zoneId, dscAlarmItemType);
        addBindingConfig(item, config);

        super.processBindingConfiguration(context, item, bindingConfig);
        logger.debug("processBindingConfiguration(): Item added: {} - {}", item.getName(), bindingConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DSCAlarmBindingConfig getDSCAlarmBindingConfig(String itemName) {
        return (DSCAlarmBindingConfig) this.bindingConfigs.get(itemName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item getItem(String itemName) {
        for (Set<Item> items : contextMap.values()) {
            if (items != null) {
                for (Item item : items) {
                    if (itemName.equals(item.getName())) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

}
