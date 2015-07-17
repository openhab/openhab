/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UluxException(final String message) {
		super(message);
	}

	public UluxException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UluxException(final Throwable cause) {
		super(cause);
	}

}
