/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.internal;

import java.util.Dictionary;

import org.openhab.binding.mailcontrol.MailControlBindingProvider;
import org.openhab.binding.mailcontrol.connection.ConnectorBuilder;
import org.openhab.binding.mailcontrol.service.MessagesService;
import org.openhab.binding.mailcontrol.service.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.creek.accessemail.connector.mail.MailConnector;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class MailControlBinding <T extends Command> extends AbstractActiveBinding<MailControlBindingProvider> implements ManagedService {
    private ConnectorBuilder connectorBuilder;
    private MessagesService<T> service;

	private static final Logger logger = LoggerFactory.getLogger(MailControlBinding.class);
	
	/** 
	 * the refresh interval which is used to poll values from the MailControl
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	public MailControlBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "MailControl Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
        // the frequently executed code (polling) goes here ...
        logger.debug("execute() method is called!");

//        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
//        if (bundleContext != null) {
//            ServiceReference<?> serviceReference2 = bundleContext.getServiceReference(ItemRegistry.class.getName());
//            if (serviceReference2 != null) {
//                ItemRegistry itemregistry = (ItemRegistry) bundleContext.getService(serviceReference2);
//                logger.debug("--------itemregistry!=null " + itemregistry.getItems().size());
//                for (Item item: itemregistry.getItems()) {
//                    logger.debug("------------: " + item.getName() + " " + item.getState());
//                }
//            } else {
//                logger.error("--------itemregistry=null");
//            }
//        } else {
//            logger.error("-----------bundleContext=null");
//        }

        try {
            MailConnector connector = connectorBuilder.createAndCheckMailConnector();
            service = new MessagesService<T>(connector, eventPublisher);
            service.receiveMessages();
        } catch(ServiceException ex) {
            logger.error("Cannot receive messages", ex);
        } catch(ConfigurationException ex) {
            logger.error("Cannot receive messages", ex);
        }
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            this.connectorBuilder = new ConnectorBuilder(config);
            connectorBuilder.createAndCheckMailConnector();
            
            // to override the default refresh interval one has to add a 
            // parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
            String refreshIntervalString = (String) config.get("refresh");
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }
            
            // read further config parameters here ...

            setProperlyConfigured(true);
        }
	}
}
