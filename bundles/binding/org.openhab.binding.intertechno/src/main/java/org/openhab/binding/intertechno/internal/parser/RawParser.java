/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal.parser;

import java.util.List;

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This parser is for raw Intertechno configurations. Use this if we don't have
 * a more convenient parser. The address parts need to specify the encoded
 * address and the on and off command as Strings which can be send via the CUL.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class RawParser extends AbstractIntertechnoParser {

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        for (int i = 0; i < configParts.size(); i++) {
            String paramName = configParts.get(i).split("=")[0].toLowerCase();
            String paramValue = configParts.get(i).split("=")[1];

            switch (paramName) {
                case "commandon":
                    commandON = paramValue;
                    break;
                case "commandoff":
                    commandOFF = paramValue;
                    break;
            }
        }
    }

}
