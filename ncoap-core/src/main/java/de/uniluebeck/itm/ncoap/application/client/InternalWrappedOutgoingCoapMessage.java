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

import de.uniluebeck.itm.ncoap.message.CoapMessage;

/**
 * The {@link InternalWrappedOutgoingCoapMessage} is an internal wrapper class for outgoing messages sent by a client.
 *
 * @author Oliver Kleine
 */
public class InternalWrappedOutgoingCoapMessage {

    private final CoapMessage coapMessage;
    private final CoapResponseProcessor coapResponseProcessor;

    public InternalWrappedOutgoingCoapMessage(CoapMessage coapMessage, CoapResponseProcessor coapResponseProcessor){
        this.coapMessage = coapMessage;
        this.coapResponseProcessor = coapResponseProcessor;
    }

    /**
     * Returns the {@link CoapMessage} to be sent
     *
     * @return the {@link CoapMessage} to be sent
     */
    public CoapMessage getCoapMessage() {
        return coapMessage;
    }

    /**
     * Returns the {@link CoapResponseProcessor} to process the awaited {@link CoapMessage}.
     *
     * @return the {@link CoapResponseProcessor} to process the awaited {@link CoapMessage}.
     */
    public CoapResponseProcessor getCoapResponseProcessor() {
        return coapResponseProcessor;
    }
}
