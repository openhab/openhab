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
package org.openhab.io.transport.cul;

import java.util.Arrays;
import java.util.List;

/**
 * This enum represents the different RF modes in which the CUL can work. Based
 * on this enum a culfw based device will be configured when openend for the
 * first time.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public enum CULMode {

    // TODO more research on correct CUL receiption modes
    /**
     * Slow RF mode for FS20, FHT etc. Intertechno also works in this mode.
     */
    SLOW_RF("X21"),
    /**
     * Fast RF mode for Homematic. Intertechno should also work in this mode.
     */
    ASK_SIN("X10", "Ar"),
    /**
     * Fast RF mode for the Moritz protocol of the Max! heating control system.
     * Intertechno should also work in this mode.
     */
    MAX("X21", "Zr");

    private List<String> commands;

    private CULMode(String... commands) {
        this.commands = Arrays.asList(commands);
    }

    public List<String> getCommands() {
        return commands;
    }

}
