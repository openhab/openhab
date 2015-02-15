/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.UpdateState;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a Node Information Frame (NIF) message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ApplicationUpdateMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationUpdateMessageClass.class);

	@Override
	public  boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId;
		boolean result = true;
		UpdateState updateState = UpdateState.getUpdateState(incomingMessage.getMessagePayloadByte(0));

		switch (updateState) {
		case NODE_INFO_RECEIVED:
			// We've received a NIF, and this contains the node ID.
			nodeId = incomingMessage.getMessagePayloadByte(1);
			logger.debug("NODE {}: Application update request. Node information received.", nodeId);
			
			int length = incomingMessage.getMessagePayloadByte(2);
			ZWaveNode node = zController.getNode(nodeId);
			if(node == null) {
				logger.debug("NODE {}: Application update request. Node not known!", nodeId);
				
				// We've received a NIF from a node we don't know.
				// This could happen if we add a new node using a different controller than OH.
				// We handle this the same way as if included through an AddNode packet.
				// This allows everyone to be notified.
				if(nodeId > 0 && nodeId <= 232) {
					zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.IncludeDone, incomingMessage.getMessagePayloadByte(2)));
				}
				break;
			}

			node.resetResendCount();

			// Remember that we've received this so we can continue initialisation
			node.setApplicationUpdateReceived(true);

			if(node.getNodeStage() == NodeStage.DONE) {
				// If this node supports associations, then assume this should be handled through that mechanism
				if(node.getCommandClass(CommandClass.ASSOCIATION) == null) {
					// If we receive an Application Update Request and the node is already
					// fully initialised we assume this is a request to the controller to 
					// re-get the current node values
					logger.debug("NODE {}: Application update request. Requesting node state.", nodeId);

					zController.pollNode(node);
				}
			}
			else {
				for (int i = 6; i < length + 3; i++) {
					int data = incomingMessage.getMessagePayloadByte(i);
					if(data == 0xef) {
						// TODO: Implement control command classes
						break;
					}
					logger.trace(String.format("NODE %d: Command class 0x%02X is supported.", nodeId, data));
					ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(data, node, zController);
					if (commandClass != null) {
						node.addCommandClass(commandClass);
					}
				}
			}

			// Treat the node information frame as a wakeup
			ZWaveWakeUpCommandClass wakeUp = (ZWaveWakeUpCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP);
			if(wakeUp != null) {
				wakeUp.setAwake(true);
			}
			break;
		case NODE_INFO_REQ_FAILED:
			// Make sure we can correlate the request before we use the nodeId
			if (lastSentMessage.getMessageClass() != SerialMessageClass.RequestNodeInfo) {
				logger.warn("Got ApplicationUpdateMessage without request, ignoring. Last message was {}.", lastSentMessage.getMessageClass());
				return false;
			}

			// The failed message doesn't contain the node number, so use the info from the request.
			nodeId = lastSentMessage.getMessageNode();
			logger.debug("NODE {}: Application update request. Node Info Request Failed.", nodeId);

			// Handle retries
			if (--lastSentMessage.attempts >= 0) {
				logger.error("NODE {}: Got Node Info Request Failed. Requeueing", nodeId);
				zController.enqueue(lastSentMessage);
			}
			else {
				logger.warn("NODE {}: Node Info Request Failed 3x. Discarding message: {}", nodeId, lastSentMessage.toString());
			}

			// Transaction is not successful
			incomingMessage.setTransactionCanceled();
			result = false;
			break;
		default:
			logger.warn("TODO: Implement Application Update Request Handling of {} ({}).", updateState.getLabel(), updateState.getKey());
		}
		
		// Check if this completes the transaction
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return result;
	}
}
