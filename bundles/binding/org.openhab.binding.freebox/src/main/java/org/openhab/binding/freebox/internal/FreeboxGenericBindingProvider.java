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
package org.openhab.binding.freebox.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.freebox.FreeboxBindingConfig;
import org.openhab.binding.freebox.FreeboxBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * The syntax of the binding configuration strings accepted is either: <br>
 * freebox="commandtype" <br>
 * or <br>
 * freebox="commandtype/value" <br>
 * where "commandtype" matches one of the enumerated commands present in
 * org.openhab.binding.freebox.internal.CommandType
 * and "value" is a parameter for the command defined by the user
 *
 * @author clinique
 * @since 1.5.0
 */
public class FreeboxGenericBindingProvider extends AbstractGenericBindingProvider implements FreeboxBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "freebox";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof DateTimeItem
                || item instanceof StringItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only String, Switch and Number are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        FreeboxBindingConfig config = parseBindingConfig(bindingConfig, item);
        addBindingConfig(item, config);
    }

    private FreeboxBindingConfig parseBindingConfig(String bindingConfig, Item item)
            throws BindingConfigParseException {

        String commandElts[] = StringUtils.split(StringUtils.trim(bindingConfig), '/');
        CommandType commandType = CommandType.fromString(commandElts[0]);
        String commandParam = null;
        if (commandElts.length == 2) {
            commandParam = commandElts[1];
        }

        return new FreeboxBindingConfig(commandType, commandParam, item);
    }

    @Override
    public FreeboxBindingConfig getConfig(String itemName) {
        FreeboxBindingConfig config = (FreeboxBindingConfig) bindingConfigs.get(itemName);
        return config;
    }

}
