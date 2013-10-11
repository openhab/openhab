/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.s300th.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.s300th.S300THBindingProvider;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.CULDeviceException;
import de.akuz.cul.CULHandler;
import de.akuz.cul.CULListener;
import de.akuz.cul.CULManager;
import de.akuz.cul.CULMode;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class S300THBinding extends AbstractActiveBinding<S300THBindingProvider> implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory.getLogger(S300THBinding.class);

	private final static String KS_300_ADDRESS = "ks300";

	/**
	 * the refresh interval which is used to poll values from the S300TH server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private final static String KEY_DEVICE_NAME = "device";

	private String deviceName;

	private CULHandler cul;

	public S300THBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		closeCUL();
	}

	private void setNewDeviceName(String deviceName) {
		if (deviceName == null) {
			logger.error("Device name was null");
			return;
		}
		if (this.deviceName != null && this.deviceName.equals(deviceName)) {
			return;
		}
		closeCUL();
		this.deviceName = deviceName;
		openCUL();
	}

	private void openCUL() {
		try {
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
		} catch (CULDeviceException e) {
			logger.error("Can't open CUL handler for device " + deviceName, e);
		}
	}

	private void closeCUL() {
		if (cul != null) {
			cul.unregisterListener(this);
			CULManager.close(cul);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "S300TH Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Ignore
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Received new config");
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			if (StringUtils.isEmpty(deviceName)) {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME, "The device name can't be empty");
			} else {
				setNewDeviceName(deviceName);
			}

			setProperlyConfigured(true);
			// read further config parameters here ...

		}
	}

	@Override
	public void dataReceived(String data) {
		if (data.startsWith("K") && data.length() == 9) {
			logger.debug("Received raw S300TH data: " + data);
			parseS300THData(data);
		} else if (data.startsWith("K") && data.length() == 15) {
			logger.debug("Received raw KS300 data: " + data);
			parseKS300Data(data);
		}
	}

	/**
	 * Parse KS 300 data
	 * 
	 * @param data
	 */
	private void parseKS300Data(String data) {
		// TODO parse address and other bytes
		double rainValue = (Integer.parseInt("" + data.charAt(14) + data.charAt(11) + data.charAt(12), 16)) * 255 / 1000;
		double windValue = Double.parseDouble(data.charAt(9) + data.charAt(10) + "." + data.charAt(7));
		int humidity = Integer.parseInt("" + data.charAt(8) + data.charAt(5));
		double temperature = Double.parseDouble(data.charAt(6) + data.charAt(4) + "." + data.charAt(4));
		int secondByte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		boolean isRaining = false;
		if ((secondByte & 2) > 0) {
			isRaining = true;
		}
		for (Datapoint datapoint : Datapoint.values()) {
			S300THBindingConfig config = findConfig(KS_300_ADDRESS, datapoint);
			if (config == null) {
				continue;
			}
			double value = 0.0;
			switch (datapoint) {
			case TEMPERATURE:
				value = temperature;
				break;
			case HUMIDITY:
				value = humidity;
				break;
			case WIND:
				value = windValue;
				break;
			case RAIN:
				value = rainValue;
				break;
			case IS_RAINING:
				continue;
			}
			updateItem(config.item, value);
		}
		S300THBindingConfig config = findConfig(KS_300_ADDRESS, Datapoint.IS_RAINING);
		if (config != null) {
			OnOffType status = isRaining ? OnOffType.ON : OnOffType.OFF;
			eventPublisher.postUpdate(config.item.getName(), status);
		}
	}

	/**
	 * Parse S300TH data
	 * 
	 * @param data
	 */
	private void parseS300THData(String data) {
		int secondByte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		int addressValue = data.charAt(2) + (secondByte & 7);
		String address = Integer.toHexString(addressValue);
		String humidityString = data.charAt(7) + data.charAt(8) + "." + data.charAt(5);
		String temperatureString = data.charAt(6) + data.charAt(3) + "." + data.charAt(4);
		double humidity = Double.parseDouble(humidityString);
		double temperature = Double.parseDouble(temperatureString);
		logger.debug("Received data from device with address " + address + " : temperature: " + temperature
				+ " humidity: " + humidity);
		if ((secondByte & 8) > 0) {
			temperature = temperature * -1;
		}

		S300THBindingConfig temperatureConfig = findConfig(address, Datapoint.TEMPERATURE);
		if (temperatureConfig != null) {
			updateItem(temperatureConfig.item, temperature);
		}
		S300THBindingConfig humidityConfig = findConfig(address, Datapoint.HUMIDITY);
		if (humidityConfig != null) {
			updateItem(humidityConfig.item, humidity);
		}
	}

	private void updateItem(Item item, double value) {
		DecimalType type = new DecimalType(value);
		eventPublisher.postUpdate(item.getName(), type);
	}

	private S300THBindingConfig findConfig(String address, Datapoint datapoint) {
		for (S300THBindingProvider provider : this.providers) {
			S300THBindingConfig config = provider.getBindingConfigForAddressAndDatapoint(address, datapoint);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	public void error(Exception e) {
		logger.error("Received error from CUL instead fo data", e);

	}

}
