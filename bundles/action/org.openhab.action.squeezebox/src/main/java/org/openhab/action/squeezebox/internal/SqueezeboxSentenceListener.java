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
package org.openhab.action.squeezebox.internal;

import java.util.concurrent.atomic.AtomicBoolean;

import org.openhab.io.squeezeserver.BaseSqueezePlayerEventListener;
import org.openhab.io.squeezeserver.SqueezePlayer.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by the Squeezebox action to detect when a sentence has
 * finished so it can restore the player state.
 *
 * @author Ben Jones
 * @since 1.3.0
 */
public class SqueezeboxSentenceListener extends BaseSqueezePlayerEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SqueezeboxSentenceListener.class);

    private final String playerId;

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(false);

    public SqueezeboxSentenceListener(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public void modeChangeEvent(PlayerEvent event) {
        if (!this.playerId.equals(event.getPlayerId())) {
            return;
        }

        if (event.getPlayer().isPlaying()) {
            this.started.set(true);
            logger.debug("Sentence started for {}", playerId);
        } else if (this.started.get() && event.getPlayer().isStopped()) {
            this.finished.set(true);
            logger.debug("Sentence finished for {}", playerId);
        }
    }

    public boolean isFinished() {
        return this.finished.get();
    }
}
