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
package org.openhab.binding.digitalstrom.internal.client.entity;

import java.util.List;

/**
 * @author Alexander Betker
 * @since 1.3.0
 */
public interface Zone {

    public int getZoneId();

    public void setZoneId(int id);

    public String getName();

    public void setName(String name);

    public List<DetailedGroupInfo> getGroups();

    public void addGroup(DetailedGroupInfo group);

    public List<Device> getDevices();

    public void addDevice(Device device);

}
