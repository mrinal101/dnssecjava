/*
 * dnssecjava - a DNSSEC validating stub resolver for Java
 * Copyright (C) 2013 Ingo Bauersachs. All rights reserved.
 *
 * This file is part of dnssecjava.
 *
 * Dnssecjava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Dnssecjava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with dnssecjava.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jitsi.dnssec.validator;

import org.jitsi.dnssec.SecurityStatus;

/**
 * Codes for DNSSEC security statuses along with a reason why the status was
 * determined.
 */
class JustifiedSecStatus {
    SecurityStatus status;
    String reason;

    /**
     * Creates a new instance of this class.
     * 
     * @param status The security status.
     * @param reason The reason why the status was determined.
     */
    JustifiedSecStatus(SecurityStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
