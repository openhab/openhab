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
package org.openhab.binding.plcbus.internal.protocol.commands;

import org.openhab.binding.plcbus.internal.protocol.Command;

/**
 * StatusRequest Command in PLCBus Protocol
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class StatusRequest extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getId() {
        return 0x0F;
    }

    /**
     * Returns the first Parameter
     * 
     * @return First Parameter
     */
    public int getParameter1() {
        return getData1();
    }

    /**
     * Returns the second Parameter
     * 
     * @return Seconds Parameter
     */
    public int getParameter2() {
        return getData2();
    }
}
