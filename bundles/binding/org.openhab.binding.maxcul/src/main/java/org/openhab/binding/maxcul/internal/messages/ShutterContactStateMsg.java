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
package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle shutter contact state messages
 *
 * @author Johannes Goehr (johgoe)
 * @since 1.8.0
 */
public class ShutterContactStateMsg extends BaseMsg {

    private final static int SHUTTER_CONTACT_STATE_PAYLOAD_LEN = 1; /* in bytes */

    public enum ShutterContactState {
        OPEN,
        CLOSED,
        UNKNOWN;
    }

    private boolean batteryLow;

    private boolean rfError;

    private ShutterContactState state = ShutterContactState.UNKNOWN;

    private static final Logger logger = LoggerFactory.getLogger(ShutterContactStateMsg.class);

    public ShutterContactStateMsg(String rawMsg) {
        super(rawMsg);
        logger.debug(this.msgType + " Payload Len -> " + this.payload.length);

        if (this.payload.length == SHUTTER_CONTACT_STATE_PAYLOAD_LEN) {
            boolean isOpen = extractBitFromByte(this.payload[0], 1);
            ;
            if (!isOpen) {
                state = ShutterContactState.CLOSED;
            } else {
                state = ShutterContactState.OPEN;
            }
            rfError = extractBitFromByte(this.payload[0], 6);
            /* extract battery status */
            batteryLow = extractBitFromByte(this.payload[0], 7);
        } else {
            logger.error("Got " + this.msgType + " message with incorrect length!");
        }
    }

    public ShutterContactState getState() {
        return state;
    }

    @Override
    protected void printFormattedPayload() {
        super.printFormattedPayload();
        logger.debug("\tState 				=> " + state);
        logger.debug("\tRF Error            => " + rfError);
        logger.debug("\tBattery Low         => " + batteryLow);
    }

    public boolean getBatteryLow() {
        return batteryLow;
    }

}
