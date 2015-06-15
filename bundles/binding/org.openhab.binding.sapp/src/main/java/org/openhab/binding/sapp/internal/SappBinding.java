/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.paolodenti.jsapp.core.command.Sapp7DCommand;
import com.github.paolodenti.jsapp.core.command.base.SappCommand;
import com.github.paolodenti.jsapp.core.command.base.SappException;
import com.github.paolodenti.jsapp.core.util.SappUtils;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Paolo Denti
 * @since 1.0.0
 */
public class SappBinding extends AbstractActiveBinding<SappBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(SappBinding.class);

	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_PNMAS_ENABLED = "pnmas.ids";
	private static final String CONFIG_KEY_PNMAS_ID = "pnmas.%s.ip";
	private static final String CONFIG_KEY_PNMAS_PORT = "pnmas.%s.port";

	/**
	 * map of existing pnmas. key is pnmas id.
	 */
	private Map<String, SappPnmas> pnmasMap = new HashMap<>();

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the Sapp server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public SappBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {

		logger.debug("sapp activate called");
		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get(CONFIG_KEY_REFRESH);
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			logger.debug("set refresh interval: " + refreshInterval);
		}

		String pnmasEnabled = (String) configuration.get(CONFIG_KEY_PNMAS_ENABLED);
		if (pnmasEnabled != null) {
			String[] pnmasIds = pnmasEnabled.split(",");
			for (String pnmasId : pnmasIds) {
				logger.debug(String.format("loading info for pnmas %s", pnmasId));

				String ip = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_ID, pnmasId));
				if (ip == null) {
					logger.warn(String.format("ip not found for pnmas %s", pnmasId));
					continue;
				}

				int port;
				String portString = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_PORT, pnmasId));
				if (portString == null) {
					logger.warn(String.format("port not found for pnmas %s", pnmasId));
					continue;
				} else {
					try {
						port = Integer.parseInt(portString);
					} catch (NumberFormatException e) {
						logger.warn(String.format("bad port number for pnmas %s", pnmasId));
						continue;
					}
				}

				if (pnmasMap.containsKey(pnmasId)) {
					logger.warn(String.format("pnmas %s duplicated, skipping", pnmasId));
					continue;
				}

				pnmasMap.put(pnmasId, new SappPnmas(ip, port));
			}
			for (String pnmasKey : pnmasMap.keySet()) {
				logger.debug(String.format("pnmas %s : %s:", pnmasKey, pnmasMap.get(pnmasKey)));
			}
		}

		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {

		// update the internal configuration accordingly
		logger.debug("modified called");
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {

		logger.debug("sapp deactivate called");
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
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

		return "Sapp Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {

		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		for (SappBindingProvider provider : providers) {
			logger.debug("found provider: " + provider.getClass());
			if (!provider.providesBindingFor(itemName))
				continue;

			SappBindingConfig bindingConfig = provider.getBindingConfig(itemName);
			logger.debug("found binding " + bindingConfig);

			if (!pnmasMap.containsKey(bindingConfig.getPnmasId())) {
				logger.warn(String.format("bad pnmas id (%s) in binding (%s) ... skipping", bindingConfig.getPnmasId(), bindingConfig));
				continue;
			}

			executeCommand(bindingConfig, command);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	/**
	 * executes the real command on pnmas device
	 */
	private void executeCommand(SappBindingConfig bindingConfig, Command command) {

		try {
			if (command instanceof OnOffType) { // set bit
				switch (bindingConfig.getAddressType()) {
				case ALARM:
				case INPUT:
				case OUTPUT: {
					logger.error("cannot run " + command.getClass().getSimpleName() + " on " + bindingConfig.getAddressType() + " type");
					break;
				}

				case VIRTUAL: {
					SappPnmas pnmas = pnmasMap.get(bindingConfig.getPnmasId());
					SappCommand sappCommand = new Sapp7DCommand(bindingConfig.getAddress(), command.equals(OnOffType.ON) ? 1 : 0);
					sappCommand.run(pnmas.getIp(), pnmas.getPort());
					break;
				}

				default:
					break;
				}
			} else { // TODO

			}
		} catch (SappException e) {
			logger.error("could not run sappcommand: " + e.getMessage());
		}
	}

}
