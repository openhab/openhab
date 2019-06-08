/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.souliss.internal.network.typicals;

import java.net.DatagramSocket;

import org.openhab.core.types.State;

/**
 * Typical T12 SWITCH WITH AUTO mode (NOT SUPPORTED BY OPENHAB)
 *
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT14 extends SoulissT11 {

    @Override
    public void commandSEND(short command) {
        // TODO Auto-generated method stub
        super.commandSEND(command);
    }

    @Override
    public State getOHState() {
        // TODO Auto-generated method stub
        return super.getOHState();
    }

    public SoulissT14(DatagramSocket _datagramsocket, String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot,
            String sOHType) {
        super(_datagramsocket, sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
        this.setType(Constants.Souliss_T14);
    }
}
