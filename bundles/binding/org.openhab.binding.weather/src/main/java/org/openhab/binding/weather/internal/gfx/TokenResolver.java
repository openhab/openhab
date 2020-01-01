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
package org.openhab.binding.weather.internal.gfx;

/**
 * Interface which is used by TokenReplacingReader to replace a token in a
 * stream.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface TokenResolver {

    /**
     * Returns the value to which the token is replaced, null to not replace the
     * token.
     */
    public String resolveToken(String tokenName);

}
