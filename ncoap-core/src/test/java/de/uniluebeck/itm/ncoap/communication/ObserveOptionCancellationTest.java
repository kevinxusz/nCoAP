/**
 * Copyright (c) 2012, Oliver Kleine, Institute of Telematics, University of Luebeck
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  - Redistributions of source messageCode must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
///**
// * Copyright (c) 2012, Oliver Kleine, Institute of Telematics, University of Luebeck
// * All rights reserved
// *
// * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
// * following conditions are met:
// *
// *  - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
// *    disclaimer.
// *
// *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
// *    following disclaimer in the documentation and/or other materials provided with the distribution.
// *
// *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
// *    products derived from this software without specific prior written permission.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//package de.uniluebeck.itm.ncoap.communication;
//
//import de.uniluebeck.itm.ncoap.plugtest.client.CoapResponseTestProcessor;
//import de.uniluebeck.itm.ncoap.plugtest.endpoints.CoapTestEndpoint;
//import de.uniluebeck.itm.ncoap.application.server.CoapServerApplication;
//import de.uniluebeck.itm.ncoap.plugtest.server.webservice.ObservableTestWebservice;
//import de.uniluebeck.itm.ncoap.message.CoapMessage;
//import de.uniluebeck.itm.ncoap.message.CoapRequest;
//import de.uniluebeck.itm.ncoap.message.CoapResponse;
//import de.uniluebeck.itm.ncoap.message.MessageCode;
//import de.uniluebeck.itm.ncoap.message.MessageType;
//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import java.net.InetSocketAddress;
//import java.net.URI;
//import java.nio.charset.Charset;
//import java.util.Iterator;
//import java.util.SortedMap;
//
//import static junit.framework.Assert.assertEquals;
//import static de.uniluebeck.itm.ncoap.message.options.OptionRegistry.OptionName.*;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
///**
//* Tests for the removal of observers.
//*
//* @author Stefan Hueske, Oliver Kleine
//*/
//public class ObserveOptionCancellationTest extends AbstractCoapCommunicationTest {
//
//    private static String PATH_TO_SERVICE = "/observable";
//
//    private static CoapServerApplication server;
//    private static ObservableTestWebservice service;
//
//    private static CoapTestEndpoint endpoints;
//    private static CoapResponseTestProcessor responseProcessor;
//
//    //requests
//    private static CoapRequest observationRequest1;
//    private static CoapRequest normalRequest;
//    private static CoapRequest observationRequest2;
//
//    @Override
//    public void setupLogging() throws Exception {
//        Logger.getLogger("de.uniluebeck.itm.ncoap.communication.observe")
//                .setLevel(Level.DEBUG);
//        Logger.getLogger("de.uniluebeck.itm.ncoap.plugtest.endpoints.CoapTestEndpoint")
//                .setLevel(Level.DEBUG);
//        Logger.getLogger("de.uniluebeck.itm.ncoap.communication.encoding.CoapMessageDecoder")
//                .setLevel(Level.DEBUG);
//        Logger.getLogger("de.uniluebeck.itm.ncoap.communication.reliability.outgoing")
//                .setLevel(Level.DEBUG);
//    }
//
//    @Override
//    public void setupComponents() throws Exception {
//        server = new CoapServerApplication(0);
//        service = new ObservableTestWebservice(PATH_TO_SERVICE, 1, 0, 1000);
//        service.setUpdateNotificationConfirmable(false);
//        server.registerService(service);
//
//        endpoints = new CoapTestEndpoint();
//        responseProcessor = new CoapResponseTestProcessor();
//
//        URI targetURI = new URI("coap://localhost:" + server.getServerPort() + PATH_TO_SERVICE);
//
//        observationRequest1 = new CoapRequest(MessageType.CON, MessageCode.GET, targetURI);
//        observationRequest1.setObserveOptionRequest();
//        observationRequest1.getHeader().setMsgID(123);
//        observationRequest1.setToken(new byte[]{1,2,3,4});
//
//        normalRequest = new CoapRequest(MessageType.CON, MessageCode.GET, targetURI);
//        normalRequest.getHeader().setMsgID(456);
//
//        observationRequest2 = new CoapRequest(MessageType.CON, MessageCode.GET, targetURI);
//        observationRequest2.setObserveOptionRequest();
//        observationRequest2.getHeader().setMsgID(789);
//        observationRequest2.setToken(new byte[]{5,6,7,8});
//    }
//
//    @Override
//    public void shutdownComponents() throws Exception {
//        server.shutdown();
//        endpoints.shutdown();
//    }
//
//
//    @Override
//    public void createTestScenario() throws Exception {
//
////             testEndpoint                    Server        DESCRIPTION
////                  |                             |
////                  |--------GET_OBSERVE--------->|          Register observer
////                  |                             |
////                  |<-------1st Notification-----|          Receive first notification
////                  |                             |
////                  |                             |  <-----  Status update (new status: 2) (after 1000 ms)
////                  |                             |
////                  |<-------2nd Notification-----|
////                  |                             |
////                  |--------GET----------------->|          Remove observer using a GET request without observe option
////                  |                             |
////                  |<-------Simple ACK response--|          Receive ACK response (without observe option)
////                  |                             |
////                  |                             |
////                  |                             |  <-----  Status update (new status: 3) (after 2000 ms)
////                  |                             |
////                  |                             |          some time passes... nothing should happen!
////                  |                             |
////                  |                             |  <-----  Status update (new status: 4) (after 3000 ms)
////                  |                             |
////                  |--------GET_OBSERVE--------->|          Register observer
////                  |                             |
////                  |<-------1st Notification-----|          Receive first notification (ACK)
////                  |                             |
////                  |                             |  <-----  Status update (new status: 5) (after 4000 ms)
////                  |                             |
////                  |<-------2nd Notification-----|
////                  |                             |
////                  |--------RST----------------->|          Respond with reset to the 2nd notification
////                  |                             |
//
//        //schedule first observation request
//        endpoints.writeMessage(observationRequest1, new InetSocketAddress("localhost", server.getServerPort()));
//        //Wait for ACK and one NON update notification
//        Thread.sleep(1100);
//
//        //write normal GET request to cancel observation
//        endpoints.writeMessage(normalRequest, new InetSocketAddress("localhost", server.getServerPort()));
//        //Wait for ACK, there should be no update notification for new status 3
//        Thread.sleep(2000);
//
//        //write second observation request
//        endpoints.writeMessage(observationRequest2, new InetSocketAddress("localhost", server.getServerPort()));
//        //wait for ACK and one NON update notification
//        Thread.sleep(1000);
//
//        //Write reset message to cancel observation
//        int messageID =
//                endpoints.getReceivedCoapMessages().get(endpoints.getReceivedCoapMessages().lastKey()).getMessageID();
//        endpoints.writeMessage(CoapMessage.createEmptyReset(messageID),
//                        new InetSocketAddress("localhost", server.getServerPort()));
//
//        //There is another status update but the endpoints should be already removed as observer
//        Thread.sleep(2000);
//    }
//
//
//
//    @Test
//    public void testReceiverReceived5Messages() {
//        String message = "Receiver did not receive 5 messages";
//        assertEquals(message, 5, endpoints.getReceivedCoapMessages().values().size());
//    }
//
//    @Test
//    public void testFirstReceivedMessage() {
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        CoapMessage receivedMessage = receivedMessages.get(receivedMessages.firstKey());
//
//        String message = "1st notification: MessageType is not ACK";
//        assertEquals(message, MessageType.ACK, receivedMessage.getMessageType());
//
//        message = "1st notification: MessageCode is not 2.05 (Content)";
//        assertEquals(message, MessageCode.CONTENT_205, receivedMessage.getMessageCode());
//
//        message = "1st notification: Payload does not match";
//        assertEquals(message, "Status #1",receivedMessage.getContent().toString(Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void testSecondReceivedMessage() {
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
//        timeKeys.next();
//
//        CoapMessage receivedMessage = receivedMessages.get(timeKeys.next());
//
//        String message = "Message type is not NON";
//        assertEquals(message, MessageType.NON, receivedMessage.getMessageType());
//
//        message = "Simple ACK response: MessageCode is not 2.05 (Content)";
//        assertEquals(message, MessageCode.CONTENT_205, receivedMessage.getMessageCode());
//
//        message = "Payload does not match.";
//
//        assertEquals(message, "Status #2", receivedMessage.getContent().toString(Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void testThirdReceivedMessage() {
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
//        timeKeys.next();
//        timeKeys.next();
//
//        CoapMessage receivedMessage = receivedMessages.get(timeKeys.next());
//
//        String message = "Message type is not ACK.";
//        assertEquals(message, MessageType.ACK, receivedMessage.getMessageType());
//
//        message = "MessageCode is not 2.05 (Content)";
//        assertEquals(message, MessageCode.CONTENT_205, receivedMessage.getMessageCode());
//
//        message = "Payload does not match.";
//        assertEquals(message, "Status #2", receivedMessage.getContent().toString(Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void test4thReceivedMessage() {
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
//        timeKeys.next();
//        timeKeys.next();
//        timeKeys.next();
//
//        CoapMessage receivedMessage = receivedMessages.get(timeKeys.next());
//
//        String message = "MessageType is not ACK";
//        assertEquals(message, MessageType.ACK, receivedMessage.getMessageType());
//
//        message = "MessageCode is not 2.05 (Content)";
//        assertEquals(message, MessageCode.CONTENT_205, receivedMessage.getMessageCode());
//
//        message = "Payload does not match";
//        assertEquals(message, "Status #4", receivedMessage.getContent().toString(Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void test5thReceivedMessage() {
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
//        timeKeys.next();
//        timeKeys.next();
//        timeKeys.next();
//        timeKeys.next();
//
//        CoapMessage receivedMessage = receivedMessages.get(timeKeys.next());
//
//        String message = "MessageType is not NON";
//        assertEquals(message, MessageType.NON, receivedMessage.getMessageType());
//
//        message = "MessageCode is not 2.05 (Content)";
//        assertEquals(message, MessageCode.CONTENT_205, receivedMessage.getMessageCode());
//
//        message = "Payload does not match";
//        assertEquals(message, "Status #5", receivedMessage.getContent().toString(Charset.forName("UTF-8")));
//    }
//
//    @Test
//    public void testObserveResponseOptions(){
//        SortedMap<Long, CoapMessage> receivedMessages = endpoints.getReceivedCoapMessages();
//        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
//
//        CoapResponse coapResponse1 = (CoapResponse) receivedMessages.get(timeKeys.next());
//        long notificationCount1 = (Long) coapResponse1.getOption(OBSERVE_RESPONSE).get(0).getDecodedValue();
//
//        CoapResponse coapResponse2 = (CoapResponse) receivedMessages.get(timeKeys.next());
//        long notificationCount2 = (Long) coapResponse2.getOption(OBSERVE_RESPONSE).get(0).getDecodedValue();
//
//        assertTrue("Notification count of response 2 is not larger than of response 1.",
//                notificationCount2 > notificationCount1);
//
//        CoapResponse coapResponse3 = (CoapResponse) receivedMessages.get(timeKeys.next());
//        assertFalse("Response 3 is an update notification!", coapResponse3.isUpdateNotification());
//
//        CoapResponse coapResponse4 = (CoapResponse) receivedMessages.get(timeKeys.next());
//        long notificationCount4 = (Long) coapResponse4.getOption(OBSERVE_RESPONSE).get(0).getDecodedValue();
//
//        CoapResponse coapResponse5 = (CoapResponse) receivedMessages.get(timeKeys.next());
//        long notificationCount5 = (Long) coapResponse5.getOption(OBSERVE_RESPONSE).get(0).getDecodedValue();
//
//        assertTrue("Notification count of response 5 is not larger than of response 4.",
//                notificationCount5 > notificationCount4);
//    }
//
//
//
////    @Test
////    public void testObserveOptions() {
////        SortedMap<Long, CoapMessage> receivedMessages = testEndpoint.getReceivedCoapMessages();
////        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
////        //reg1 notification1
////        CoapMessage receivedMessage1 = receivedMessages.get(timeKeys.next());
////        //simple ack
////        CoapMessage receivedMessage2 = receivedMessages.get(timeKeys.next());
////        //reg2 notification1
////        CoapMessage receivedMessage3 = receivedMessages.get(timeKeys.next());
////        //reg2 notification2
////        CoapMessage receivedMessage4 = receivedMessages.get(timeKeys.next());
////
////
////        String message = "Observe Option should be set for reg1 notification1";
////        assertFalse(message, receivedMessage1.getOption(OBSERVE_RESPONSE).isEmpty());
////        message = "Observe Option should NOT be set for simple ack response";
////        assertTrue(message, receivedMessage2.getOption(OBSERVE_RESPONSE).isEmpty());
////        message = "Observe Option should be set for reg2 notification1";
////        assertFalse(message, receivedMessage3.getOption(OBSERVE_RESPONSE).isEmpty());
////        message = "Observe Option should be set for reg2 notification2";
////        assertFalse(message, receivedMessage4.getOption(OBSERVE_RESPONSE).isEmpty());
////    }
////
////    @Test
////    public void testFirstNotificationHasSameMsgID() {
////        SortedMap<Long, CoapMessage> receivedMessages = testEndpoint.getReceivedCoapMessages();
////        CoapMessage receivedMessage = receivedMessages.get(receivedMessages.firstKey());
////        String message = "First notification Msg ID does not match with request Msg ID";
//////        assertEquals(message, coapRequest.getMessageID(), receivedMessage.getMessageID());
////    }
////
////    @Test
////    public void testReg2NotificationsHaveTheSameToken() {
////        SortedMap<Long, CoapMessage> receivedMessages = testEndpoint.getReceivedCoapMessages();
////        Iterator<Long> timeKeys = receivedMessages.keySet().iterator();
////        CoapMessage reg1notification1 = receivedMessages.get(timeKeys.next());
////        timeKeys.next();
////        CoapMessage reg2notification1 = receivedMessages.get(timeKeys.next());
////        CoapMessage reg2notification2 = receivedMessages.get(timeKeys.next());
////
////        String message = "At least one notification has a wrong token";
////        assertTrue(message, Arrays.equals(reg1Request.getToken(), reg1notification1.getToken()));
////        assertTrue(message, Arrays.equals(reg2Request.getToken(), reg2notification1.getToken()));
////        assertTrue(message, Arrays.equals(reg2Request.getToken(), reg2notification2.getToken()));
////    }
//
//}
