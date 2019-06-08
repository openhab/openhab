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

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRFStopPublisherCommand implements LightwaveRFCommand {

    @Override
    public String getLightwaveRfCommandString() {
        return null;
    }

    public String getRoomId() {
        return null;
    }

    public String getDeviceId() {
        return null;
    }

    @Override
    public State getState(LightwaveRfType type) {
        return null;
    }

    @Override
    public LightwaveRfMessageId getMessageId() {
        return null;
    }

    @Override
    public LightwaveRfMessageType getMessageType() {
        return null;
    }

}
