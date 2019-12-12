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
package org.openhab.binding.hms;

import org.openhab.binding.hms.internal.HMSGenericBindingProvider.HMSBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface extends BindingProvider and provides a new method for getting
 * a HMS binding configuration by address string and datapoint.
 *
 * @author Thomas Urmann
 * @since 1.7.0
 */
public interface HMSBindingProvider extends BindingProvider {
    public HMSBindingConfig getBindingConfigForAddressAndDatapoint(String address,
            HMSBindingConfig.Datapoint datapoint);
}
