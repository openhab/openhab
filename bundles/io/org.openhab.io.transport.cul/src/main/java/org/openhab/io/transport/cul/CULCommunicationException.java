/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

/**
 * An exception which is thrown if communication with a culfw based device
 * causes an error.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULCommunicationException extends Exception {

    private static final long serialVersionUID = -1861588016496497682L;

    public CULCommunicationException() {
        super();
    }

    public CULCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CULCommunicationException(String message) {
        super(message);
    }

    public CULCommunicationException(Throwable cause) {
        super(cause);
    }

}
