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
package org.openhab.binding.xbmc.rpc.calls;

import java.util.Collections;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * System.Hibernate RPC
 *
 * @author Ard van der Leeuw
 * @since 1.6.0
 */
public class SystemHibernate extends RpcCall {

    public SystemHibernate(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    @Override
    protected String getName() {
        return "System.Hibernate";
    }

    @Override
    protected Map<String, Object> getParams() {
        return Collections.emptyMap();
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }
}
