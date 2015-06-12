/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav;

/**
 * Public event notifier interface. Other bundles can register via this listener.
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public interface EventNotifier {
	/**
	 * invoked when event was loaded via caldav
	 * @param event the interesting event
	 */
	void eventLoaded(CalDavEvent event);
	
	/**
	 * invoked when event was removed from calendar and reloaded from caldav
	 * @param event the interesting event
	 */
	void eventRemoved(CalDavEvent event);
	
	/**
	 * invoked when event was changed (modify date of ics event
	 * @param event the interesting event
	 */
	void eventChanged(CalDavEvent event);
	
	/**
	 * invoked when event starts
	 * @param event the interesting event
	 */
	void eventBegins(CalDavEvent event);
	
	/**
	 * invoked when event ends
	 * @param event the interesting event
	 */
	void eventEnds(CalDavEvent event);
}
