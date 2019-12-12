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
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dual Button LED Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLEDConfiguration#isAutotoggle
 * <em>Autotoggle</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonLEDConfiguration()
 * @model
 * @generated
 */
public interface DualButtonLEDConfiguration extends TFConfig {
    /**
     * Returns the value of the '<em><b>Autotoggle</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Autotoggle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Autotoggle</em>' attribute.
     * @see #setAutotoggle(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDualButtonLEDConfiguration_Autotoggle()
     * @model default="false" unique="false"
     * @generated
     */
    boolean isAutotoggle();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.DualButtonLEDConfiguration#isAutotoggle
     * <em>Autotoggle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Autotoggle</em>' attribute.
     * @see #isAutotoggle()
     * @generated
     */
    void setAutotoggle(boolean value);

} // DualButtonLEDConfiguration
