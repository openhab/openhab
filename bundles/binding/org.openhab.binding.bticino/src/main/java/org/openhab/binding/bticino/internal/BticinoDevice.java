/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bticino.internal;

import java.util.List;

import org.openhab.binding.bticino.internal.BticinoGenericBindingProvider.BticinoBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.devlaminck.openwebnet.IBticinoEventListener;
import be.devlaminck.openwebnet.OpenWebNet;
import be.devlaminck.openwebnet.ProtocolRead;

/**
 * This class connects to the openweb gateway of bticino (MH200(N)). It opens a
 * monitor session to retrieve the events and creates, for every command
 * received, a command on the bus.
 *
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.7.0
 */
public class BticinoDevice implements IBticinoEventListener {

    // The ID of this gateway (corresponds with the .cfg)
    private String m_gateway_id;
    // The Bticino binding object (needed to send the events back + retrieve
    // information about the binding config of the items)
    private BticinoBinding m_bticino_binding;
    // Hostname or Host IP (of the MH200)
    private String m_host = "";
    // Port to connect to
    private int m_port = 0;
    // Openweb password
    private String m_passwd = "";
    // Rescan interval in seconds
    private int m_rescan_interval_secs = 0;
    // Heating zones
    private int m_heating_zones = 0;
    // Indicator if this device is started
    private boolean m_device_is_started = false;
    // A lock object
    private Object m_lock = new Object();
    // The openweb object that handles connections and events
    private OpenWebNet m_open_web_net = null;

    private static final Logger logger = LoggerFactory.getLogger(BticinoDevice.class);

    private EventPublisher eventPublisher;

    public BticinoDevice(String p_gateway_id, BticinoBinding p_bticino_binding) {
        m_gateway_id = p_gateway_id;
        m_bticino_binding = p_bticino_binding;
    }

    public void setHost(String p_host) {
        m_host = p_host;
    }

    public void setPort(int p_port) {
        m_port = p_port;
    }

    public void setPasswd(String p_passwd) {
        m_passwd = p_passwd;
    }

    public void setRescanInterval(int p_rescan_interval_secs) {
        m_rescan_interval_secs = p_rescan_interval_secs;
    }

    public void setHeatingZones(int p_heating_zones) {
        m_heating_zones = p_heating_zones;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;
    }

    /**
     * Initialize this device
     *
     * @throws InitializationException
     */
    public void initialize() throws InitializationException {
        // Add other initialization stuff here
        logger.debug("Gateway [{}], initialize OK", m_gateway_id);
    }

    /**
     * Start this device
     *
     */
    public void startDevice() {
        if (m_open_web_net == null) {
            m_open_web_net = new OpenWebNet(m_host, m_port, m_passwd, m_rescan_interval_secs, m_heating_zones);
            m_open_web_net.addEventListener(this);
            m_open_web_net.onStart();
        }
        m_device_is_started = true;
        logger.debug("Gateway [{}], started OK", m_gateway_id);
    }

    public void stopDevice() {
        if (m_open_web_net != null) {
            m_open_web_net.interrupt();
            m_open_web_net = null;
        }
        m_device_is_started = false;
    }

    public boolean isDeviceStarted() {
        return m_device_is_started;
    }

