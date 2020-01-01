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
package org.openhab.binding.enocean.internal.converter;

import java.math.BigDecimal;

import org.opencean.core.common.values.NumberWithUnit;
import org.opencean.core.common.values.Unit;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;

/**
 * A converter to convert a NumberWithUnit WATT to a DecimalType
 *
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 *
 */
public class PowerConverter extends StateConverter<NumberWithUnit, DecimalType> {

    @Override
    protected NumberWithUnit convertFromImpl(DecimalType source) {
        return new NumberWithUnit(Unit.WATT, source.toBigDecimal());
    }

    @Override
    protected DecimalType convertToImpl(NumberWithUnit source) {
        return new PercentType((BigDecimal) source.getValue());
    }

}
