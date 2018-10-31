/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.json;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.comm.GetWLANConfig;
import org.openhab.binding.velux.things.VeluxGwWLAN;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Retrieval of WLAN configuration.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.json.JsonBridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class JCgetWLANConfig extends GetWLANConfig implements JsonBridgeCommunicationProtocol  {

	private static String url = "/api/v1/settings";
	private static String description = "get WLAN configuration";

    private Request request = new Request();
    private Response response;
    

	/*
	 * Message Objects
	 */

	/**
	 * Bridge I/O Request message used by {@link org.openhab.binding.velux.bridge.comm.json.JsonVeluxBridge} for serializing:
	 * <P>
	 * Resulting JSON:
	 * <pre>
	 * {"action":"wifi","params":{}}
	 * </pre>
	 */
	public static class Request {

		@SuppressWarnings("unused")
		private String action;

		@SuppressWarnings("unused")
		private Map<String, String> params;

		public Request() {
			this.action = "wifi";
			this.params = new HashMap<String, String>();
		}
	}

	/**
	 * Bridge Communication Structure containing the version of the firmware.
	 * <P>
	 * Used within structure {@link JCgetWLANConfig} to describe the network connectivity of the Bridge.
	 */
	public static class BCWLANConfig {
		/*
		 * {"password":"Esf56mxqFY","name":"VELUX_KLF_847C"}
		 */
		private String password;
		private String name;

		public String getPassword() {
			return this.password;
		}

		public String getSSID() {
			return this.name;
		}

		@Override
		public String toString() {
			return String.format("SSID=%s,password=********", this.name);
		}
	}

	/**
	 * Bridge I/O Response message used by {@link JsonBridgeCommunicationProtocol} for deserialization with including component access
	 * methods
	 * <P>
	 * Expected JSON (sample):
	 * <pre>
	 * {
	 *  "token":"RHIKGlJyZhidI/JSK0a2RQ==",
	 *  "result":true,
	 *  "deviceStatus":"IDLE",
	 *  "data":{"password":"Esf56mxqFY","name":"VELUX_KLF_847C"},
	 *  "errors":[]
	 * }
	 * </pre>
	 */
	public static class Response {
		private String token;
		private boolean result;
		private String deviceStatus;
		private BCWLANConfig data;
		private String[] errors;

		public String getToken() {
			return token;
		}

		public boolean getResult() {
			return result;
		}

		public String getDeviceStatus() {
			return deviceStatus;
		}

		public BCWLANConfig getWLANConfig() {
			return data;
		}

		public String[] getErrors() {
			return errors;
		}
		/*
		 * ===========================================================
		 * Methods required for interface {@link org.openhab.binding.velux.bridge.comm.BCgetWLANConfig}.
		 */
		public String getWLANSSID() {
			return data.getSSID();
		}
		public String getWLANPassword() {
			return data.getPassword();
		}
		public String toString() {
			return data.toString();
		}
	}


	/*
	 * ===========================================================
	 * Methods required for interface {@link BridgeCommunicationProtocol}.
	 */

	@Override
	public String name() {
		return description;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public Object getObjectOfRequest() {
		return request;
	}

	@Override
	public Class<Response> getClassOfResponse() {
		return Response.class;
	}


	@Override
	public void setResponse(Object response) {
		this.response = (Response) response;
	}

	@Override
	public boolean isCommunicationSuccessful() {
		return response.getResult();
	}

	@Override
	public String getDeviceStatus() {
		return response.deviceStatus;
	}

	@Override
	public String[] getErrors() {
		return response.errors;
	}
	
	/**
	 * ===========================================================
	 * <P>Public Methods required for abstract class {@link org.openhab.binding.velux.bridge.comm.GetWLANConfig}.
	 */
	public VeluxGwWLAN getWLANConfig() {
		VeluxGwWLAN gwWLAN = new VeluxGwWLAN(response.data.name, response.data.password);
		return gwWLAN;
	}

}
/**
 * end-of-bridge/comm/BCgetWLANConfig.java
 */
