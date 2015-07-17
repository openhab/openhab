/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.openhab.binding.ulux.UluxBindingConfig.MESSAGE_ACTIVATE;
import static org.openhab.binding.ulux.UluxBindingConfig.MESSAGE_AUDIO_PLAY_LOCAL;
import static org.openhab.binding.ulux.UluxBindingConfig.MESSAGE_PAGE_INDEX;
import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.net.InetAddress;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Type;

/**
 * A factory for {@link UluxDatagram}s.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxDatagramFactory {

	private final UluxConfiguration configuration;

	private final UluxMessageFactory messageFactory = new UluxMessageFactory();

	/**
	 * Constructs a datagram factory for the given configuration.
	 */
	public UluxDatagramFactory(final UluxConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(short switchId, InetAddress sourceAddress) {
		return new UluxDatagram(switchId, sourceAddress);
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(UluxBindingConfig config) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = this.configuration.getSwitchAddress(switchId);

		return createDatagram(switchId, switchAddress);
	}

	/**
	 * Creates a datagram containing a message for the given command or update.
	 * 
	 * @return never {@code null}
	 */
	public UluxDatagram createDatagram(UluxBindingConfig config, Type type) {
		final UluxDatagram datagram = createDatagram(config);
		final UluxMessage message;

		if (type instanceof DateTimeType) {
			message = createMessage(config, (DateTimeType) type);
		} else if (type instanceof DecimalType) {
			message = createMessage(config, (DecimalType) type);
		} else if (type instanceof IncreaseDecreaseType) {
			message = createMessage(config, (IncreaseDecreaseType) type);
		} else if (type instanceof OnOffType) {
			message = createMessage(config, (OnOffType) type);
		} else if (type instanceof OpenClosedType) {
			message = createMessage(config, (OpenClosedType) type);
		} else if (type instanceof StopMoveType) {
			message = createMessage(config, (StopMoveType) type);
		} else if (type instanceof UpDownType) {
			message = createMessage(config, (UpDownType) type);
		} else {
			message = null;
		}

		if (message != null) {
			datagram.addMessage(message);
		} else {
			LOG.warn("No message created for unsupported type '{}'!", type);
		}

		return datagram;
	}

	private UluxMessage createMessage(UluxBindingConfig config, DateTimeType type) {
		return null; // TODO
	}

	private UluxMessage createMessage(UluxBindingConfig config, DecimalType type) {
		if (equalsIgnoreCase(config.getMessage(), MESSAGE_PAGE_INDEX)) {
			return messageFactory.createPageIndexMessage(type);
		} else if (equalsIgnoreCase(config.getMessage(), MESSAGE_AUDIO_PLAY_LOCAL)) {
			return messageFactory.createAudioPlayLocalMessage(type);
		} else {
			return messageFactory.createEditValueMessage(config, type);
		}
	}

	private UluxMessage createMessage(UluxBindingConfig config, IncreaseDecreaseType type) {
		return null; // TODO
	}

	private UluxMessage createMessage(UluxBindingConfig config, OnOffType type) {
		if (equalsIgnoreCase(config.getMessage(), MESSAGE_ACTIVATE)) {
			return messageFactory.createActivateMessage(type);
		} else {
			return messageFactory.createEditValueMessage(config, type);
		}
	}

	private UluxMessage createMessage(UluxBindingConfig config, OpenClosedType type) {
		return null; // TODO
	}

	private UluxMessage createMessage(UluxBindingConfig config, StopMoveType type) {
		return null; // TODO
	}

	private UluxMessage createMessage(UluxBindingConfig config, UpDownType type) {
		return null; // TODO
	}

}
