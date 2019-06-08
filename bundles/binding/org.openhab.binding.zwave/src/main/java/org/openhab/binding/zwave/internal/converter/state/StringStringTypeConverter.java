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
package org.openhab.binding.zwave.internal.converter.state;

import org.openhab.core.library.types.StringType;

/**
 * Converts from a {@link String} to a {@link StringType}
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class StringStringTypeConverter extends ZWaveStateConverter<String, StringType> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected StringType convert(String value) {
        return new StringType(value);
    }

}
