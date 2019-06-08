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
package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LightwaveRfRequestHeatInfoTest {

    @Test
    public void test() throws Exception {
        String message = "105,!R3F*r\n";
        LightwaveRfRoomMessage command = new LightwaveRfHeatInfoRequest(message);
        assertEquals("3", command.getRoomId());
        assertEquals("105,!R3F*r\n", command.getLightwaveRfCommandString());
    }

}
