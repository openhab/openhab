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
package org.openhab.binding.insteonhub.internal.util;

import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.slf4j.Logger;

/**
 * Utility functions for common log statements
 *
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBindingLogUtil {

    public static void logCommunicationFailure(Logger logger, InsteonHubProxy proxy, Throwable t) {
        logger.error("Communication error with Insteon Hub @" + proxy.getConnectionString(), t);
    }

    public static void logCommunicationFailure(Logger logger, InsteonHubProxy proxy, String device, Throwable t) {
        logger.warn("Cannot communicate with Insteon Hub @" + proxy.getConnectionString() + " (device:" + device + ")",
                t);
    }
}
