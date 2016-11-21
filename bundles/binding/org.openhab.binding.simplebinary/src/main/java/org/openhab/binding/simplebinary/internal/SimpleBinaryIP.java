/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;

import org.openhab.binding.simplebinary.internal.SimpleBinaryDeviceState.DeviceStates;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryInfoBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryPortState.PortStates;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP device class
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryIP extends SimpleBinaryGenericDevice {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryIP.class);

    /** port */
    public static final int DEFAULT_PORT = 43243;
    private String bindAddress = "";
    private int port = 43243;

    /** server socket instance */
    private AsynchronousServerSocketChannel listener;
    /** connected clients collection */
    private SimpleBinaryIPChannelInfoCollection channels;

    // ** communication timeout **/
    private static final int DEFAULT_COMMUNICATION_TIMEOUT = 30000;

    /**
     * Constructor
     *
     * @param deviceName
     * @param ip
     * @param port
     * @param simpleBinaryPoolControl
     */
    public SimpleBinaryIP(String deviceName, String ip, int port, SimpleBinaryPoolControl simpleBinaryPoolControl) {
        super(deviceName, "TCPserver", simpleBinaryPoolControl);

        this.bindAddress = ip;
        this.port = port;
    }

    /**
     * Return bind IP address
     *
     * @return
     */
    protected String getIp() {
        return this.bindAddress;
    }

    /**
     * Return port
     *
     * @return
     */
    protected int getPort() {
        return port;
    }

    @Override
    public void setBindingData(EventPublisher eventPublisher, Map<String, SimpleBinaryBindingConfig> itemsConfig,
            Map<String, SimpleBinaryInfoBindingConfig> itemsInfoConfig) {
        super.setBindingData(eventPublisher, itemsConfig, itemsInfoConfig);

        this.channels = new SimpleBinaryIPChannelInfoCollection(devicesStates, deviceName);
    }

    /**
     * Check if port is opened
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Open socket
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#open()
     */
    @Override
    public Boolean open() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Start listening", this.toString());
        }

        portState.setState(PortStates.CLOSED);
        // clear device states
        devicesStates.clear();
        // set initial state for configured devices
        devicesStates.setStateToAllConfiguredDevices(this.deviceName, DeviceStates.UNKNOWN);
        // reset connected state
        connected = false;
        // setWaitingForAnswer(false);

        try {
            // AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withThreadPool(executor);
            listener = AsynchronousServerSocketChannel.open();
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            InetSocketAddress hostAddress;

            if (this.getIp().length() > 0) {
                hostAddress = new InetSocketAddress(getIp(), getPort());
            } else {
                hostAddress = new InetSocketAddress(this.port);
            }

            // boolean running = true;

            listener.bind(hostAddress);

            logger.debug("{} - Listener local address={}", toString(), listener.getLocalAddress().toString());

            listener.accept(channels,
                    new CompletionHandler<AsynchronousSocketChannel, SimpleBinaryIPChannelInfoCollection>() {
                        @Override
                        public void completed(AsynchronousSocketChannel channel,
                                SimpleBinaryIPChannelInfoCollection a) {

                            if (logger.isDebugEnabled()) {
                                logger.debug("New incoming connection");
                            }

                            // get ready for next connection
                            listener.accept(a, this);
                            // allocate receive buffer
                            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
                            // insert client into list
                            SimpleBinaryIPChannelInfo chInfo = a.addChannel(channel, buffer,
                                    new SimpleBinaryIRequestTimeouted() {

                                        @Override
                                        public void timeoutEvent(SimpleBinaryIPChannelInfo chInfo) {

                                            logger.warn("Device{} - Receiving data timeouted. ", chInfo.getDeviceId());

                                            devicesStates.setDeviceState(deviceName, chInfo.getDeviceId(),
                                                    DeviceStates.NOT_RESPONDING);

                                            processCommandQueue(chInfo.getDeviceId());
                                        }
                                    });

                            if (logger.isDebugEnabled()) {
                                logger.debug("New Channel opened:{}", chInfo.getIp());

                            }

                            // callback read
                            channel.read(buffer, chInfo, new CompletionHandler<Integer, SimpleBinaryIPChannelInfo>() {
                                @Override
                                public void completed(Integer result, final SimpleBinaryIPChannelInfo chInfo) {

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Channel {} - read result = {}", chInfo.getIp(), result);
                                    }

                                    if (result < 0) {
                                        if (logger.isDebugEnabled()) {
                                            logger.debug("Disconnected: {}", chInfo.getIp());
                                        }

                                        try {
                                            chInfo.getChannel().close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            chInfo.closed();
                                            logger.warn("Device {}/{} was disconnected", chInfo.getDeviceId(),
                                                    chInfo.getIp());
                                        }

                                        return;
                                    }

                                    if (chInfo.isIpLocked()) {
                                        // TODO: get this to processData
                                        // ipLocked - get device ID from configuration
                                        int forcedId = chInfo.getDeviceIdConfigured();
                                    }

                                    // data processing
                                    SimpleBinaryByteBuffer inBuffer = chInfo.getBuffer();

                                    while (inBuffer.position() > 3) {

                                        // verify device first
                                        if (!chInfo.isDeviceIdAlreadyReceived()) {
                                            int r = verifyDataOnly(inBuffer);

                                            if (logger.isDebugEnabled()) {
                                                logger.debug("Verify incomming data result: {}", r);
                                            }

                                            if (r >= 0) {
                                                // received ID is not equal configured
                                                if (chInfo.hasIdConfigured() && !chInfo.isIpLocked()
                                                        && chInfo.getDeviceIdConfigured() != r) {
                                                    logger.error(
                                                            "Device with IP {} has mismatch between configured({}) and connected({}) device ID's. "
                                                                    + "Remove it from configuration, change device ID or mark it in configuration as locked.",
                                                            chInfo.getIp(), chInfo.getDeviceIdConfigured(), r);

                                                    // TODO: should it send info to device?

                                                    // ready for new data
                                                    chInfo.getChannel().read(buffer, chInfo, this);

                                                    return;
                                                } else if (!chInfo.assignDeviceId(r)) {
                                                    logger.error(
                                                            "DeviceID {} is already used by another connected device. This device will be ignored.",
                                                            r);
                                                    return;
                                                } else if (chInfo.hasIpMismatch()) {
                                                    logger.warn(
                                                            "DeviceID {} has mismatch between configured and connected IP addresses.",
                                                            r);
                                                } else if (chInfo.isIpLocked() && chInfo.hasIdMismatch()) {
                                                    logger.warn(
                                                            "Device with IP {} has mismatch between configured and connected device ID's.",
                                                            chInfo.getIp());
                                                }
                                            }
                                        }

                                        // look what is in income raw data
                                        int r = processData(inBuffer, chInfo.getLastSentData());
                                        // clear last data
                                        chInfo.setLastSentData(null);

                                        if (r >= 0 || r == ProcessDataResult.INVALID_CRC
                                                || r == ProcessDataResult.BAD_CONFIG
                                                || r == ProcessDataResult.NO_VALID_ADDRESS
                                                || r == ProcessDataResult.UNKNOWN_MESSAGE) {
                                            // waiting for answer?
                                            if (chInfo.waitingForAnswer.get()) {
                                                // stop block sent
                                                chInfo.clearWaitingForAnswer();
                                            }
                                        } else if (r == ProcessDataResult.DATA_NOT_COMPLETED
                                                || r == ProcessDataResult.PROCESSING_ERROR) {
                                            break;
                                        }
                                    }

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Channel {}/{} - read finished", chInfo.getDeviceId(),
                                                chInfo.getIp());
                                    }

                                    // ready for new data
                                    chInfo.getChannel().read(buffer, chInfo, this);
                                }

                                @Override
                                public void failed(Throwable t, SimpleBinaryIPChannelInfo chInfo) {
                                    logger.warn(t.getMessage());
                                }
                            });

                            // look for data to send
                            processCommandQueue(chInfo.getDeviceId());
                        }

                        @Override
                        public void failed(Throwable t, SimpleBinaryIPChannelInfoCollection a) {
                            logger.warn(t.getMessage());
                        }
                    });

        } catch (UnknownHostException ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - address error", this.toString());

            return false;
        } catch (IOException ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - socket error: {}", this.toString(), ex.getMessage());

            return false;

        } catch (Exception ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - socket error: {}", this.toString(), ex.getMessage());

            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("{} - channel listen for incomming connections", this.toString());
        }

        portState.setState(PortStates.LISTENING);
        connected = true;

        return true;
    }

    /**
     * Close socket
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#close()
     */
    @Override
    public void close() {
        if (listener != null) {
            // IOUtils.closeQuietly(inputStream);
            // IOUtils.closeQuietly(outputStream);

            try {
                listener.close();
            } catch (IOException e) {
                logger.error("{} close - IO error: {}", this.toString(), e.getMessage());
            }
            listener = null;
        }

        portState.setState(PortStates.CLOSED);
        connected = false;
    }

    /**
     * Write data into device stream
     *
     * @param data
     *            Item data with compiled packet
     */
    @Override
    protected boolean sendDataOut(SimpleBinaryItemData data) {
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Try to send data to device {} - {} bytes", this.toString(), data.getDeviceId(),
                    data.getData().length);
        }

        SimpleBinaryIPChannelInfo chInfo = channels.getById(data.getDeviceId());

        if (chInfo == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} - Device {}: No channel found.", this.toString(), data.getDeviceId());
            }
            return false;
        }

        if (chInfo.getChannel() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} - Device {}: Channel not ready.", this.toString(), data.getDeviceId());
            }
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("{} - data: {}", this.toString(),
                    SimpleBinaryProtocol.arrayToString(data.getData(), data.getData().length));
        }

        if (chInfo.compareAndSetWaitingForAnswer()) {
            // write string to tcp channel

            ByteBuffer buffer = ByteBuffer.allocate(data.getData().length);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.put(data.getData());
            buffer.flip();

            chInfo.setWriteBuffer(buffer);

            // if (logger.isDebugEnabled()) {
            // logger.debug("ID={},channel={}", data.getDeviceId(), chInfo.getChannel().toString());
            // }

            // write into device
            chInfo.getChannel().write(buffer, chInfo, new CompletionHandler<Integer, SimpleBinaryIPChannelInfo>() {
                @Override
                public void completed(Integer result, final SimpleBinaryIPChannelInfo chInfo) {

                    if (result < 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Disconnected:{}", chInfo.getIp());
                        }

                        try {
                            chInfo.getChannel().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            chInfo.closed();
                            logger.warn("Device {}/{} was disconnected", chInfo.getDeviceId(), chInfo.getIp());
                        }

                        return;
                    }

                    if (chInfo.getBuffer().remaining() > 0) {
                        chInfo.getChannel().write(chInfo.getWriteBuffer(), chInfo, this);
                    } else {
                        chInfo.clearWriteBuffer();
                    }
                }

                @Override
                public void failed(Throwable t, SimpleBinaryIPChannelInfo chInfo) {
                    logger.warn(t.getMessage());

                    try {
                        chInfo.getChannel().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        chInfo.closed();
                        logger.warn("Device {}/{} was disconnected", chInfo.getDeviceId(), chInfo.getIp());
                    }
                }
            });

            chInfo.setLastSentData(data);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("{} - Sending data to device {} discarted. Another send/wait is processed.",
                        this.toString(), data.getDeviceId());
            }

            return false;
        }

        return true;
    }

    /**
     * Check new data for all connected devices
     *
     */
    @Override
    public void checkNewData() {
        super.checkNewData();
    }

    @Override
    public String toString() {
        return deviceID + ":" + getPort();
    }

    public void addDevice(String deviceID, String ipAddress, boolean isIpLocked) {
        if (logger.isDebugEnabled()) {
            logger.debug("{}: Adding TCP client configuration: {}-{}", toString(), Integer.parseInt(deviceID),
                    ipAddress);
        }
        if (channels != null) {
            channels.addConfiguredChannel(Integer.parseInt(deviceID), ipAddress, isIpLocked);
        }
    }

    // public void processChannel(final AsynchronousSocketChannel channel) throws Exception {
    //
    // final String address = channel.getRemoteAddress().toString();
    //
    // logger.debug("New Channel:{}", address);
    //
    // final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
    // Object params[] = null;
    //
    // channel.read(buffer, params, new CompletionHandler<Integer, Object[]>() {
    //
    // @Override
    // public void completed(Integer nBytes, Object[] attachment) {
    //
    // channel.read(buffer, attachment, this);
    // }
    //
    // @Override
    // public void failed(Throwable exc, Object[] attachment) {
    // // exc.printStackTrace();
    // // try {
    // // ((AsynchronousSocketChannel) attachment[Constants.CHANNEL_POS]).close();
    // // } catch (IOException e) {
    // // // NOPE
    // // }
    // }
    // });

    // final String sessionId = generateSessionId();
    // final ByteBuffer buffer = ByteBuffer.allocateDirect(512);
    // final ByteBuffer writeBuffers[] = FileLoader.cloneData();
    // final long fileLength = FileLoader.getFileLength();
    // final CompletionHandler<Integer, Object[]> readHandler = new ReadCompletionHandler();
    // final CompletionHandler<Long, Object[]> writeHandler = new WriteCompletionHandler();
    //
    // // Put params in the Map
    //
    // final Object array[] = new Object[10];
    // array[Constants.CHANNEL_POS] = channel;
    // array[Constants.READ_BUFFER_POS] = buffer;
    // array[Constants.WRITE_BUFFERS_POS] = writeBuffers;
    // array[Constants.FILE_LENGTH_POS] = fileLength;
    // array[Constants.SESSION_ID_POS] = sessionId;
    // array[Constants.READ_HANDLER_POS] = readHandler;
    // array[Constants.WRITE_HANDLER_POS] = writeHandler;
    //
    // // Perform an asynchronous read operation
    // channel.read(buffer, array, new CompletionHandler<Integer, Object[]>() {
    //
    // @Override
    // public void completed(Integer nBytes, Object[] attachment) {
    // if (nBytes < 0) {
    // failed(new ClosedChannelException(), attachment);
    // return;
    // }
    // if (nBytes > 0) {
    // ByteBuffer buff = (ByteBuffer) array[Constants.READ_BUFFER_POS];
    // buff.flip();
    // byte bytes[] = new byte[nBytes];
    // buff.get(bytes).clear();
    // String response = "jSessionId: " + attachment[Constants.SESSION_ID_POS] + Constants.CRLF;
    // // write initialization response to client
    // buff.put(response.getBytes()).flip();
    // AsynchronousSocketChannel ch = (AsynchronousSocketChannel) attachment[Constants.CHANNEL_POS];
    // ch.write(buff, attachment, new CompletionHandler<Integer, Object[]>() {
    //
    // @Override
    // public void completed(Integer nBytes, Object[] attachment) {
    // // System.out.println("Number of bytes written to client -> " + nBytes);
    // if (nBytes < 0) {
    // failed(new ClosedChannelException(), attachment);
    // } else if (nBytes > 0) {
    // AsynchronousSocketChannel channel = (AsynchronousSocketChannel) attachment[Constants.CHANNEL_POS];
    // ByteBuffer bb = (ByteBuffer) attachment[Constants.READ_BUFFER_POS];
    // if (bb.hasRemaining()) {
    // channel.write(bb, attachment, this);
    // } else {
    // @SuppressWarnings("unchecked")
    // CompletionHandler<Integer, Object[]> readHandler = (CompletionHandler<Integer, Object[]>)
    // attachment[Constants.READ_HANDLER_POS];
    // // System.out.println("End of Session Initialization, Waiting for client requests");
    // channel.read(bb, attachment, readHandler);
    // }
    // }
    // }
    //
    // @Override
    // public void failed(Throwable exc, Object[] attachment) {
    // exc.printStackTrace();
    // try {
    // ((AsynchronousSocketChannel) attachment[Constants.CHANNEL_POS]).close();
    // } catch (IOException e) {
    // // NOPE
    // }
    // }
    // });
    // }
    // }
    //
    // @Override
    // public void failed(Throwable exc, Object[] attachment) {
    // // exc.printStackTrace();
    // try {
    // ((AsynchronousSocketChannel) attachment[Constants.CHANNEL_POS]).close();
    // } catch (IOException e) {
    // // NOPE
    // }
    // }
    // });

    // }
}
