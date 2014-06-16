/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.energenie.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import org.openhab.binding.energenie.EnergenieBindingProvider;
import org.openhab.binding.energenie.internal.EnergenieBindingConfig;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public class EnergenieBinding extends AbstractActiveBinding<EnergenieBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(EnergenieBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the Energenie
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|password)$");


	protected Map<String, DeviceConfig> deviceConfigs = new HashMap<String, DeviceConfig>();

    protected Map<String, String> pmsIpConfig = new HashMap<String, String>();
    
    protected Map<String, String> pmsPasswordConfig = new HashMap<String, String>();
    
    private final String USER_AGENT = "Mozilla/5.0";
    
	public EnergenieBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Energenie Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");

		for (EnergenieBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				EnergenieBindingConfig deviceConfig = getConfigForItemName(itemName);
				if (deviceConfig == null) {
					return;
				}
				try {
					String pmsId = deviceConfig.getDeviceId();
					int pmsSocketId = deviceConfig.getSocketNumber();

					String pmsIp = pmsIpConfig.get(pmsId);
					String pmsPw = pmsPasswordConfig.get(pmsId);
					String url = "http://"+pmsIp+"/login.html";
					
					try {
						URL obj = new URL(url);
						HttpURLConnection con = (HttpURLConnection) obj.openConnection();
						
						//add request header
						con.setRequestProperty("User-Agent", USER_AGENT);
						con.setRequestMethod("GET");
						
						String urlParameters = "pw="+pmsPw;
						
						// Send get request
						con.setDoOutput(true);
						DataOutputStream wr = new DataOutputStream(con.getOutputStream());
						wr.writeBytes(urlParameters);
						wr.flush();
						wr.close();
						
						int responseCode = con.getResponseCode();
						logger.trace("energenie: Login to {} with Password {}", pmsIp, pmsPw);
						logger.trace("energenie: Sending 'POST' request to URL : {}", url);
						logger.trace("energenie: Post parameters : {}", urlParameters);
						logger.trace("energenie: ResponseCode : {}", responseCode);
						
						BufferedReader in = new BufferedReader(
						        new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer responseString = new StringBuffer();
				 
						while ((inputLine = in.readLine()) != null) {
							responseString.append(inputLine);
						}
						in.close();
				 
						String loginResponseString = responseString.toString();
						
						String stateResponseSearch = "var sockstates = ";
						int findState=loginResponseString.lastIndexOf(stateResponseSearch);
						if (findState !=0) {
							logger.trace("energenie: searchstring sockstates found at position {}", findState);
							
							String slicedResponse = loginResponseString.substring(findState+18, findState+25);
							
							logger.trace("energenie: transformed state response = {}", slicedResponse);

							try {
								String [] parts = slicedResponse.split(",");
								String itemState = parts[pmsSocketId-1];
								logger.trace("energenie: Response for item {} = {}", itemName, itemState);
								if (itemState.equals("0")) {
									State state = OnOffType.valueOf("OFF");
									logger.trace("energenie: transformed state for item {} = {}", itemName, state);
									eventPublisher.postUpdate(itemName, state);
								}
								if (itemState.equals("1")) {
									State state = OnOffType.valueOf("ON");
									logger.trace("energenie: transformed state for item {} = {}", itemName, state);
									eventPublisher.postUpdate(itemName, state);
								}
							
							} catch (Exception te) {
								logger.error("Response transformation throws exception ", te);
							}
							
						} else {
							logger.trace("energenie: searchstring sockstates not found");			
						}
						
					} catch (Exception e) {
						logger.error("energenie: Failed to logout from ip {}", pmsIp);
					}


					sendLogOut(pmsId);
			
				} catch (Exception e) {
					logger.error("energenie: failed to read state", e);
				}

			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");
		
		EnergenieBindingConfig deviceConfig = getConfigForItemName(itemName);
		if (deviceConfig == null) {
			return;
		}
		try {
			String pmsId = deviceConfig.getDeviceId();
			int pmsSocketId = deviceConfig.getSocketNumber();
			
			sendLogin(pmsId);
			if (OnOffType.ON.equals(command)) {
				sendOn(pmsId, pmsSocketId);
			} else if (OnOffType.OFF.equals(command)) {
				sendOff(pmsId, pmsSocketId);
			}
			sendLogOut(pmsId);
	
		} catch (Exception e) {
			logger.error("energenie: failed to send {} command", command, e);
		}
	}

	private void sendLogin(String pmsId) {
		String pmsIp = pmsIpConfig.get(pmsId);
		String pmsPw = pmsPasswordConfig.get(pmsId);
		String url = "http://"+pmsIp+"/login.html";
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestMethod("GET");
			
			String urlParameters = "pw="+pmsPw;
			
			// Send get request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			logger.trace("energenie: login to {} with password {}", pmsIp, pmsPw);
			logger.trace("energenie: sending 'POST' request to URL : {}", url);
			logger.trace("energenie: post parameters : {}", urlParameters);
			logger.trace("energenie: responseCode : {}", responseCode);
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseString = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				responseString.append(inputLine);
			}
			in.close();
	 
			String loginResponseString = responseString.toString();
			
			String stateResponseSearch = "var sockstates = ";
			int findState=loginResponseString.lastIndexOf(stateResponseSearch);
			if (findState !=0) {
				logger.trace("energenie: searchstring sockstates found at position {}", findState);
				
				String slicedResponse = loginResponseString.substring(findState+18, findState+25);
				
				logger.trace("energenie: transformed state response = {}", slicedResponse);
				
			} else {
				logger.trace("energenie: searchstring sockstates not found");			
			}
			
		} catch (Exception e) {
			logger.error("energenie: failed to logout from ip {}", pmsIp);
		}

	}

	private void sendOn(String pmsId, int pmsSocketId) {
		String pmsIp = pmsIpConfig.get(pmsId);
		String url = "http://"+pmsIp;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestMethod("POST");
			
			String urlParameters = "cte"+pmsSocketId+"=1";
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			logger.trace("energenie: sending 'POST' request to URL : {}", url);
			logger.trace("energenie: post parameters : {}", urlParameters);
			logger.trace("energenie: responseCode : {}", responseCode);

			
			logger.trace("energenie: send command ON to socket {} at host {}", pmsSocketId, pmsIp);

			
		} catch (Exception e) {
			logger.error("energenie: failed so send command ON to socket {} at ip {}", pmsIp, pmsSocketId);
		}
		
	}
	
	private void sendOff(String pmsId, int pmsSocketId) {
		String pmsIp = pmsIpConfig.get(pmsId);
		String url = "http://"+pmsIp;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestMethod("POST");
			
			String urlParameters = "cte"+pmsSocketId+"=0";
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			logger.info("energenie: sending 'POST' request to URL : {}", url);
			logger.info("energenie: post parameters : {}", urlParameters);
			logger.info("energenie: responseCode : {}", responseCode);

			
			logger.info("energenie: send command OFF to socket {} at host {}", pmsSocketId, pmsIp);

			
		} catch (Exception e) {
			logger.error("energenie: failed so send command OFF to socket {} at ip {}", pmsIp, pmsSocketId);
		}
		
	}

	private void sendLogOut(String pmsId) {
		String pmsIp = pmsIpConfig.get(pmsId);
		String url = "http://"+pmsIp;

		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestMethod("GET");
			
			// Send get request
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			logger.trace("energenie: responseCode : {}", responseCode);
			logger.trace("energenie: logout from ip {}", pmsIp);
			
		} catch (Exception e) {
			logger.error("energenie: failed to logout from ip {}", pmsIp);
		}

	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveCommand() is called!");

	}

	/**
	* Lookup of the configuration of the named item.
	* 
 	* @param itemName
 	*            The name of the item.
 	* @return The configuration, null otherwise.
 	*/
	private EnergenieBindingConfig getConfigForItemName(String itemName) {
		for (EnergenieBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '" + key
						+ "' does not follow the expected pattern '<id>.<host|password>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigs.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigs.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
					pmsIpConfig.put(deviceId, value);
				} else if ("password".equals(configKey)) {
					deviceConfig.password = value;
					pmsPasswordConfig.put(deviceId, value);
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}			
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			setProperlyConfigured(true);
		}
	}
	
	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {
		
		String host;
		String password;
		String deviceId;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getHost(){
			return host;
		}

		public String getPassword(){
			return password;
		}

		
		@Override
		public String toString() {
			return "Device [id=" + deviceId + "]";
		}
	}
}
