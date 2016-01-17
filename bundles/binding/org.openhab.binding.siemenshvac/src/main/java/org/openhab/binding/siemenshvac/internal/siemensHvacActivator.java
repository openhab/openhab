/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.siemenshvac.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Laurent ARNAL
 */
public final class siemensHvacActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(siemensHvacActivator.class);

	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		logger.debug("siemensHvac:Siemens hvac binding has been started.");
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		logger.debug("siemensHvac:Siemens hvac binding has been stopped.");
	}

}