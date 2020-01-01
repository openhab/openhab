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
package org.openhab.binding.digitalstrom.internal.config;

/**
 * @author Alexander Betker
 * @since 1.3.0
 */
public enum ConsumptionConfig {

    ACTIVE_POWER, // (default) possible for dsid and dsmid
    OUTPUT_CURRENT, // only for devices (dsid)
    ELECTRIC_METER // only for meters (dsmid)

}
