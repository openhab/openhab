/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.library.location.items;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.library.location.types.LocationType;

/**
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class LocationItem extends GenericItem {
	private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
	private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

	static {
		acceptedDataTypes.add(LocationType.class);
		acceptedDataTypes.add(UnDefType.class);
	}
	
	public LocationItem(String name) {
		super(name);
	}

	public List<Class<? extends State>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}

	public List<Class<? extends Command>> getAcceptedCommandTypes() {
		return acceptedCommandTypes;
	}
}
