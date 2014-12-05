/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.command;

import org.creek.mailcontrol.model.command.StopMoveCommand;
import org.creek.mailcontrol.model.types.StopMoveDataType;
import org.openhab.core.library.types.StopMoveType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabStopMoveCommand extends OpenhabCommand<StopMoveDataType, StopMoveCommand> implements OpenhabCommandTransformable<StopMoveType> {
    public OpenhabStopMoveCommand(StopMoveCommand command) {
        super(command);
    }

    @Override
    public StopMoveType getCommandValue() {
        return StopMoveType.valueOf(data.name());
    }
}
