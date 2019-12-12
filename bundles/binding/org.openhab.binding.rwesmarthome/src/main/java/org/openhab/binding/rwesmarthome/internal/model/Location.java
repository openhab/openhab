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
package org.openhab.binding.rwesmarthome.internal.model;

/**
 * Class to hold an RWE Smarthome location.
 *
 * @author ollie-dev
 *
 */
public class Location {

    private String id;
    private String name;

    /**
     * Constructor with id and name.
     * 
     * @param id
     * @param name
     */
    public Location(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the location id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the location id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name of the location.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the location.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
