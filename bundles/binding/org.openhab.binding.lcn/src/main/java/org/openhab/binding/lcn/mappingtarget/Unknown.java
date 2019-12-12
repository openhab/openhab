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
package org.openhab.binding.lcn.mappingtarget;

import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.input.ModStatusBinSensors;
import org.openhab.binding.lcn.input.ModStatusKeyLocks;
import org.openhab.binding.lcn.input.ModStatusLedsAndLogicOps;
import org.openhab.binding.lcn.input.ModStatusOutput;
import org.openhab.binding.lcn.input.ModStatusRelays;
import org.openhab.binding.lcn.input.ModStatusVar;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Stores unknown input data that could not be parsed into another target type.
 *
 * @author Tobias J�ttner
 *
 */
public class Unknown extends Target {

    /** The original configuration text. */
    private final String input;

    /**
     * Constructor.
     * 
     * @param input the unchanged configuration text
     */
    Unknown(String input) {
        this.input = input;
        logger.error(String.format("Could not parse target: %s", input));
    }

    /** {@inheritDoc} */
    @Override
    public void send(Connection conn, Item item, Command cmd) {
    }

    /** {@inheritDoc} */
    @Override
    public void register(Connection conn) {
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item,
            EventPublisher eventPublisher) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.input;
    }

}
