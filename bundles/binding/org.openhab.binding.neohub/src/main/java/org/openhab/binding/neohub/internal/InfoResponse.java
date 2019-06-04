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
package org.openhab.binding.neohub.internal;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A wrapper around the JSON response to the INFO command.
 *
 * @author Sebastian Prehn
 * @since 1.5.0
 */
class InfoResponse {

    @JsonProperty("devices")
    private List<Device> devices;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Device {

        @JsonProperty("device")
        private String deviceName;
        @JsonProperty("CURRENT_SET_TEMPERATURE")
        private BigDecimal currentSetTemperature;
        @JsonProperty("CURRENT_TEMPERATURE")
        private BigDecimal currentTemperature;
        @JsonProperty("CURRENT_FLOOR_TEMPERATURE")
        private BigDecimal currentFloorTemperature;
        @JsonProperty("AWAY")
        private Boolean away;
        @JsonProperty("HOLIDAY")
        private Boolean holiday;
        @JsonProperty("HOLIDAY_DAYS")
        private BigDecimal holidayDays;
        @JsonProperty("STANDBY")
        private Boolean standby;
        @JsonProperty("HEATING")
        private Boolean heating;
        @JsonProperty("PREHEAT")
        private Boolean preHeat;

        public BigDecimal getCurrentSetTemperature() {
            return currentSetTemperature;
        }

        public BigDecimal getCurrentTemperature() {
            return currentTemperature;
        }

        public BigDecimal getCurrentFloorTemperature() {
            return currentFloorTemperature;
        }

        public Boolean isAway() {
            return away;
        }

        public Boolean isHoliday() {
            return holiday;
        }

        public BigDecimal getHolidayDays() {
            return holidayDays;
        }

        public Boolean isStandby() {
            return standby;
        }

        public Boolean isHeating() {
            return heating;
        }

        public Boolean isPreHeat() {
            return preHeat;
        }

        public String getDeviceName() {
            return deviceName;
        }
    }

    /**
     * Create wrapper around the JSON response.
     * 
     * @param response
     *            the JSON response
     * @return a wrapper around the JSON response
     */
    static InfoResponse createInfoResponse(String response) {
        try {
            return new ObjectMapper().readValue(response, InfoResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse info response.", e);
        }
    }

    /**
     * Returns the Device section for the given device name.
     * 
     * @param device
     *            the device name
     * @return the matching section in the response
     */
    public Device getDevice(String device) {
        for (Device d : devices) {
            if (device.equals(d.getDeviceName())) {
                return d;
            }
        }
        return null;
    }
}
