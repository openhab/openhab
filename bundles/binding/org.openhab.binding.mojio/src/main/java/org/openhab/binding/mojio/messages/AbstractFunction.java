/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.openhab.io.net.http.HttpUtil.executeUrl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.mojio.internal.MojioException;

/**
 * The function object is defined by its functionType and one or more additional properties. The property list is
 * variable depending on the type of function.
 * 
 * <p>
 * Functions are used to perform more complex operations on a thermostat or user which are too complex with simple
 * property modifications. Functions are used to modify read-only objects where appropriate.
 * 
 * <p>
 * Each function takes different parameters.
 * 
 * @see <a href="https://www.mojio.com/home/developer/api/documentation/v1/objects/Function.shtml">Function</a>
 * @author John Cocula
 * @since 1.7.0
 */
public abstract class AbstractFunction extends AbstractRequest {

	// TODO needs to be in specific thermostat's local timezone (@watou)
	protected final static DateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	protected final static DateFormat hms = new SimpleDateFormat("HH:mm:ss");

  private String method;
	private String token;

	/**
	 * Construct a function of given type with zero params.
	 * 
	 * @param type
	 *            the function type name
	 */
	protected AbstractFunction(String method, String mojioToken) {
    this.method = method;
    this.token = mojioToken;
		HTTP_HEADERS.put("MojioAPIToken", this.token);
	}

	protected String executeQuery(final String url) {
		return executeUrl(this.method, url, HTTP_HEADERS, null, null, HTTP_REQUEST_TIMEOUT);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("Token", this.token);

		return builder.toString();
	}
}
