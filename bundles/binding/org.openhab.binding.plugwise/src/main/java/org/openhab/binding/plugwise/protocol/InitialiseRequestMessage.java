/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
