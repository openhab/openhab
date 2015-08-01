package org.openhab.binding.sapp.internal;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

public class SappBindingConfigSwitchItem extends SappBindingConfig {

	private SappAddressOnOffStatus status;
	private SappAddressOnOffControl control;

	public SappBindingConfigSwitchItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());

		String[] bindingConfigParts = bindingConfig.split("/");
		if (bindingConfigParts.length != 2) {
			throw new BindingConfigParseException(errorMessage(bindingConfig));
		}

		this.status = parseSappAddressStatus(bindingConfigParts[0]);
		this.control = parseSappAddressControl(bindingConfigParts[1]);
	}

	public SappAddressOnOffStatus getStatus() {
		return status;
	}

	public SappAddressOnOffControl getControl() {
		return control;
	}

	@Override
	public String toString() {
		return String.format("[itemName:%s: status:%s - control: %s ]", getItemName(), this.status.toString(), this.control.toString());
	}

	private String errorMessage(String bindingConfig) {
		return String.format("Invalid Sapp binding configuration for SwitchItem '%s'", bindingConfig);
	}

	private SappAddressOnOffStatus parseSappAddressStatus(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int onValue = 1;

		String[] bindingAddress = bindingStringAddress.split(":");
		if (bindingAddress.length != 4 && bindingAddress.length != 5) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// pnmasId
		pnmasId = bindingAddress[0];
		if (pnmasId.length() == 0) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// addressType
		addressType = SappAddressType.fromString(bindingAddress[1].toUpperCase());
		if (!validAddresses.keySet().contains(addressType)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// address
		try {
			address = Integer.parseInt(bindingAddress[2]);
			if (address < validAddresses.get(addressType).getLoRange() || address > validAddresses.get(addressType).getHiRange()) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// subaddress
		subAddress = bindingAddress[3].toUpperCase();
		if (!ArrayUtils.contains(validSubAddresses, subAddress)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// onvalue, offvalue
		if (bindingAddress.length == 5) {
			try {
				onValue = Integer.parseInt(bindingAddress[4]);
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		}

		return new SappAddressOnOffStatus(pnmasId, addressType, address, subAddress, onValue);
	}

	private SappAddressOnOffControl parseSappAddressControl(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int onValue = 1;
		int offValue = 0;

		String[] bindingAddress = bindingStringAddress.split(":");
		if (bindingAddress.length != 4 && bindingAddress.length != 6) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// pnmasId
		pnmasId = bindingAddress[0];
		if (pnmasId.length() == 0) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// addressType
		addressType = SappAddressType.fromString(bindingAddress[1].toUpperCase());
		if (!validAddresses.keySet().contains(addressType)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}
		if (addressType != SappAddressType.VIRTUAL) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// address
		try {
			address = Integer.parseInt(bindingAddress[2]);
			if (address < validAddresses.get(addressType).getLoRange() || address > validAddresses.get(addressType).getHiRange()) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// subaddress
		subAddress = bindingAddress[3].toUpperCase();
		if (!ArrayUtils.contains(validSubAddresses, subAddress)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// onvalue, offvalue
		if (bindingAddress.length == 6) {
			try {
				onValue = Integer.parseInt(bindingAddress[4]);
				offValue = Integer.parseInt(bindingAddress[5]);
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		}

		return new SappAddressOnOffControl(pnmasId, addressType, address, subAddress, onValue, offValue);
	}
}
