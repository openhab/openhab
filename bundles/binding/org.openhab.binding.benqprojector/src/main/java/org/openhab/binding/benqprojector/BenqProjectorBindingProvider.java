/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.benqprojector;

import org.openhab.binding.benqprojector.internal.BenqProjectorBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface BenqProjectorBindingProvider extends BindingProvider {

    public BenqProjectorBindingConfig getConfigForItemName(String itemName);
}
