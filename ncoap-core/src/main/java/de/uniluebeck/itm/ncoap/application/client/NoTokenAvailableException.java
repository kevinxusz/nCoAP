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

import java.net.InetSocketAddress;

/**
 * Exception that is thrown (and handled internally) if there is no {@link Token} available to start communicating
 * with another CoAP endpoints, i.e. a CoAP server.
 *
 * @author Oliver Kleine
 */
public class NoTokenAvailableException extends Exception {

    private InetSocketAddress remoteEndpoint;

    /**
     * Creates a new instance of {@link NoTokenAvailableException}.
     * @param remoteEndpoint the address of the CoAP endpoints for which all possible {@link Token}s are
     *                       already in use
     */
    public NoTokenAvailableException(InetSocketAddress remoteEndpoint){
        this.remoteEndpoint = remoteEndpoint;
    }

    /**
     * Returns the address of the CoAP endpoints for which all possible {@link Token}s are already in use
     *
     * @return the address of the CoAP endpoints for which all possible {@link Token}s are already in use
     */
    public InetSocketAddress getRemoteEndpoint() {
        return remoteEndpoint;
    }
}
