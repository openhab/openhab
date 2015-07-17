/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.UluxBindingProvider;
import org.openhab.binding.ulux.internal.audio.AudioChannel;
import org.openhab.binding.ulux.internal.handler.UluxCommandHandler;
import org.openhab.binding.ulux.internal.handler.UluxMessageHandlerFacade;
import org.openhab.binding.ulux.internal.handler.UluxStateUpdateHandler;
import org.openhab.binding.ulux.internal.ump.UluxDatagramFactory;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessageFactory;
import org.openhab.binding.ulux.internal.ump.UluxMessageParser;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class UluxBinding extends AbstractBinding<UluxBindingProvider> implements ManagedService, Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(UluxBinding.class);

	private static final int BUFFER_SIZE = 1024;

	public static final int AUDIO_PORT = 0x88A4;

	// private static final int MANAGEMENT_PORT = 0x88A8;

	public static final int PORT = 0x88AC;

	private final UluxCommandHandler commandHandler;

	private final UluxStateUpdateHandler stateUpdateHandler;

	private final UluxMessageHandlerFacade messageHandler;

	private final UluxMessageParser messageParser;

	private UluxConfiguration configuration;

	private final UluxMessageFactory messageFactory;

	private final UluxDatagramFactory datagramFactory;

	private DatagramChannel channel;

	private volatile Thread thread;

	private ScheduledExecutorService executorService;

	private AudioChannel audioChannel;

	public UluxBinding() {
		this.configuration = new UluxConfiguration();

		this.messageFactory = new UluxMessageFactory();
		this.datagramFactory = new UluxDatagramFactory(configuration);
		this.commandHandler = new UluxCommandHandler(configuration, messageFactory, datagramFactory);
		this.stateUpdateHandler = new UluxStateUpdateHandler(configuration, messageFactory, datagramFactory);

		this.messageHandler = new UluxMessageHandlerFacade(this.providers);
		this.messageParser = new UluxMessageParser(messageFactory);
	}

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);

		this.messageHandler.setEventPublisher(eventPublisher);
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.messageHandler.setItemRegistry(itemRegistry);
	}

	@Override
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		super.unsetEventPublisher(eventPublisher);

		this.messageHandler.setEventPublisher(null);
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.messageHandler.setItemRegistry(null);
	}

	@Override
	public void activate() {
		LOG.info("u::Lux binding activated");

		// this.executorService = Executors.newCachedThreadPool();
		this.executorService = Executors.newScheduledThreadPool(1);

		if (this.configuration.isConfigured()) {
			startListenerThread();
		}
	}

	@Override
	public void deactivate() {
		LOG.info("u::Lux binding deactivated");
		stopListenerThread();

		this.commandHandler.deactivate();
		this.stateUpdateHandler.deactivate();

		try {
			this.executorService.shutdown();
			this.executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
		LOG.info("u::Lux binding configuration updated");
		this.configuration.updated(config);

		restartListenerThread();
	}

	private void restartListenerThread() {
		if (this.thread != null) {
			stopListenerThread();
		}
		if (this.configuration.isConfigured()) {
			startListenerThread();
		}
	}

	private void startListenerThread() {
		try {
			this.channel = DatagramChannel.open(StandardProtocolFamily.INET);
			this.channel.socket().bind(configuration.getBindSocketAddress());
		} catch (final IOException e) {
			throw new UluxException("Could not open UDP port for listening!", e);
		}

		this.thread = new Thread(this);
		this.thread.start();

		this.audioChannel = new AudioChannel(configuration, providers);
		this.audioChannel.start();

		this.commandHandler.activate(audioChannel.getChannel(), channel, executorService);
		this.stateUpdateHandler.activate(audioChannel.getChannel(), channel, executorService);

	}

	private void stopListenerThread() {
		final Thread thread = this.thread;
		this.thread = null;
		thread.interrupt();

		try {
			this.channel.close();
			this.channel = null;
		} catch (final IOException e) {
			LOG.warn("Error closing channel!", e);
			// swallow exception
		}

		this.audioChannel.stop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(final String itemName, final Command command) {
		for (final UluxBindingProvider provider : this.providers) {
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}

			final UluxBindingConfig binding = provider.getBinding(itemName);
			final Runnable handlerTask = this.commandHandler.createHandlerTask(binding, command);

			this.executorService.execute(handlerTask);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(final String itemName, final State newState) {
		for (final UluxBindingProvider provider : this.providers) {
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}

			final UluxBindingConfig binding = provider.getBinding(itemName);
			final Runnable handlerTask = this.stateUpdateHandler.createHandlerTask(binding, newState);

			this.executorService.execute(handlerTask);
		}
	}

	/**
	 * This method is run by the thread created in {@link #activate()}.
	 */
	@Override
	public void run() {
		final Thread currentThread = Thread.currentThread();

		while (this.thread == currentThread) {
			try {
				doRun();
			} catch (final ClosedByInterruptException e) {
				// normal behavior during shutdown
			} catch (final Exception e) {
				LOG.error("Error while running u::Lux listener thread!", e);
			}
		}
	}

	/**
	 * This method waits for a datagram to arrive, parses it into a message and reacts accordingly.
	 */
	private void doRun() throws Exception {
		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		final InetSocketAddress source = (InetSocketAddress) this.channel.receive(buffer);

		if (source != null) {
			LOG.debug("Received datagram from: {}", source);

			this.executorService.execute(new Runnable() {

				@Override
				public void run() {
					final InetAddress sourceAddress = source.getAddress();
					final short switchId = configuration.getSwitchId(sourceAddress);
					final UluxMessageDatagram response = datagramFactory.createMessageDatagram(switchId, sourceAddress);

					final List<UluxMessage> messages = messageParser.parse(buffer);
					for (UluxMessage message : messages) {
						LOG.debug("Processing incoming message: {}", message);

						messageHandler.handleMessage(message, response);
					}

					response.send(channel);
				}
			});
		}
	}

}
