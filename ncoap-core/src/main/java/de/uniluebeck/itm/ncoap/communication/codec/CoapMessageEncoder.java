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
* All rights reserved
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
* following conditions are met:
*
*  - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
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

package de.uniluebeck.itm.ncoap.communication.codec;

import com.google.common.primitives.Ints;
import de.uniluebeck.itm.ncoap.application.client.Token;
import de.uniluebeck.itm.ncoap.message.CoapMessage;
import de.uniluebeck.itm.ncoap.message.MessageCode;
import de.uniluebeck.itm.ncoap.message.options.OptionValue;
import de.uniluebeck.itm.ncoap.application.client.CoapResponseProcessor;
import de.uniluebeck.itm.ncoap.application.client.CoapClientApplication;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * A {@link CoapMessageEncoder} serializes outgoing {@link CoapMessage}s. In the (rather unlikely) case that there is
 * an exception thrown during the encoding process, an internal message is sent upstream, i.e. in the direction of
 * the application.
 *
 * <b>Note for instances of {@link CoapClientApplication}:</b>Implement {@link EncodingFailedProcessor} within your
 * {@link CoapResponseProcessor} instance (which was supposed to handle an awaited response) to get your application
 * informed about such an error.
 *
 * @author Oliver Kleine
 */
public class CoapMessageEncoder extends SimpleChannelDownstreamHandler {

    /**
     * The maximum option delta (65804)
     */
    public static final int MAX_OPTION_DELTA = 65804;

    /**
     * The maximum option length (65804)
     */
    public static final int MAX_OPTION_LENGTH = 65804;


    private Logger log = LoggerFactory.getLogger(this.getClass().getName());


