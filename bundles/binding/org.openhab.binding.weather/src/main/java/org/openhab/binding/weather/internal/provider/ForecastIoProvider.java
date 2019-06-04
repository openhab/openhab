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
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * ForecastIO weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ForecastIoProvider extends AbstractWeatherProvider {
    private static final String URL = "https://api.darksky.net/forecast/[API_KEY]/[LATITUDE],[LONGITUDE]?units=[UNITS]&lang=[LANGUAGE]&exclude=hourly,flags";

    public ForecastIoProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.FORECASTIO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
