package org.openhab.binding.onewire.internal.deviceproperties;

import java.util.ArrayList;

import org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class which defines a 1-Wire Device Property which can be read
 * 
 * Basic Configuration for an OneWire Binding:
 * <code>
 * 	onewire="deviceId=<i>deviceId</i>;propertyName<i>propertyName</i>"
 * </code>
 * 
 * Optional:
 * <code>refreshinterval=<i>value in seconds</i></code>
 * Defaults to 60 seconds
 * 
 * Example:
 * <code>
 * 	onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10"
 * </code>
 * 
 * Type-Modifiers can be configured to the items. The documentation of these possible modifiers, look into the specialized classes
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 */
public abstract class AbstractOneWireDevicePropertyBindingConfig implements BindingConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOneWireDevicePropertyBindingConfig.class);

	/**
	 * deviceId like <code>28.67C6697351FF</code>
	 */
	private String ivDeviceId;

	/**
	 * property name like <code>temperature</code>  
	 */
	private String ivPropertyName;
	
	
	/**
	 * Default autofresh value in seconds, can be set for each defined Item
	 */
	private int ivAutoRefreshInSecs = 60; // Default 60 Sekunden

	/** maintains state of filters for eliminating outliers */
	private ArrayList<InterfaceOneWireTypeModifier> ivTypeModifieryList = new ArrayList<InterfaceOneWireTypeModifier>();

	public AbstractOneWireDevicePropertyBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super();
		parseBindingConfig(pvBindingConfig);
	}

	private void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		String[] pvConfigParts = pvBindingConfig.trim().split(";");

		for (String pvConfigPart : pvConfigParts) {
			parseDeviceId(pvConfigPart);
			parsePropertyName(pvConfigPart);
			parseRefreshInterval(pvConfigPart);
		}

		// DeviceId and property must be filled
		if (this.ivDeviceId == null || this.ivDeviceId.trim().equals("") || this.ivPropertyName == null || this.ivPropertyName.trim().equals("")) {
			LOGGER.error("deviceId and propertyName not set in config!");
			throw new BindingConfigParseException("Onewire sensor configuration must contain at least the deviceId and the propertyName");
		}
	}

	private void parseDeviceId(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "deviceId=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			this.setDeviceId(lvConfigValue);
		}
	}

	private void parsePropertyName(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "propertyName=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			this.setPropertyName(lvConfigValue);
		}
	}

	private void parseRefreshInterval(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "refreshinterval=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			this.setAutoRefreshInSecs(Integer.parseInt(lvConfigValue));
		}
	}

	public String getDeviceId() {
		return ivDeviceId;
	}

	public void setDeviceId(String pvDeviceId) {
		this.ivDeviceId = pvDeviceId;
	}

	public String getPropertyName() {
		return ivPropertyName;
	}

	public void setPropertyName(String pvPropertyName) {
		this.ivPropertyName = pvPropertyName;
	}

	public int getAutoRefreshInSecs() {
		return ivAutoRefreshInSecs;
	}

	public void setAutoRefreshInSecs(int pvAutoRefreshInSecs) {
		this.ivAutoRefreshInSecs = pvAutoRefreshInSecs;
	}

	/**
	 * @return deviceId + / + propertyName
	 */
	public String getDevicePropertyPath() {
		return new StringBuffer().append(getDeviceId()).append("/").append(getPropertyName()).toString();
	}

	/**
	 * @return a list of configured modifiers for this 1-Wire property device binding 
	 */
	public ArrayList<InterfaceOneWireTypeModifier> getTypeModifieryList() {
		return ivTypeModifieryList;
	}

	/**
	 * 
	 * @param pvReadValue
	 * @return the modified and converted given readValue String as a openHab-Type 
	 */
	public Type convertReadValueToType(String pvReadValue) {
		Type lvType = convertReadValueToUnmodifiedType(pvReadValue);

		for (InterfaceOneWireTypeModifier lvTypeModifier : getTypeModifieryList()) {
			LOGGER.debug("type of " + getDevicePropertyPath() + " before modifier:" + lvTypeModifier.getModifierName() + "type=" + lvType.toString());
			lvType = lvTypeModifier.modify4Read(lvType);
			LOGGER.debug("type of " + getDevicePropertyPath() + " after modifier:" + lvTypeModifier.getModifierName() + "type=" + lvType.toString());
		}

		return lvType;
	}

	/**
	 * Abstract method, which must be implemented by specialized Classes
	 * 
	 * @param pvReadValue
	 * @return converts the given readValue to an unmodified openHab Type
	 */
	abstract protected Type convertReadValueToUnmodifiedType(String pvReadValue);

	@Override
	public String toString() {
		final int maxLen = 20;
		return "AbstractOneWireDevicePropertyBindingConfig [deviceId=" + ivDeviceId + ", propertyName=" + ivPropertyName + ", autoRefreshInSecs=" + ivAutoRefreshInSecs + ", typeModifieryList="
				+ (ivTypeModifieryList != null ? ivTypeModifieryList.subList(0, Math.min(ivTypeModifieryList.size(), maxLen)) : null) + "]";
	}
}