    @Override
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent evt) throws Exception {

        if (!(evt instanceof MessageEvent) || !(((MessageEvent) evt).getMessage() instanceof CoapMessage)) {
            ctx.sendDownstream(evt);
            return;
        }

        InetSocketAddress remoteEndpoint = (InetSocketAddress) ((MessageEvent) evt).getRemoteAddress();
        CoapMessage coapMessage = (CoapMessage) ((MessageEvent) evt).getMessage();

        try{
            Channels.write(ctx, evt.getFuture(), encode(coapMessage), remoteEndpoint);
        }
        catch(Exception ex){
            evt.getFuture().setFailure(ex);
            sendInternalEncodingFailedMessage(ctx, remoteEndpoint, coapMessage.getMessageID(), coapMessage.getToken(),
                    ex);
        }
    }


    protected ChannelBuffer encode(CoapMessage coapMessage) throws InvalidOptionException {
        log.info("CoapMessage to be encoded: {}", coapMessage);


        //Start encoding
        ChannelBuffer encodedMessage = ChannelBuffers.dynamicBuffer(0);

        //Encode HEADER and TOKEN
        encodeHeader(encodedMessage, coapMessage);
        log.debug("Encoded length of message (after HEADER + TOKEN): {}", encodedMessage.readableBytes());

        if(coapMessage.getMessageCodeName() == MessageCode.Name.EMPTY){
            encodedMessage = ChannelBuffers.wrappedBuffer(Ints.toByteArray(encodedMessage.getInt(0) & 0xF0FFFFFF));
            return encodedMessage;
        }


        if(coapMessage.getAllOptions().size() == 0 && coapMessage.getContent().readableBytes() == 0)
            return encodedMessage;


        encodeOptions(encodedMessage, coapMessage);

        log.debug("Encoded length of message (after OPTIONS): {}", encodedMessage.readableBytes());

        if(coapMessage.getContent().readableBytes() > 0){
            //Add END-OF-OPTIONS marker only if there is payload
            encodedMessage.writeByte(255);

            //Add CONTENT
            encodedMessage = ChannelBuffers.wrappedBuffer(encodedMessage, coapMessage.getContent());
            log.debug("Encoded length of message (after CONTENT): {}", encodedMessage.readableBytes());
        }

        return encodedMessage;
    }


    protected void encodeHeader(ChannelBuffer buffer, CoapMessage coapMessage){

        byte[] token = coapMessage.getToken().getBytes();

        int encodedHeader = ((coapMessage.getProtocolVersion()  & 0x03)     << 30)
                          | ((coapMessage.getMessageType()      & 0x03)     << 28)
                          | ((token.length                      & 0x0F)     << 24)
                          | ((coapMessage.getMessageCode()      & 0xFF)     << 16)
                          | ((coapMessage.getMessageID()        & 0xFFFF));

        buffer.writeInt(encodedHeader);

        if(log.isDebugEnabled()){
            String binary = Integer.toBinaryString(encodedHeader);
            while(binary.length() < 32){
                binary = "0" + binary;
            }
            log.debug("Encoded Header: {}", binary);
        }

        //Write token
        if(token.length > 0)
            buffer.writeBytes(token);

    }


    protected void encodeOptions(ChannelBuffer buffer, CoapMessage coapMessage) throws InvalidOptionException {

        //Encode options one after the other and append buf option to the buf
        int previousOptionNumber = 0;

        for(int optionNumber : coapMessage.getAllOptions().keySet()){
            for(OptionValue optionValue : coapMessage.getOptions(optionNumber)){
                encodeOption(buffer, optionNumber, optionValue, previousOptionNumber);
                previousOptionNumber = optionNumber;
            }
        }
    }


    protected void encodeOption(ChannelBuffer buffer, int optionNumber, OptionValue optionValue, int prevNumber)
            throws InvalidOptionException {

        //The previous option number must be smaller or equal to the actual one
        if(prevNumber > optionNumber){
            log.error("Previous option no. ({}) must not be larger then current option no ({})",
                    prevNumber, optionNumber);

            throw new InvalidOptionException(optionNumber);
        }


        int optionDelta = optionNumber - prevNumber;
        int optionLength = optionValue.getValue().length;

        if(optionLength > MAX_OPTION_LENGTH){
            log.error("Option no. {} exceeds maximum option length (actual: {}, max: {}).",
                    new Object[]{optionNumber, optionLength, MAX_OPTION_LENGTH});

            throw new InvalidOptionException(optionNumber);
        }


        if(optionDelta > MAX_OPTION_DELTA){
            log.error("Option delta exceeds maximum option delta (actual: {}, max: {})", optionDelta, MAX_OPTION_DELTA);
            throw new InvalidOptionException(optionNumber);
        }


        //option delta < 13
        if(optionDelta < 13){

            if(optionLength < 13){
                buffer.writeByte(((optionDelta & 0xFF) << 4) | (optionLength & 0xFF));
            }

            else if (optionLength < 269){
                buffer.writeByte(((optionDelta << 4) & 0xFF) | (13 & 0xFF));
                buffer.writeByte((optionLength - 13) & 0xFF);
            }

            else{
                buffer.writeByte(((optionDelta << 4) & 0xFF) | (14 & 0xFF));
                buffer.writeByte(((optionLength - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionLength - 269) & 0xFF);
            }
        }

        //13 <= option delta < 269
        else if(optionDelta < 269){

            if(optionLength < 13){
                buffer.writeByte(((13 & 0xFF) << 4) | (optionLength & 0xFF));
                buffer.writeByte((optionDelta - 13) & 0xFF);
            }

            else if (optionLength < 269){
                buffer.writeByte(((13 & 0xFF) << 4) | (13 & 0xFF));
                buffer.writeByte((optionDelta - 13) & 0xFF);
                buffer.writeByte((optionLength - 13) & 0xFF);
            }

            else{
                buffer.writeByte((13 & 0xFF) << 4 | (14 & 0xFF));
                buffer.writeByte((optionDelta - 13) & 0xFF);
                buffer.writeByte(((optionLength - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionLength - 269) & 0xFF);
            }
        }

        //269 <= option delta < 65805
        else{

            if(optionLength < 13){
                buffer.writeByte(((14 & 0xFF) << 4) | (optionLength & 0xFF));
                buffer.writeByte(((optionDelta - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionDelta - 269) & 0xFF);
            }

            else if (optionLength < 269){
                buffer.writeByte(((14 & 0xFF) << 4) | (13 & 0xFF));
                buffer.writeByte(((optionDelta - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionDelta - 269) & 0xFF);
                buffer.writeByte((optionLength - 13) & 0xFF);
            }

            else{
                buffer.writeByte(((14 & 0xFF) << 4) | (14 & 0xFF));
                buffer.writeByte(((optionDelta - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionDelta - 269) & 0xFF);
                buffer.writeByte(((optionLength - 269) & 0xFF00) >>> 8);
                buffer.writeByte((optionLength - 269) & 0xFF);
            }
        }

        //Write option value
        buffer.writeBytes(optionValue.getValue());
        log.debug("Encoded option no {} with value {}", optionNumber, optionValue.getDecodedValue());
        log.debug("Encoded message length is now: {}", buffer.readableBytes());
    }


    private void sendInternalEncodingFailedMessage(ChannelHandlerContext ctx, InetSocketAddress remoteEndpoint,
                                                   int messageID, Token token, Throwable cause){

        InternalEncodingFailedMessage internalMessage =
                new InternalEncodingFailedMessage(remoteEndpoint, messageID, token, cause);

        Channels.fireMessageReceived(ctx, internalMessage);
    }
}
