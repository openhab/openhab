package org.openhab.binding.lightwaverf.internal;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRFReceiver implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
    
    private final int port;
    private final CopyOnWriteArrayList<LightwaveRFMessageListener> listerns = new CopyOnWriteArrayList<LightwaveRFMessageListener>();
    private final LightwaverfConvertor messageConvertor;
    
    private CountDownLatch latch = new CountDownLatch(0);
    private boolean running = true;
    private DatagramSocket receiveSocket;
    
    
    public LightwaveRFReceiver(LightwaverfConvertor messageConvertor, int port) {
    	this.port = port;
    	this.messageConvertor = messageConvertor;
    }
    	
    /**
     * Start the LightwaveRFReceiver
     * Will set running true, initialise the socket and start the thread.
     */
    public synchronized void start() {
        logger.info("Starting LightwaveRFReceiver");
        running = true;
        latch = new CountDownLatch(1);
        initialiseSockets();
        new Thread(this).start();
    }
    /**
    * Stop the LightwaveRFSender
    * Will close the socket wait for the thread to exit and null the socket
    */
    public synchronized void stop() {
        logger.info("Stopping LightwaveRFReceiver");
        running = false;
        receiveSocket.close();
        try {
            latch.await();
        } 		catch(InterruptedException e) {
            logger.error("Error waiting for shutdown to complete", e);
        }
        receiveSocket = null;
        logger.info("LightwaveRFReceiver Stopped");
    }
    /**
    * Initialise receive sockets for UDP
    */
    private void initialiseSockets() {
        try {
            receiveSocket = new DatagramSocket(port);
        } catch (IOException e) {
            logger.error("Error initalising socket", e);
        }
    }
    /**
    * Run method, this will listen to the socket and receive messages.
    * The blocking is stopped when the socket is closed.
    */
    public void run() {
        logger.info("LightwaveRFReceiver Started");
        while(running) {
        	String message = null;
            try {
            	message = receiveUDP();
                logger.info("Message received: " + message);
                LightwaveRFCommand command = messageConvertor.convertFromLightwaveRfMessage(message);
                switch (command.getMessageType()) {
				case OK:
					notifyOkListners((LightwaveRfCommandOk) command);
					break;
				case ROOM_DEVICE:
					notifyRoomDeviceListners((LightwaveRfRoomDeviceMessage) command);
					break;
				case ROOM:
					notifyRoomListners((LightwaveRfRoomMessage) command);
					break;
				case SERIAL:
					notifySerialListners((LightwaveRfSerialMessage) command);
					break;
				case VERSION:
					notifyVersionListners((LightwaveRfVersionMessage) command);
					break;
				default:
					break;
				}
            } 			
            catch (IOException e) {
                if(!(running == false && receiveSocket.isClosed())) {
                    // If running isn't false and the socket isn't closed log the error
                    logger.error("Error receiving message", e);
                }
            }
            catch (LightwaveRfMessageException e){
            	logger.error("Error converting message: " + message);
            }
        }
        latch.countDown();
    }
    /**
     	 * Receive the next UDP packet on the socket
     	 * @return
    	 * @throws IOException
     */
    public String receiveUDP() throws IOException {
        String receivedMessage = "";
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        receiveSocket.receive(receivePacket);
        receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        return receivedMessage;
    }
    
    /**
    * Add listener to be notified of messages received on the socket
    * @param listener
    */
    public void addListener(LightwaveRFMessageListener listener) {
        listerns.add(listener);
    }
    /**
     * Remove listener to stop being notified of messages being received on the socket.
    * @param listener
     */
    public void removeListener(LightwaveRFMessageListener listener) {
        listerns.remove(listener);
    }
    /**
    * Notify all listeners of a message
    * @param message
    */
    
    private void notifyRoomDeviceListners(LightwaveRfRoomDeviceMessage message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.roomDeviceMessageReceived(message);
        }
    }

    private void notifyRoomListners(LightwaveRfRoomMessage message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.roomMessageReceived(message);
        }
    }

    private void notifySerialListners(LightwaveRfSerialMessage message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.serialMessageReceived(message);
        }
    }
    
    private void notifyOkListners(LightwaveRfCommandOk message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.okMessageReceived(message);
        }
    }
    
    private void notifyVersionListners(LightwaveRfVersionMessage message) {
        for(LightwaveRFMessageListener listener : listerns) {
            listener.versionMessageReceived(message);
        }
    }



}
