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

 package org.jitsi;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Message;
import org.xbill.DNS.Rcode;

public class TestWildcard extends TestBase {
    @Test
    public void testNameNotExpandedFromWildcardWhenNonWildcardExists() throws IOException {
//        ;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 60176
//        ;; flags: qr aa rd ra ; qd: 1 an: 2 au: 4 ad: 3 
//        ;; QUESTIONS:
//        ;;  a.d.ingotronic.ch., type = A, class = IN
//
//        ;; ANSWERS:
//        a.d.ingotronic.ch.  300 IN  A   127.0.0.2
//        a.d.ingotronic.ch.  300 IN  RRSIG   A 5 3 300 20130531135453 20130501134258 17430 ingotronic.ch. IZWwjIZi58dHBY9/ui7O1NsRJzSBG9LuwkQ+AEnzLjTtZzRgVJAhDJj2XijEXyMsV0hoNNXWWoDdOLRgbN4w5/tLDaBZD2M1dyvNlamRdb49x0G1TJq1uiXR7a1t0PN6c9keUDRteA84VwqAXbiCWnutWVOiNQ16tH0LkgjK5uc=
//
//        ;; AUTHORITY RECORDS:
//        ingotronic.ch.      300 IN  NS  ns1.ingotronic.ch.
//        ingotronic.ch.      300 IN  RRSIG   NS 5 2 300 20130531140553 20130501134258 17430 ingotronic.ch. d5muom+0sQUrxRSo8lI1n2rT4XG7PP3WqZxpwXZWejRHwJsWyjYIrQq6K59GKfS116oQoRs3sdav4/Ujb3Cqkq8T5/Wj38mvqSOanakly6P6K7bMOCS1zPZ95QgdyxDsQH3nDHRWUmDSl/NSCkSVWiBTMBtFwgmIUiMB+HCV/fk=
//        a.d.ingotronic.ch.  300 IN  NSEC    invalid.ingotronic.ch. A RRSIG NSEC
//        a.d.ingotronic.ch.  300 IN  RRSIG   NSEC 5 4 300 20130531135453 20130501134258 17430 ingotronic.ch. LsXsCIWEqG2pAtx+I/rfrlP2ToAhl9D0VcE01kd/Dcu+GSrashuMdziJi0bKgzFVUHLEM4TMSwopyy1u9eSb3xPfLAwsrXsJB3Kx3Kiz2lVn95RliUlgzgLnxN5Vr95kBxGV0yqQkB+bWsVUdJpZCoYMWd1KUJJ4m51ZtmKjScY=
//
//        ;; ADDITIONAL RECORDS:
//        ns1.ingotronic.ch.  300 IN  A   62.192.5.131
//        ns1.ingotronic.ch.  300 IN  RRSIG   A 5 3 300 20130531140814 20130501134258 17430 ingotronic.ch. A86YmGMp/CQZfvbhXRMIrVbQwIGz1iSWEAMbV4XgeHIvAM00TJmbpvQnekhmffEBYeokTf4lvVSIBfQ+t6GcHT583CMZ0ecfmEJhgei8H9ON85OFtl/59YCUO+Ew6R1T8hrDG3R5P/vP0OuVfNna1WRJZ1ighvoqpghZu7kXgRQ=
//        .           32768   CLASS4096   OPT  ; payload 4096, xrcode 0, version 0, flags 32768
//
//        ;; Message size: 833 bytes
        //this a faked response: the original query/response was for b.d.ingotronic.ch. and is now changed to a.d.ingotronic.ch.
        add("a.d.ingotronic.ch./A",
                "EB1085800001000200040003016101640A696E676F74726F6E69630263680000010001C00C000100010000012C00047F000002C00C002E00010000012C00A1000105030000012C51A8ABAD51811BE244160A696E676F74726F6E6963026368002195B08C8662E7C747058F7FBA2ECED4DB112734811BD2EEC2443E0049F32E34ED6734605490210C98F65E28C45F232C57486834D5D65A80DD38B4606CDE30E7FB4B0DA0590F6335772BCD95A99175BE3DC741B54C9AB5BA25D1EDAD6DD0F37A73D91E50346D780F38570A805DB8825A7BAD5953A2350D7AB47D0B9208CAE6E7C010000200010000012C0006036E7331C010C010002E00010000012C00A1000205020000012C51A8AE4151811BE244160A696E676F74726F6E6963026368007799AEA26FB4B1052BC514A8F252359F6AD3E171BB3CFDD6A99C69C176567A3447C09B16CA3608AD0ABA2B9F4629F4B5D7AA10A11B37B1D6AFE3F5236F70AA92AF13E7F5A3DFC9AFA9239A9DA925CBA3FA2BB6CC3824B5CCF67DE5081DCB10EC407DE70C74565260D297F3520A44955A2053301B45C20988522301F87095FDF90161C00E002F00010000012C001F07696E76616C69640A696E676F74726F6E6963026368000006400000000003C19F002E00010000012C00A1002F05040000012C51A8ABAD51811BE244160A696E676F74726F6E6963026368002EC5EC088584A86DA902DC7E23FADFAE53F64E802197D0F455C134D6477F0DCBBE192ADAB21B8C7738898B46CA8331555072C43384CC4B0A29CB2D6EF5E49BDF13DF2C0C2CAD7B090772B1DCA8B3DA5567F79465894960CE02E7C4DE55AFDE64071195D32A90901F9B5AC554749A590A860C59DD4A5092789B9D59B662A349C6C0EC000100010000012C00043EC00583C0EC002E00010000012C00A1000105030000012C51A8AECE51811BE244160A696E676F74726F6E69630263680003CE98986329FC24197EF6E15D1308AD56D0C081B3D6249610031B5785E078722F00CD344C999BA6F4277A48667DF10161EA244DFE25BD548805F43EB7A19C1D3E7CDC2319D1E71F98426181E8BC1FD38DF39385B65FF9F580943BE130E91D53F21AC31B74793FFBCFD0EB957CD9DAD564496758A086FA2AA60859BBB91781140000291000000080000000");
//                                         ^^

        // a.d.ingotronic.ch./A exists, but the response is faked from *.d.ingotronic.ch. which must be detected by the NSEC proof
        Message response = resolver.send(createMessage("a.d.ingotronic.ch./A"));
        assertFalse(response.getHeader().getFlag(Flags.AD));
        assertEquals(Rcode.SERVFAIL, response.getHeader().getRcode());
    }
}
