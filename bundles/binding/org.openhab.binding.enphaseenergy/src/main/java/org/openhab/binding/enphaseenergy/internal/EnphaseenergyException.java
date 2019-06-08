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
package org.openhab.binding.enphaseenergy.internal;

/**
 * @author Markus Fritze
 * @since 1.7.0
 */
public class EnphaseenergyException extends RuntimeException {

    public EnphaseenergyException(String message) {
        super(message);
    }

    public EnphaseenergyException(final Throwable cause) {
        super(cause);
    }

    public EnphaseenergyException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
