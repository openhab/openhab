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
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PTC Connected</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.PTCConnected#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCConnected()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.PTCDevice
 *        org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.DigitalValue>"
 * @generated
 */
public interface PTCConnected extends PTCDevice, MSensor<HighLowValue> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"ptc_connected"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCConnected_DeviceType()
     * @model default="ptc_connected" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

} // PTCConnected
