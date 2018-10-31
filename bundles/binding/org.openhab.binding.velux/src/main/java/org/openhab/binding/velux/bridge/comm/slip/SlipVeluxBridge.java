/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm.slip;

import java.io.IOException;
import org.openhab.binding.velux.things.VeluxKLFAPI.Command;

import java.net.ConnectException;
import java.text.ParseException;
import java.util.Set;
import java.util.TreeSet;

import org.openhab.binding.velux.bridge.VeluxBridge;
import org.openhab.binding.velux.bridge.VeluxBridgeInstance;
import org.openhab.binding.velux.bridge.comm.BridgeAPI;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxKLFAPI.CommandNumber;
import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLIP-based 2nd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides methods for pre- and postcommunication
 * as well as a common method for the real communication.
 * <P>
 * In addition to the generic {@link VeluxBridge} methods, i.e.
 * <UL>
 * <LI>{@link VeluxBridge#bridgeLogin} for pre-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeLogout} for post-communication,</LI>
 * <LI>{@link VeluxBridge#bridgeCommunicate} as method for the common communication,</LI>
 * <LI>{@link VeluxBridge#bridgeOverwriteConfig} for modifying the communication parameters,</LI>
 * <LI> {@link VeluxBridge#bridgeAPI} as interfacing method to all interaction prototypes,</LI>
 * </UL>
 * the following class access methods provides the protocol-specific implementation:
 * <UL>
 * <LI>{@link #bridgeDirectCommunicate} for SLIP-based communication.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public class SlipVeluxBridge extends VeluxBridge {
	private final Logger logger = LoggerFactory.getLogger(SlipVeluxBridge.class);

	/**
	 * Support protocols for the concrete implementation.
	 * {@link VeluxBridgeConfiguration}.
	 */
	public  Set<String> supportedProtocols = new TreeSet<String>();

	private SSLconnection connectivity = null;
	private final byte[] emptyPacket = new byte[0];

	/**
	 * Handler passing the interface methods to other classes.
	 * Can be accessed via  method {@link org.openhab.binding.velux.bridge.comm.slip.bridgeAPI bridgeAPI}.
	 *
	 */
	protected BridgeAPI	bridgeAPI = null;

	/**
	 * Constructor.<P>
	 * Inherits the initialization of the binding-wide instance for dealing for common informations and
	 * initializes the Velux bridge connectivity settings.
	 * 
	 * @param bridgeInstance   refers to the binding-wide instance for dealing for common informations.
	 */
	public SlipVeluxBridge(VeluxBridgeInstance bridgeInstance) {
		super(bridgeInstance);
		logger.trace("SlipVeluxBridge(constructor) called.");
		supportedProtocols.add("slip");
	}

	/**
	 * Provides information about the base-level communication method and
	 * any kind of available gateway interactions.
	 * <P>
	 * <B>Note:</B> the implementation within this class {@link SlipVeluxBridge} as inherited from {@link VeluxBridge}
	 *  will return the protocol-specific class implementations.
	 * <P>
	 * The information will be initialized by the corresponding API class {@link SlipBridgeAPI}.
	 *
	 * @return <b>bridgeAPI</b> of type {@link BridgeAPI} contains all possible methods.
	 */
	public BridgeAPI bridgeAPI() {
		logger.trace("bridgeAPI() called.");
		return bridgeAPI;
	}


	/**
	 * Base level communication with the {@link SlipVeluxBridge <b>Velux</b> bridge}.
	 *
	 * @param host 	as String describing the Service Access Point location i.e. hostname.
	 * @param port  as String describing the Service Access Point location i.e. TCP port.
	 * @param request as Array of bytes representing the structure of the message to be converted into SLIP.
	 * @return <b>response</b> of type Array of byte containing all resulting informations, i.e. device status, errors a.s.o. Will
	 *         return
	 *         <B>null</B> in case of communication or decoding error.
	 * @throws java.net.ConnectException	 in case of unrecoverable communication failures.
	 * @throws java.io.IOException in case of continuous communication I/O failures.
	 */
	private byte[] io(String host, int port, byte[] request)
			throws ConnectException, IOException {
		logger.trace("io({},{},{} bytes) called.",host,port,request.length);

		/** Local handles */
		int retryCount = 0;
		IOException lastIOE = null;

		do {
			try {
				if (connectivity == null) {
					try {
						logger.trace("io(): connecting to {}:{}.", host, port);
						connectivity = new SSLconnection(host, port);
					} catch (ConnectException ce) {
						logger.trace("io(): raised a non-recoverable error during connection setup: {}.", ce.getMessage());
						throw ce;
					}
					catch (Exception e) {
						logger.trace("io(): raised an error during connection setup: {}.", e.getMessage());
						continue;
					}
				}
				if (request.length > 0) {
					try {
						logger.debug("io(): sending packet of size {}.", request.length);
						connectivity.send(request);
					} catch (Exception e) {
						logger.trace("io(): raised an error during sending: {}.", e.getMessage());
					}

					// Give the bridge some time to breathe
//					if (configuration.timeoutMsecs > 0) {
					if (this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs > 0) {
						logger.trace("io(): wait time {} msecs.", this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
						try {
							Thread.sleep(this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
						} catch (InterruptedException ie) {
							logger.trace("io() wait interrupted.");
						}
					}
				}
				byte[] packet;
				logger.trace("io(): receiving bytes.");
				packet = connectivity.receive();
				if (logger.isTraceEnabled())	{
					logger.trace("io(): received packet with {} bytes: ", packet.length, new Packet(packet));
				} else {
					logger.debug("io(): received packet with {} bytes.", packet.length);
				}
				logger.trace("io() finished.");
				return packet;
			} catch (IOException ioe) {
				logger.trace("io(): Exception occurred during I/O: {}.", ioe.getMessage());
				lastIOE = ioe;
				// Error Retries with Exponential Backoff
				long waitTime = ((long) Math.pow(2, retryCount) * this.bridgeInstance.veluxBridgeConfiguration().timeoutMsecs);
				logger.trace("io(): wait time {} msecs.", waitTime);
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException ie) {
					logger.trace("io(): wait interrupted.");
				}
			}
		} while (retryCount++ < this.bridgeInstance.veluxBridgeConfiguration().retries);
		logger.info("io(): socket I/O failed continuously ({} times).", this.bridgeInstance.veluxBridgeConfiguration().retries);
		throw lastIOE;
	}


	/**
	 * Initializes a client/server communication towards <b>Velux</b> veluxBridge
	 * based on the Basic I/O interface {@link #io} and parameters
	 * passed as arguments (see below) and provided by {@link VeluxBridgeConfiguration}.
	 *
	 * @param communication     Structure of interface type {@link SlipBridgeCommunicationProtocol} describing the
	 *                              intended
	 *                              communication,
	 *                              that is request and response interactions as well as appropriate URL definition.
	 * @param useAuthentication boolean flag to decide whether to use authenticated communication.
	 * @return <b>success</b> of type boolean which signals the success of the communication.
	 */
	public synchronized boolean bridgeDirectCommunicate(SlipBridgeCommunicationProtocol communication, boolean useAuthentication) {
		logger.trace("bridgeDirectCommunicate({},{}authenticated) called.",
				communication.name(),
				useAuthentication ? "" : "un");

		// From configuration
		String host = this.bridgeInstance.veluxBridgeConfiguration().bridgeIPAddress;
		int port = this.bridgeInstance.veluxBridgeConfiguration().bridgeTCPPort;
		assert this.bridgeInstance.veluxBridgeConfiguration().bridgeProtocol.contentEquals("slip");

		// From parameters
		short command = communication.getRequestCommand().toShort();
		byte[] data = communication.getRequestDataAsArrayOfBytes();
		logger.debug("bridgeDirectCommunicate({},{}authenticated) called.",
				Command.get(command).toString(),
				useAuthentication ? "" : "un");
		boolean success = false;

		communication:
			do {
				// Special handling
				if (Command.get(command) == Command.GW_OPENHAB_CLOSE) {
					if (connectivity != null) {
						logger.trace("bridgeDirectCommunicate(): shutting down connection.");
						try { 
							connectivity.close();
						} catch (IOException e) {
							logger.trace("bridgeDirectCommunicate(): connection shutdown failed due to {}.", e.getMessage());
						}
						connectivity = null;
					}
					success = true;
					continue;
				}

				// Normal processing
				byte[] sendBytes = null;
				logger.debug("bridgeDirectCommunicate(): working on request {} with {} bytes of data.",
						Command.get(command).toString(), data.length);
				SlipEncoding t = new SlipEncoding(command, data);
				if (!t.isValid()) {
					logger.error("bridgeDirectCommunicate(): SlipEncoding() failed, aborting.");
					break;
				}
				logger.trace("bridgeDirectCommunicate(): transportEncoding={}.", t.toString());
				sendBytes = new SlipRFC1055().encode(t.toMessage());

				do {
					byte[] receivedPacket;
					try {
						if (sendBytes.length > 0) {
							logger.trace("bridgeDirectCommunicate(): sending {} bytes to {}:{}.", sendBytes.length, host, port);
						} else {
							logger.trace("bridgeDirectCommunicate(): receiving from {}:{}.", host, port);
						}
						receivedPacket = io(host, port, sendBytes);
						// Once being sent, it should never be sent again
						sendBytes = emptyPacket;
					} catch (Exception e) {
						logger.error("bridgeDirectCommunicate(): method io() raised an error: {}.", e.getMessage());
						break communication;
					}
					logger.trace("bridgeDirectCommunicate(): received packet {}.", new Packet(receivedPacket).toString());
					byte[] response;
					try { 
						response = new SlipRFC1055().decode(receivedPacket);
					} catch (ParseException e) {
						logger.error("bridgeDirectCommunicate(): method SlipRFC1055() raised a decoding error: {}.", e.getMessage());
						break communication;
					}
					SlipEncoding tr = new SlipEncoding(response);
					if (!tr.isValid()) {
						logger.error("bridgeDirectCommunicate(): method SlipEncoding() raised a decoding error.");
						break communication;
					}
					short responseCommand = tr.getCommand();
					byte[] responseData = tr.getData();
					logger.debug("bridgeDirectCommunicate(): working on response {} with {} bytes of data.",
							Command.get(responseCommand).toString(), responseData.length);
					// Handle some common (unexpected) answers
					switch (Command.get(responseCommand)) {
					case GW_NODE_STATE_POSITION_CHANGED_NTF:	
						logger.trace("bridgeDirectCommunicate(): received GW_NODE_STATE_POSITION_CHANGED_NTF, special processing of this packet.");
						SCreceiveStatus receiver = new SCreceiveStatus();
						receiver.setResponse(responseCommand, responseData);
						if (receiver.isCommunicationSuccessful()) {
							logger.warn("bridgeDirectCommunicate(): existingProducts().update() called.");
							this.bridgeInstance.existingProducts().update(new ProductBridgeIndex(receiver.getNtfNodeID()),
									receiver.getNtfState(), receiver.getNtfCurrentPosition(), receiver.getNtfTarget());
						}
						sendBytes = emptyPacket;
						logger.trace("bridgeDirectCommunicate(): continue with receiving.");
						continue;	
					case GW_ERROR_NTF:	
						switch (responseData[0]) {
						case 0:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF (Not further defined error), aborting.");
							break communication;
						case 1:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF (Unknown Command or command is not accepted at this state), aborting.");
							break communication;
						case 2:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF (ERROR on Frame Structure), aborting.");
							break communication;
						case 7:
							logger.trace("bridgeDirectCommunicate(): received GW_ERROR_NTF (Busy. Try again later), retrying.");
							sendBytes = emptyPacket;
							continue;	
						case 8:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF (Bad system table index), aborting.");
							break communication;
						case 12:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF (Not authenticated), aborting.");
							break communication;
						default:
							logger.error("bridgeDirectCommunicate(): received GW_ERROR_NTF ({}), aborting.",responseData[0]);
							break communication;
						}
					default:
					}
					logger.trace("bridgeDirectCommunicate(): passes back command {} and data {}.",
							new CommandNumber(responseCommand).toString(),
							new Packet(responseData).toString());
					communication.setResponse(responseCommand, responseData);
				} while (!communication.isCommunicationFinished());
				success = communication.isCommunicationSuccessful();
			} while (false); // communication
		logger.debug("bridgeDirectCommunicate({}) returns success={} to caller.", Command.get(command).toString(), success);
		//		new LogExtension().log("hello");
		return success;
	}
}

/**
 * end-of-bridge/SlipVeluxBridge.java
 */
