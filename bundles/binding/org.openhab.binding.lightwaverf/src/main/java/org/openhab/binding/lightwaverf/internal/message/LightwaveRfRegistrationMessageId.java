/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.lightwaverf.internal.message;

import java.util.Objects;

/**
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfRegistrationMessageId implements LightwaveRfMessageId {

    private final int messageId;

    public LightwaveRfRegistrationMessageId() {
        this.messageId = 100;
    }

    @Override
    public String getMessageIdString() {
        return String.valueOf(messageId);
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof LightwaveRfRegistrationMessageId) {
            return Objects.equals(this.messageId, ((LightwaveRfRegistrationMessageId) that).messageId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "LightwaveRfRegistrationMessageId[" + messageId + "]";
    }
}
