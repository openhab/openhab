/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.OpenClosedCommand;
import org.creek.mailcontrol.model.types.OpenClosedDataType;
import org.openhab.core.library.types.OpenClosedType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.6.0
 */
public class OpenhabOpenClosedCommand implements OpenhabCommandTransformable {
    private final OpenClosedDataType data;
    
    public OpenhabOpenClosedCommand(OpenClosedCommand command) {
        this.data = (OpenClosedDataType)command.getData();
    }

    @Override
    public OpenClosedType getCommandValue() {
        return OpenClosedType.valueOf(data.name());
    }
}
