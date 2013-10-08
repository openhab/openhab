/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
 * 
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or 
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */


package org.openhab.binding.gpio.internal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.openhab.binding.gpio.GPIOBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.UnDefType;
import org.openhab.io.gpio.GPIO;
import org.openhab.io.gpio.GPIOPin;
import org.openhab.io.gpio.GPIOPinEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binds items with GPIO pins. Manages live for backend GPIO pin
 * objects and translates GPIO events to item events.
 * 
 * @author Dancho Penev
 * @since 1.3.1
 */
public class GPIOBinding extends AbstractBinding<GPIOBindingProvider> implements GPIOPinEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(GPIOBinding.class);
	private static final long REGISTRYLOCK_TIMEOUT = 10;
	private static final TimeUnit REGISTRYLOCK_TIMEOUT_UNITS = TimeUnit.SECONDS;

	/** Read/write lock protecting item/pin object registry. */
	private final ReentrantReadWriteLock registryLock = new ReentrantReadWriteLock();

	/** Bidirectional map for storing registered items and their
	 * corresponding pin objects.
	 */
	private final BidiMap registry = new DualHashBidiMap();

	/** GPIO IO service. */
	private volatile GPIO gpio = null;

	@Override
	public void allBindingsChanged(BindingProvider provider) {

		if (gpio != null) {
			for (String itemName : provider.getItemNames()) {
				changeItem((GPIOBindingProvider) provider, itemName);
			}
		}

		super.allBindingsChanged(provider);
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {

		if (gpio != null) {
			changeItem((GPIOBindingProvider) provider, itemName);
		}

		super.bindingChanged(provider, itemName);
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (gpio != null) {
			try {
				if (registryLock.readLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
					try {
						GPIOPin gpioPin = (GPIOPin) registry.get(itemName);

						if (gpioPin != null) {
							if (command == OnOffType.ON) {
								gpioPin.setValue(GPIOPin.VALUE_HIGH);
							} else {
								gpioPin.setValue(GPIOPin.VALUE_LOW);
							}
						}
					} catch (IOException e) {
						logger.error("Error occured while changing pin state for item " + itemName + ", exception: " + e.getMessage());
						return;
					} finally {
						registryLock.readLock().unlock();
					}
				} else {
					logger.error("Pin state for item " + itemName + " hasn't been changed, timeout expired while waiting for registry lock");
					return;
				}
			} catch (InterruptedException e) {
				logger.error("Pin state for item " + itemName + " hasn't been changed, thread was interrupted while waiting for registry lock");
				return;
			}
		}
		super.internalReceiveCommand(itemName, command);
	}

	/**
	 * Called when GPIO OSGi service is available, creates and configure
	 * backend objects for configured items.
	 *  
	 * @param gpio GPIO OSGi service
	 */
	public void bindGPIO(GPIO gpio) {

		this.gpio = gpio;

		for (GPIOBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				if (provider.isItemConfigured(itemName)) {
					newItem(provider, itemName);
				}
			}
		}
	}

	/**
	 * Called when GPIO OSGi service is stopped, deletes previously
	 * created backend objects for configured items.
	 * 
	 * @param gpio GPIO OSGi service
	 */
	public void unbindGPIO(GPIO gpio) {

		for (GPIOBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				deleteItem(itemName);
			}
		}

		this.gpio = null;
	}

	public void onEvent(GPIOPin pin, int value) {

		try {
			if (registryLock.readLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
				try {
					String itemName = (String) registry.getKey(pin);

					/* The item may be deleted while waiting to acquire read lock */
					if (itemName != null) {
						if (value == GPIOPin.VALUE_HIGH) {
							eventPublisher.postUpdate(itemName, OpenClosedType.OPEN);
						} else {
							eventPublisher.postUpdate(itemName, OpenClosedType.CLOSED);
						}
					}
				} finally {
					registryLock.readLock().unlock();
				}
			} else {
				logger.error("Item state hasn't been changed, timeout expired while waiting for registry lock");
			}
		} catch (InterruptedException e) {
			logger.error("Item state hasn't been changed, thread was interrupted while waiting for registry lock");
		}
	}

	public void onError(GPIOPin pin, Exception exception) {

		String itemName = null;

		logger.error("Error occured in pin event processing, exception: " + exception.getMessage());

		try {
			if (registryLock.readLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
				try {
					itemName = (String) registry.getKey(pin);
				} finally {
					registryLock.readLock().unlock();
				}
			} else {
				logger.error("Item name can't be determined, timeout expired while waiting for registry lock");
			}
		} catch (InterruptedException e) {
			logger.error("Item name can't be determined, thread was interrupted while waiting for registry lock");
		}

		/* The item may be deleted while waiting to acquire read lock */
		if (itemName != null) {
			deleteItem(itemName);
		}
	}

	/**
	 * Changes backend GPIO pin object for the item.
	 * 
	 * @param provider binding provider
	 * @param itemName the name of the item which to process
	 */
	private void changeItem(GPIOBindingProvider provider, String itemName) {

		if (provider.isItemConfigured(itemName)) {

			/* The item configuration was changed */

			try {
				if (registryLock.writeLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
					try {
						GPIOPin gpioPin = (GPIOPin) registry.get(itemName);

						/* Existing or new item */
						if (gpioPin != null) {

							/* Pin number change requires deletion of old and creation of new backend object */ 
							if (gpioPin.getPinNumber() != provider.getPinNumber(itemName)) {
								deleteItem(itemName);
								newItem(provider, itemName);
							} else {
								int newActiveLow = provider.getActiveLow(itemName);
								int currentDirection = gpioPin.getDirection();
								int newDirection = provider.getDirection(itemName);

								if (newActiveLow != gpioPin.getActiveLow())
									gpioPin.setActiveLow(newActiveLow);

								if (newDirection != currentDirection) {
									if (currentDirection == GPIOPin.DIRECTION_IN)
										/* Tracking interrupts on output pins is meaningless */
										gpioPin.removeEventHandler(this);
									gpioPin.setDirection(newDirection);
									if (newDirection == GPIOPin.DIRECTION_IN)
										gpioPin.addEventHandler(this);
								}
							}
						} else {
							newItem(provider, itemName);
						}
					} catch (Exception e) {
						logger.error("Error occured while changing backend object for item " + itemName + ", exception: " + e.getMessage());
					} finally {
						registryLock.writeLock().unlock();
					}
				} else {
					logger.error("Item " + itemName + " hasn't been changed, timeout expired while waiting for registry lock");
				}
			} catch (InterruptedException e) {
				logger.error("Item " + itemName + " hasn't been changed, thread was interrupted while waiting for registry lock");
			}
		} else {

			/* The item was deleted from configuration file or system is shut down */
			deleteItem(itemName);
		}
	}

	/**
	 * Setup new backend GPIO pin object and relate it with the item.
	 * 
	 * @param provider binding provider
	 * @param itemName the name of the item which to process
	 */
	private void newItem(GPIOBindingProvider provider, String itemName) {

		try {
			int direction;

			GPIOPin gpioPin = gpio.reservePin(provider.getPinNumber(itemName));

			gpioPin.setActiveLow(provider.getActiveLow(itemName));

			direction = provider.getDirection(itemName);
			gpioPin.setDirection(direction);

			gpioPin.setEdgeDetection(GPIOPin.EDGEDETECTION_BOTH);

			/* Register the pin */
			try {
				if (registryLock.writeLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
					try {
						registry.put(itemName, gpioPin);
					} finally {
						registryLock.writeLock().unlock();
					}
				} else {
					logger.error("Item " + itemName + " hasn't been inserted into the registry, timeout expired while waiting for registry lock");
					return;
				}
			} catch (InterruptedException e) {
				logger.error("Item " + itemName + " hasn't been inserted into the registry, thread was interrupted while waiting for registry lock");
				return;
			}

			/* Set initial item state */
			if (direction == GPIOPin.DIRECTION_IN) {

				/* Item type 'Contact' */
				if (gpioPin.getValue() == GPIOPin.VALUE_HIGH) {
					eventPublisher.postUpdate(itemName, OpenClosedType.OPEN);
				} else {
					eventPublisher.postUpdate(itemName, OpenClosedType.CLOSED);
				}
			} else {

				/* Item type 'Switch' */
				if (gpioPin.getValue() == GPIOPin.VALUE_HIGH) {
					eventPublisher.sendCommand(itemName, OnOffType.ON);
				} else {
					eventPublisher.sendCommand(itemName, OnOffType.OFF);
				}
			}

			/* The item is of type 'Contact', register for state change notifications */
			if (direction == GPIOPin.DIRECTION_IN) {
				gpioPin.addEventHandler(this);
			}
		} catch (Exception e) {
			logger.error("Error occured while creating backend object for item " +itemName + ", exception: "+ e.getMessage());
		}
	}

	/**
	 * Destroys backend GPIO pin object and clear relationship with the item.
	 * 
	 * @param itemName the name of the item which to process
	 */
	private void deleteItem(String itemName) {

		try {
			if (registryLock.writeLock().tryLock(REGISTRYLOCK_TIMEOUT, REGISTRYLOCK_TIMEOUT_UNITS)) {
				try {

					/* Remove the item from registry */
					GPIOPin gpioPin = (GPIOPin) registry.remove(itemName);

					/* Release the backend object */
					if (gpioPin != null) {
						try {
							gpio.releasePin(gpioPin);
						} catch (Exception e) {
							logger.error("Error occured while deleting backend object for item " +itemName + ", exception: "+ e.getMessage());
						}
					}

					/* Change the item state to 'Undefined' */
					eventPublisher.postUpdate(itemName, UnDefType.UNDEF);
				} finally {
					registryLock.writeLock().unlock();
				}
			} else {
				logger.error("Item " + itemName + " hasn't been deleted, timeout expired while waiting for registry lock");
			}
		} catch (InterruptedException e) {
			logger.error("Item " + itemName + " hasn't been deleted, thread was interrupted while waiting for registry lock");
		}
	}
}
