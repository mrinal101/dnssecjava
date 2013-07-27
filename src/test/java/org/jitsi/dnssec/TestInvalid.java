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

package org.jitsi.dnssec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRSIGRecord;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

public class TestInvalid extends TestBase {
    @Test
    public void testUnknownAlg() throws IOException {
        Message response = resolver.send(createMessage("unknownalgorithm.dnssec.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:unknownalgorithm.dnssec.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    @Ignore // signature is incepted
    public void testSigNotIncepted() throws IOException {
        Message response = resolver.send(createMessage("signotincepted.dnssec.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:signotincepted.dnssec.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    public void testSigExpired() throws IOException {
        Message response = resolver.send(createMessage("sigexpired.dnssec.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:sigexpired.dnssec.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    public void testBogusSig() throws IOException {
        Message response = resolver.send(createMessage("bogussig.dnssec.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:bogussig.dnssec.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    public void testSignedBelowUnsignedBelowSigned() throws IOException {
        Message response = resolver.send(createMessage("ok.nods.ok.dnssec.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.NOERROR, response.getRcode());
        assertFalse(isEmptyAnswer(response));
        assertEquals("validate.insecure", getReason(response));
    }

    @Test
    public void testUnknownAlgNsec3() throws IOException {
        Message response = resolver.send(createMessage("unknownalgorithm.Nsec3.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:unknownalgorithm.nsec3.tjeb.nl.:failed.ds", getReason(response));
    }

//    @Test  disabled, the signature is actually valid
//    public void testSigNotInceptedNsec3() throws IOException {
//        Message response = resolver.send(createMessage("signotincepted.Nsec3.tjeb.nl./A"));
//        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
//        assertEquals(Rcode.SERVFAIL, response.getRcode());
//    }

    @Test
    public void testSigExpiredNsec3() throws IOException {
        Message response = resolver.send(createMessage("sigexpired.Nsec3.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:sigexpired.nsec3.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    public void testBogusSigNsec3() throws IOException {
        Message response = resolver.send(createMessage("bogussig.Nsec3.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:bogussig.nsec3.tjeb.nl.:failed.ds", getReason(response));
    }

    @Test
    public void testSignedBelowUnsignedBelowSignedNsec3() throws IOException {
        Message response = resolver.send(createMessage("ok.nods.ok.Nsec3.tjeb.nl./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.NOERROR, response.getRcode());
        assertFalse(isEmptyAnswer(response));
        assertEquals("validate.insecure", getReason(response));
    }

    @Test
    public void testUnsignedThatMustBeSigned() throws IOException {
        Name query = Name.fromString("www.ingotronic.ch.");

        // prepare a faked, unsigned response message that must have a signature
        // to be valid
        Message message = new Message();
        message.addRecord(Record.newRecord(query, Type.A, DClass.IN), Section.QUESTION);
        message.addRecord(new ARecord(query, Type.A, DClass.IN, InetAddress.getByName(localhost)), Section.ANSWER);
        add("www.ingotronic.ch./A", message);

        Message response = resolver.send(createMessage("www.ingotronic.ch./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.missingsig", getReason(response));
    }

    @Test
    public void testModifiedSignature() throws IOException {
        Name query = Name.fromString("www.ingotronic.ch.");

        // prepare a faked, unsigned response message that must have a signature
        // to be valid
        Message message = new Message();
        message.addRecord(Record.newRecord(query, Type.A, DClass.IN), Section.QUESTION);
        message.addRecord(new ARecord(query, Type.A, DClass.IN, InetAddress.getByName(localhost)), Section.ANSWER);
        message.addRecord(new RRSIGRecord(query, DClass.IN, 0, Type.A, Algorithm.RSASHA256, 5, new Date(System.currentTimeMillis() + 5000), new Date(System.currentTimeMillis() - 5000), 1234, Name.fromString("ingotronic.ch."), new byte[] { 1, 2, 3 }), Section.ANSWER);
        add("www.ingotronic.ch./A", message);

        Message response = resolver.send(createMessage("www.ingotronic.ch./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertTrue(getReason(response).startsWith("failed.answer.positive:{ www.ingotronic.ch."));
    }

    @Test
    public void testReturnServfailIfIntermediateQueryFails() throws IOException {
        Message message = new Message();
        message.getHeader().setRcode(Rcode.NOTAUTH);
        message.addRecord(Record.newRecord(Name.fromString("ch."), Type.DS, DClass.IN), Section.QUESTION);
        add("ch./DS", message);

        Message response = resolver.send(createMessage("www.ingotronic.ch./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        // rfc4035#section-5.5
        assertEquals(Rcode.SERVFAIL, response.getRcode());
        assertEquals("validate.bogus.badkey:ch.:failed.ds.unknown", getReason(response));
    }

    @Test
    public void testReturnOriginalRcodeIfPrimaryQueryFails() throws IOException {
        Message message = new Message();
        message.getHeader().setRcode(Rcode.REFUSED);
        message.addRecord(Record.newRecord(Name.fromString("www.ingotronic.ch."), Type.A, DClass.IN), Section.QUESTION);
        add("www.ingotronic.ch./A", message);

        Message response = resolver.send(createMessage("www.ingotronic.ch./A"));
        assertFalse("AD flag must not be set", response.getHeader().getFlag(Flags.AD));
        // rfc4035#section-5.5
        assertEquals(Rcode.REFUSED, response.getRcode());
        assertEquals("failed.nodata", getReason(response));
    }
}
