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
package org.openhab.binding.weather.internal.converter.property;

import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter with a UTC pattern.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class UtcDateConverter extends AbstractDateConverter {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm a";

    public UtcDateConverter() {
        super(DATE_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.UTC_DATE;
    }

}
