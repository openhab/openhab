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
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 *
 * @author Chris Jackson
 * @since 1.5.0
 */
public class GetVersionMessageClass extends ZWaveCommandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(GetVersionMessageClass.class);

    private String zWaveVersion = "Unknown";
    private int ZWaveLibraryType = 0;

    public SerialMessage doRequest() {
        return new SerialMessage(SerialMessageClass.GetVersion, SerialMessageType.Request,
                SerialMessageClass.GetVersion, SerialMessagePriority.High);
    }

    @Override
    public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage,
            SerialMessage incomingMessage) {
        ZWaveLibraryType = incomingMessage.getMessagePayloadByte(12);
        zWaveVersion = new String(ArrayUtils.subarray(incomingMessage.getMessagePayload(), 0, 11));
        logger.debug(String.format("Got MessageGetVersion response. Version = %s, Library Type = 0x%02X", zWaveVersion,
                ZWaveLibraryType));

        checkTransactionComplete(lastSentMessage, incomingMessage);

        return true;
    }

    public String getVersion() {
        return zWaveVersion;
    }

    public int getLibraryType() {
        return ZWaveLibraryType;
    }
}
