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
package org.openhab.binding.plugwise.protocol;

/**
 * Initialisation request, typically triggered by the Stick
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class InitialiseRequestMessage extends Message {

    public InitialiseRequestMessage() {
        super("");
        type = MessageType.INITIALISE_REQUEST;
        payLoad = "";
        MAC = "";
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

    @Override
    protected void parsePayLoad() {
    }

    @Override
    protected String payLoadToHexString() {
        return "";
    }

}
