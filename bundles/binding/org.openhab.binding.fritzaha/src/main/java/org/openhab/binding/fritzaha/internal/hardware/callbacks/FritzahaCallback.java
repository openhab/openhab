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
package org.openhab.binding.fritzaha.internal.hardware.callbacks;

/**
 * Interface for callbacks in asynchronous requests.
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaCallback {
    /**
     * Runs callback code after response completion.
     */
    void execute(int status, String response);
}
