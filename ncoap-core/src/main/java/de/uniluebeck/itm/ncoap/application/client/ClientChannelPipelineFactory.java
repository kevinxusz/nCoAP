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

package de.uniluebeck.itm.ncoap.application.client;

import de.uniluebeck.itm.ncoap.application.AbstractCoapChannelPipelineFactory;
import de.uniluebeck.itm.ncoap.communication.codec.CoapMessageDecoder;
import de.uniluebeck.itm.ncoap.communication.codec.CoapMessageEncoder;
import de.uniluebeck.itm.ncoap.communication.observe.client.IncomingUpdateNotificationHandler;
import de.uniluebeck.itm.ncoap.communication.reliability.incoming.IncomingMessageReliabilityHandler;
import de.uniluebeck.itm.ncoap.communication.reliability.outgoing.OutgoingMessageReliabilityHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.handler.execution.ExecutionHandler;
import de.uniluebeck.itm.ncoap.message.CoapRequest;

import java.util.concurrent.ScheduledExecutorService;


/**
 * Factory to provide the {@link ChannelPipeline} for a newly created {@link DatagramChannel} for a
 * {@link CoapClientApplication}.
 *
 * @author Oliver Kleine
 */
public class ClientChannelPipelineFactory extends AbstractCoapChannelPipelineFactory {

    /**
     * Creates a new instance of {@link ClientChannelPipelineFactory}.
     *
     * @param executorService The {@link ScheduledExecutorService} to provide the thread(s) for I/O operations
     *
     * @param tokenFactory The {@link TokenFactory} to be used for generating {@link Token}s for outgoing
     *                     {@link CoapRequest}s
     */
    public ClientChannelPipelineFactory(ScheduledExecutorService executorService, TokenFactory tokenFactory){

        addChannelHandler(EXECUTION_HANDLER, new ExecutionHandler(executorService));

        addChannelHandler(ENCODER, new CoapMessageEncoder());
        addChannelHandler(DECODER, new CoapMessageDecoder());

        addChannelHandler(INCOMING_UPDATE_NOTIFICATION_HANDLER, new IncomingUpdateNotificationHandler());

        addChannelHandler(OUTGOING_MESSAGE_RELIABILITY_HANDLER, new OutgoingMessageReliabilityHandler(executorService));

        addChannelHandler(INCOMING_MESSAGE_RELIABILITY_HANDLER, new IncomingMessageReliabilityHandler(executorService));

        addChannelHandler(RESPONSE_DISPATCHER, new CoapResponseDispatcher(executorService, tokenFactory));
    }

}
