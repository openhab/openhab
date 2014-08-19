package org.openhab.binding.lightwaverf.internal;

import java.io.IOException;
import java.lang.InterruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LightwaveRFSender implements Runnable {
	private static final int LightwavePortIn = 9760; // Port into Lightwave Wifi hub.
	private static final String BroadcastAddress = "255.255.255.255";  // Broadcast UDP address.
	private final ScheduledExecutorService scheduler;
	private static final long POLL_TIME = 1;
	private static final long INITIAL_POLL_DELAY = 0;

	private int MessageCount = 0;
	private DatagramSocket transmitSocket; // Socket for UDP transmission to LWRF port 9760
	private BlockingQueue<String> queue; // Simple queue to queue up UDP transmission that could be from a polling thread, or direct commands through API  	

	/*
	 * Constructor defaults to logging every 30 seconds
	 */
	public LightwaveRFSender() {
		queue = new LinkedBlockingQueue<String>();
		initialiseSockets();
		scheduler = Executors.newScheduledThreadPool(1);
	}

	/*
	 * Initialise transmit sockets for UDP
	 */
	public void initialiseSockets(){
		try{
			transmitSocket = new DatagramSocket();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

 	public void start(){
		scheduler.scheduleWithFixedDelay(this, INITIAL_POLL_DELAY, POLL_TIME, TimeUnit.SECONDS);
 	}

	/*
	 * Run thread, pulling off any items from the UDP commands buffer, then send across network
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try{
			netsendUDP(queue.take());
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

     	/*
 	 * Add UDP commands to a buffer.
  	 */
	public void sendUDP(String Command){
		try{
			queue.put(Command);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	/*
	 * Send the UDP commands from the buffer, waiting a period of time before sending next, so as not to flood UDP socket on LWRF 9760 port
	 */
	public void netsendUDP(String Command){
		Command = MessageCount + Command;
		incrementMessageCount();
		try {
			byte[] sendData = new byte[1024];
			sendData = Command.getBytes();
			InetAddress IPAddress =  InetAddress.getByName(BroadcastAddress);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, LightwavePortIn); //Send broadcast UDP to 9760 port
			transmitSocket.send(sendPacket);
		}
		catch (IOException e) {
			e.printStackTrace(); // Display if something went wrong
		}
	}

 	/*
	 * Increment message counter, so different messages have different IDs
	 * Important for getting corresponding OK acknowledgements from port 9761 tagged with the same counter value
	 */
	private void incrementMessageCount(){
		if (MessageCount <=998) MessageCount++;
		else MessageCount = 000;
	}
}
