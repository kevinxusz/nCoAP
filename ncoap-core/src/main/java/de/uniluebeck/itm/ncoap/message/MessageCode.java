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
/**
* Copyright (c) 2012, Oliver Kleine, Institute of Telematics, University of Luebeck
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
* following conditions are met:
*
* - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
* disclaimer.
* - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
* following disclaimer in the documentation and/or other materials provided with the distribution.
* - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
* products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
* INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
* GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
* OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package de.uniluebeck.itm.ncoap.message;

import java.util.HashMap;

/**
 * This enumeration contains all defined message codes (i.e. methods for requests and status for responses)
 * in CoAPs draft v7
 *
 * @author Oliver Kleine
*/

public abstract class MessageCode {

    private static final HashMap<Integer, Name> validNumbers = new HashMap<Integer, Name>();

    public static enum Name{
         UNKNOWN(-1),
         EMPTY(0),
         GET(1),
         POST(2),
         PUT(3),
         DELETE(4),
         CREATED_201(65),
         DELETED_202(66),
         VALID_203(67),
         CHANGED_204(68),
         CONTENT_205(69),
         BAD_REQUEST_400(128),
         UNAUTHORIZED_401(129),
         BAD_OPTION_402(130),
         FORBIDDEN_403(131),
         NOT_FOUND_404(132),
         METHOD_NOT_ALLOWED_405(133),
         NOT_ACCEPTABLE_406(134),
         PRECONDITION_FAILED_412(140),
         REQUEST_ENTITY_TOO_LARGE_413(141),
         UNSUPPORTED_CONTENT_FORMAT_415(143),
         INTERNAL_SERVER_ERROR_500(160),
         NOT_IMPLEMENTED_501(161),
         BAD_GATEWAY_502(162),
         SERVICE_UNAVAILABLE_503(163),
         GATEWAY_TIMEOUT_504(164),
         PROXYING_NOT_SUPPORTED_505(165);

        private int number;

        private Name(int number){
            this.number = number;
            validNumbers.put(number, this);
        }


        public int getNumber() {
            return this.number;
        }

        /**
         * Returns the {@link Name} corresponding to the given number or {@link Name#UNKNOWN} if no such {@link Name}
         * exists.
         *
         * @return the {@link Name} corresponding to the given number or {@link Name#UNKNOWN} if no such {@link Name}
         * exists.
         */
        public static Name getName(int number){
            if(validNumbers.containsKey(number))
                return validNumbers.get(number);
            else
                return Name.UNKNOWN;
        }

        public static boolean isMessageCode(int number){
            return validNumbers.containsKey(number);
        }

        public static boolean isRequest(Name messageCode){
            return MessageCode.isResponse(messageCode.getNumber());
        }

        public static boolean isResponse(Name messageCode) {
            return MessageCode.isResponse(messageCode.getNumber());
        }

    }

    /**
     * This method indicates whether the given number refers to a {@link MessageCode} for {@link CoapRequest}s.
     *
     * <b>Note:</b> Messages with {@link MessageCode.Name#EMPTY} are considered neither a response nor a request
     *
     * @return <code>true</code> in case of a request code, <code>false</code> otherwise.
     *
     */
    public static boolean isRequest(int codeNumber){
        return (codeNumber > 0 && codeNumber < 5);
    }

    /**
     * This method indicates whether the given {@link MessageCode.Name} indicates a {@link CoapRequest}.
     *
     * <b>Note:</b> Messages with {@link MessageCode.Name#EMPTY} are considered neither a response nor a request
     *
     * @return <code>true</code> in case of a request code, <code>false</code> otherwise.
     *
     */
    public static boolean isRequest(MessageCode.Name messageCode){
        return isRequest(messageCode.getNumber());
    }

    /**
     * This method indicates whether the given number refers to a {@link MessageCode} for {@link CoapResponse}s.
     *
     * <b>Note:</b> Messages with {@link MessageCode.Name#EMPTY} are considered neither a response nor a request
     *
     * @return <code>true</code> in case of a response code, <code>false</code> otherwise.
     *
     */
    public static boolean isResponse(int codeNumber){
        return codeNumber >= 5;
    }

    /**
     * This method indicates whether the given {@link MessageCode.Name} indicates a {@link CoapResponse}.
     *
     * <b>Note:</b> Messages with {@link MessageCode.Name#EMPTY} are considered neither a response nor a request
     *
     * @return <code>true</code> in case of a response code, <code>false</code> otherwise.
     *
     */
    public static boolean isResponse(MessageCode.Name messageCode){
        return isResponse(messageCode.getNumber());
    }

    /**
     * This method indicates whether the given number refers to a {@link MessageCode} for {@link CoapResponse}s
     * indicating an error.
     *
     * @return <code>true</code> in case of an error response code, <code>false</code> otherwise.
     *
     */
    public static boolean isErrorMessage(int codeNumber){
        return (codeNumber >= 128);
    }

    /**
     * This method indicates whether a message may contain payload
     * @return <code>true</code> if payload is allowed, <code>false</code> otherwise
     */
    public static boolean allowsContent(int codeNumber){
        return !(codeNumber == Name.GET.getNumber() || codeNumber == Name.DELETE.getNumber());
    }


}

