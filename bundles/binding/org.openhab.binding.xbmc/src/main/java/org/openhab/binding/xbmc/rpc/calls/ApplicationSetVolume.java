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

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.Stop RPC
 *
 * @author Ard van der Leeuw
 * @since 1.6.0
 */
public class ApplicationSetVolume extends RpcCall {

    private Integer volume;

    public ApplicationSetVolume(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    @Override
    protected String getName() {
        return "Application.SetVolume";
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    @Override
    protected Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("volume", volume);
        return params;
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }
}
