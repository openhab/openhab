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
package org.openhab.binding.ihc.ws.datatypes;

import org.openhab.binding.ihc.ws.IhcExecption;

/**
 * <p>
 * Java class for WSControllerState complex type.
 *
 */

public class WSControllerState extends WSBaseDataType {
    private String state;

    public WSControllerState() {
    }

    public WSControllerState(String state) {
        this.state = state;
    }

    /**
     * Gets the state value for this WSControllerState.
     * 
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state value for this WSControllerState.
     * 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void encodeData(String data) throws IhcExecption {
        if (data.contains("getState1")) {
            state = parseValue(data, "/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:getState1/ns1:state");
        } else if (data.contains("waitForControllerStateChange3")) {
            state = parseValue(data, "/SOAP-ENV:Envelope/SOAP-ENV:Body/ns1:waitForControllerStateChange3/ns1:state");
        } else {
            throw new IhcExecption("Encoding error, unsupported data");
        }
    }

}
