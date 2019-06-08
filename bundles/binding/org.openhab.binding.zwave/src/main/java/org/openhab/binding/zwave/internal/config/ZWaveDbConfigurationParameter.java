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
package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Implements the configuration parameters for the XML product database
 *
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbConfigurationParameter {
    public Integer Index;
    public String Type;
    public String Default;
    public Integer Size;
    public Integer Minimum;
    public Integer Maximum;
    public String Units;
    public Boolean ReadOnly;
    public Boolean WriteOnly;
    @XStreamImplicit
    public List<ZWaveDbLabel> Label;
    @XStreamImplicit
    public List<ZWaveDbLabel> Help;
    @XStreamImplicit
    public List<ZWaveDbConfigurationListItem> Item;
}