    public void receiveCommand(String itemName, Command command, BticinoBindingConfig itemBindingConfig) {
        try {
            synchronized (m_lock) {
                // A command is received from the openHab system;
                // analyse it and execute it
                logger.debug("Gateway [" + m_gateway_id + "], Command '{}' received for item {}",
                        (Object[]) new String[] { command.toString(), itemName });

                ProtocolRead l_pr = new ProtocolRead(itemBindingConfig.who + "*" + itemBindingConfig.where);
                l_pr.addProperty("who", itemBindingConfig.who);
                l_pr.addProperty("address", itemBindingConfig.where);

                int l_who = Integer.parseInt(itemBindingConfig.who);
                switch (l_who) {
                    // Lights
                    case 1: {
                        if (command instanceof IncreaseDecreaseType) {
                            if (IncreaseDecreaseType.INCREASE.equals(command)) {
                                logger.debug("Light received INCREASE command.");
                                l_pr.addProperty("what", "30");
                            } else {
                                logger.debug("Light received DECREASE command.");
                                l_pr.addProperty("what", "31");
                            }
                        } else if (command instanceof PercentType) {
                            PercentType pType = (PercentType) command;
                            // take percentage and divide by 10, round 1 (ie 0 to 10 is the result, nothing else)
                            int percentValue = (int) (Math.floor(pType.intValue() / 10F));
                            logger.debug("Set light value to {}", percentValue);
                            l_pr.addProperty("what", String.valueOf(percentValue));
                        } else if (command instanceof OnOffType) {
                            if (OnOffType.ON.equals(command)) {
                                l_pr.addProperty("what", "1");
                            } else {
                                l_pr.addProperty("what", "0");
                            }
                        } else {
                            logger.warn("Received unknown command type for lighting: '{}'",
                                    command.getClass().getName());
                        }
                        break;
                    }
                    // Shutter
                    case 2: {
                        if (command instanceof PercentType) {
                            // Workaround for Homekit as it sends PecentType Commands
                            if (Integer.valueOf(command.toString()) >= 55) {
                                l_pr.addProperty("what", "2");
                            } else if (Integer.valueOf(command.toString()) <= 45) {
                                l_pr.addProperty("what", "1");
                            } else if (Integer.valueOf(command.toString()) > 45
                                    && Integer.valueOf(command.toString()) < 55) {
                                l_pr.addProperty("what", "0");
                            }
                        } else {
                            if (UpDownType.UP.equals(command)) {
                                l_pr.addProperty("what", "1");
                            } else if (UpDownType.DOWN.equals(command)) {
                                l_pr.addProperty("what", "2");
                            } else if (StopMoveType.STOP.equals(command)) {
                                l_pr.addProperty("what", "0");
                            }
                        }
                        break;
                    }
                    // Temperature Control
                    case 4: {
                        // Set Temperature Heating Zone
                        // *#4*#WHERE*#14*T*M##
                        // WHERE = #1-99
                        // T = Temperature 0400 (40 degrees C)
                        // M = Mode (1=Heating, 2=Cooling 3=General)
                        // #0 is used in the WHERE for ALL ZONES
                        if (itemBindingConfig.what.equalsIgnoreCase("14")) {
                            l_pr.addProperty("what", "14*" + convertTemperatureBticino(String.valueOf(command)) + "*1");
                        }
                        // Set Control Unit Heating
                        // WHAT = 100
                        else if (itemBindingConfig.what.equalsIgnoreCase("100")) {
                            l_pr.addProperty("what", String.valueOf(command));
                        }
                        break;
                    }
                    // Door Lock
                    case 6: {
                        // Only for the ON type (open)
                        if (OnOffType.ON.equals(command)) {
                            l_pr.addProperty("what", itemBindingConfig.what);
                        }
                        break;
                    }
                    // CEN Basic & Evolved
                    case 15: {
                        // Only for the on type, send a CEN event (aka a pushbutton
                        // device)
                        // the CEN can start a scenario on eg. a MH200N gateway
                        // device
                        if (OnOffType.ON.equals(command)) {
                            l_pr.addProperty("what", itemBindingConfig.what);
                        }
                        break;
                    }
                }
                m_open_web_net.onCommand(l_pr);
            }
        } catch (Exception e) {
            logger.error("Gateway [{}], Error processing receiveCommand '{}'", m_gateway_id,
                    new String[] { e.getMessage() });
        }
    }

    private String convertTemperatureBticino(String temperature) {
        // Encode temperature for Set Point
        String temp = "";
        if (temperature.indexOf(".") < 0) {
            temp = "0" + temperature + "0";
        } else {
            temp = "0" + temperature.substring(0, temperature.indexOf("."))
                    + temperature.substring(temperature.indexOf(".") + 1);
        }
        return (temp);
    }

