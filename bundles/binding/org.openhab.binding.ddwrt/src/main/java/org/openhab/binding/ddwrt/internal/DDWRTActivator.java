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
package org.openhab.binding.ddwrt.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 *
 * @author Kai Kreuzer
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */
public class DDWRTActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(DDWRTActivator.class);

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        logger.debug("DD-WRT binding has been activated.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        logger.debug("DD-WRT binding has been deactivated.");
    }

}
