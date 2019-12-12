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

import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Implements the top level class for the product file
 *
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbProductFile {
    public String Model;
    public Integer Endpoints;
    @XStreamImplicit
    public List<ZWaveDbLabel> Label;

    public ZWaveDbCommandClassList CommandClasses;

    public ZWaveDbConfiguration Configuration;
    public ZWaveDbAssociation Associations;

    List<ZWaveDbConfigurationParameter> getConfiguration() {
        final List<ZWaveDbConfigurationParameter> params;
        if (Configuration == null) {
            params = Collections.emptyList();
        } else {
            params = Configuration.Parameter;
        }
        return params;
    }

    List<ZWaveDbAssociationGroup> getAssociations() {
        final List<ZWaveDbAssociationGroup> groups;
        if (Associations == null) {
            groups = Collections.emptyList();
        } else {
            groups = Associations.Group;
        }
        return groups;
    }

    class ZWaveDbCommandClassList {
        @XStreamImplicit
        public List<ZWaveDbCommandClass> Class;
    }

    class ZWaveDbConfiguration {
        @XStreamImplicit
        public List<ZWaveDbConfigurationParameter> Parameter;
    }

    class ZWaveDbAssociation {
        @XStreamImplicit
        List<ZWaveDbAssociationGroup> Group;
    }
}