    public void handleEvent(ProtocolRead p_protocol_read) throws Exception {
        // the events on the bus are now received
        // map them to events on the openhab bus
        logger.debug("Gateway [{}], Bticino WHO [{}], WHAT [{}], WHERE [{}]", m_gateway_id,
                p_protocol_read.getProperty("who"), p_protocol_read.getProperty("what"),
                p_protocol_read.getProperty("where"));

        // Get all the configs that are connected to this (who,where), multiple
        // possible
        List<BticinoBindingConfig> l_binding_configs = m_bticino_binding.getItemForBticinoBindingConfig(
                p_protocol_read.getProperty("who"), p_protocol_read.getProperty("where"));

        // log it when an event has occurred that no item is bound to
        if (l_binding_configs.isEmpty()) {
            logger.debug("Gateway [{}], No Item found for bticino event, WHO [{}], WHAT [{}], WHERE [{}]", m_gateway_id,
                    p_protocol_read.getProperty("who"), p_protocol_read.getProperty("what"),
                    p_protocol_read.getProperty("where"));
        }

        // every item associated with this who/where update the status
        for (BticinoBindingConfig l_binding_config : l_binding_configs) {
            // Get the Item out of the config
            Item l_item = l_binding_config.getItem();

            if (l_item instanceof SwitchItem) {
                // Lights
                if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("lighting")) {
                    logger.debug("Gateway [{}], RECEIVED EVENT FOR SwitchItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                            m_gateway_id, l_item.getName());

                    if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Light ON")) {
                        eventPublisher.postUpdate(l_item.getName(), OnOffType.ON);
                    } else if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Light OFF")) {
                        eventPublisher.postUpdate(l_item.getName(), OnOffType.OFF);
                    } else if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Light Dimmer")) {
                        int percentValue = (int) (Math
                                .floor(Integer.parseInt(p_protocol_read.getProperty("what")) * 10F));
                        eventPublisher.postUpdate(l_item.getName(), PercentType.valueOf(String.valueOf(percentValue)));
                    }
                }
                // CENs
                else if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("CEN Basic and Evolved")) {
                    // Pushbutton virtual address must match
                    if (l_binding_config.what.equalsIgnoreCase(p_protocol_read.getProperty("what"))) {
                        logger.debug("Gateway [{}], RECEIVED EVENT FOR SwitchItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                                m_gateway_id, l_item.getName());

                        if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Virtual pressure")) {
                            // only returns when finished
                            eventPublisher.sendCommand(l_item.getName(), OnOffType.ON);
                        } else if (p_protocol_read.getProperty("messageDescription")
                                .equalsIgnoreCase("Virtual release after short pressure")) {
                            // only returns when finished
                            eventPublisher.sendCommand(l_item.getName(), OnOffType.ON);
                        } else if (p_protocol_read.getProperty("messageDescription")
                                .equalsIgnoreCase("Virtual release after an extended pressure")) {
                            // only returns when finished
                            eventPublisher.sendCommand(l_item.getName(), OnOffType.ON);
                        } else if (p_protocol_read.getProperty("messageDescription")
                                .equalsIgnoreCase("Virtual extended pressure")) {
                            // only returns when finished
                            eventPublisher.sendCommand(l_item.getName(), OnOffType.ON);
                        }
                    }
                }
            } else if (l_item instanceof RollershutterItem) {
                logger.debug("Gateway [{}], RECEIVED EVENT FOR RollershutterItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                        m_gateway_id, l_item.getName());

                if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("automation")) {

                    if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Automation UP")) {
                        eventPublisher.postUpdate(l_item.getName(), UpDownType.UP);
                    } else if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Automation DOWN")) {
                        eventPublisher.postUpdate(l_item.getName(), UpDownType.DOWN);
                    }
                }
            } else if (l_item instanceof NumberItem) {
                logger.debug("Gateway [{}], RECEIVED EVENT FOR NumberItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                        m_gateway_id, l_item.getName());

                // THERMOREGULATION
                if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("thermoregulation")) {

                    switch (Integer.parseInt(l_binding_config.what)) {
                        case 0:
                            // Status Actual Temperature
                        case 12:
                            // Status Active Set Point Temperature
                        case 13:
                            // Status Offset Set Point Temperature
                        case 14:
                            // Status Set Point Temperature
                            if (p_protocol_read.getProperty("what").equalsIgnoreCase(l_binding_config.what)
                                    && p_protocol_read.getProperty("hStatus").equalsIgnoreCase("0")
                                    && p_protocol_read.getProperty("temperature") != null) {
                                logger.debug("T : {} : {}", l_item.getName(),
                                        p_protocol_read.getProperty("temperature"));
                                eventPublisher.postUpdate(l_item.getName(),
                                        DecimalType.valueOf(p_protocol_read.getProperty("temperature")));
                            }
                            break;

                        case 100:
                            // Status Operation Mode
                            if (p_protocol_read.getProperty("hStatus").equalsIgnoreCase("1")) {
                                logger.debug("T_ControlStatus : {}", l_item.getName());
                                eventPublisher.postUpdate(l_item.getName(),
                                        DecimalType.valueOf(p_protocol_read.getProperty("what")));
                            }
                            break;

                        case 101:
                            // Status Control Mode
                            if (p_protocol_read.getProperty("hStatus").equalsIgnoreCase("2")) {
                                logger.debug("T_ControlMode : {}", l_item.getName());
                                eventPublisher.postUpdate(l_item.getName(),
                                        DecimalType.valueOf(p_protocol_read.getProperty("what")));
                            }
                            break;

                        case 102:
                            // Status Main Unit
                            if (p_protocol_read.getProperty("hStatus").equalsIgnoreCase("3")) {
                                logger.debug("T_ControlMessage : {}", l_item.getName());
                                eventPublisher.postUpdate(l_item.getName(),
                                        DecimalType.valueOf(p_protocol_read.getProperty("what")));
                            }
                            break;
                    }
                }
            } else if (l_item instanceof ContactItem) {
                logger.debug("Gateway [{}], RECEIVED EVENT FOR ContactItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                        m_gateway_id, l_item.getName());

                // THERMOREGULATION
                if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("thermoregulation")) {
                    switch (Integer.parseInt(l_binding_config.what)) {
                        case 20:
                            // Status Actuators
                            if (p_protocol_read.getProperty("what").equalsIgnoreCase(l_binding_config.what)) {
                                int state = -1;
                                if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Heating OFF")) {
                                    state = 0;
                                    eventPublisher.postUpdate(l_item.getName(),
                                            (state == 1) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
                                } else if (p_protocol_read.getProperty("messageDescription")
                                        .equalsIgnoreCase("Heating ON")) {
                                    state = 1;
                                    eventPublisher.postUpdate(l_item.getName(),
                                            (state == 1) ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
                                }
                                logger.debug("T_Relay : {} / {}", l_item.getName(), state);
                            }
                            break;
                    }
                }
            } else if (l_item instanceof StringItem) {
                logger.debug("Gateway [{}], RECEIVED EVENT FOR StringItem [{}], TRANSLATE TO OPENHAB BUS EVENT",
                        m_gateway_id, l_item.getName());

                // THERMOREGULATION
                // to be used for Homekit
                if (p_protocol_read.getProperty("messageType").equalsIgnoreCase("thermoregulation")) {
                    String state = null;
                    switch (Integer.parseInt(l_binding_config.what)) {
                        case 20:
                            // Status Actuators
                            if (p_protocol_read.getProperty("what").equalsIgnoreCase(l_binding_config.what)) {
                                if (p_protocol_read.getProperty("messageDescription").equalsIgnoreCase("Heating OFF")) {
                                    state = "Off";
                                    eventPublisher.postUpdate(l_item.getName(), StringType.valueOf(state));
                                } else if (p_protocol_read.getProperty("messageDescription")
                                        .equalsIgnoreCase("Heating ON")) {
                                    state = "HeatOn";
                                    eventPublisher.postUpdate(l_item.getName(), StringType.valueOf(state));
                                }
                                logger.debug("T_Relay : {} / {}", l_item.getName(), state);
                            }
                            break;
                        case 101:
                            // Status Control Mode
                            if (p_protocol_read.getProperty("hStatus").equalsIgnoreCase("2")) {
                                switch (Integer.parseInt(p_protocol_read.getProperty("what"))) {
                                    case 1:
                                        state = "HeatOn";
                                        eventPublisher.postUpdate(l_item.getName(), StringType.valueOf(state));
                                        break;
                                    case 2:
                                        state = "CoolOn";
                                        eventPublisher.postUpdate(l_item.getName(), StringType.valueOf(state));
                                        break;
                                }
                                logger.debug("T_ControlMode : {}", l_item.getName());
                            }
                            break;
                    }
                }
            }
        }
    }
}
