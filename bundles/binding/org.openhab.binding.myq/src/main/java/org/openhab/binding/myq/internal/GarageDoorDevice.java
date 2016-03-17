/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import org.codehaus.jackson.JsonNode;

/**
 * This Class holds the Garage Door Opener Device data and extends MyqDevice.
 * <ul>
 * <li>Status: Garage Door Opener "doorstate" Attribute</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @author Dan Cunningham
 * @since 1.8.0
 */
class GarageDoorDevice extends MyqDevice {

	private GarageDoorStatus status;

	public GarageDoorDevice(int deviceId, String deviceType,int deviceTypeID,
	JsonNode deviceJson) {
		super(deviceId, deviceType, deviceTypeID, deviceJson);
		JsonNode attributes = deviceJson.get("Attributes");
		if (attributes.isArray()) {
			int attributesSize = attributes.size();
			for (int j = 0; j < attributesSize; j++) {
				String attributeName = attributes.get(j)
						.get("AttributeDisplayName").asText();
				if (attributeName.contains("doorstate")) {
					int doorstate = attributes.get(j)
							.get("Value").asInt();
					this.status = GarageDoorStatus.GetDoorStatus(doorstate);
					deviceAttributes.put("UpdatedDate", attributes.get(j)
						.get("UpdatedDate").asText());
					logger.debug(
							"GarageDoorOpener DeviceID: {} DeviceType: {} Doorstate : {}",
							deviceId, deviceType, doorstate);
					break;
				}
			}
		}
	}

	public GarageDoorStatus getStatus() {
		return this.status;
	}

	public enum GarageDoorStatus {
		OPEN("Open", 1), CLOSED("Closed", 2), PARTIAL("Partially Open/Closed",
				3), OPENING("Opening", 4), CLOSING("Closing", 5), UNKNOWN(
				"Unknown", -1);

		/**
		 * The label used to display status to a user
		 */
		private String label;
		/**
		 * The int value returned from the MyQ API
		 */
		private int value;

		private GarageDoorStatus(String label, int value) {
			this.label = label;
			this.value = value;
		}

		/**
		 * Label for the door status
		 * 
		 * @return human readable label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Int value of the door status
		 * 
		 * @return int value of the door status
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Is the door in a closed or closing state
		 * 
		 * @return is closed or is closing
		 */
		public boolean isClosedOrClosing() {
			return (this == CLOSED || this == CLOSING);
		}

		/**
		 * Is the door in motion
		 * 
		 * @return door in motion
		 */
		public boolean inMotion() {
			return (this == OPENING || this == CLOSING);
		}

		/**
		 * Lookup a door status by its int value
		 * 
		 * @param value
		 * @return a door status enum
		 */
		public static GarageDoorStatus GetDoorStatus(int value) {
			for (GarageDoorStatus ds : values()) {
				if (ds.getValue() == value)
					return ds;
			}
			return UNKNOWN;
		}
	}
}
