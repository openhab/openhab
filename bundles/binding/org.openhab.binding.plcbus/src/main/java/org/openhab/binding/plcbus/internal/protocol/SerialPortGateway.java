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
package org.openhab.binding.plcbus.internal.protocol;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;

/**
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortGateway implements ISerialPortGateway {

    private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
    private NRSerialPort serialPort;

    private SerialPortGateway(String serialPortName) {
        this.serialPort = new NRSerialPort(serialPortName, 9600);
        this.serialPort.connect();
    }

    public static ISerialPortGateway create(String serialPortName) {
        return new SerialPortGateway(serialPortName);
    }

    @Override
    public synchronized void send(TransmitFrame frame, IReceiveFrameContainer receivedFrameContainer) {
        try {
            logger.debug("thread {} entering send", Thread.currentThread());
            byte[] paket = Convert.toByteArray(frame.getBytes());

            OutputStream out = serialPort.getOutputStream();

            out.write(paket);

            try {
                receivedFrameContainer.process(SerialPortByteProvider.create(serialPort));
            } catch (Exception e) {
                logger.error("Error while processing: " + e.getMessage());
            }

        } catch (Exception e) {
            logger.info("Error in write method: " + e.getMessage());
        } finally {
            logger.debug("thread {} leaving send", Thread.currentThread());
        }
    }

    @Override
    public void close() {
        serialPort.disconnect();
    }

}