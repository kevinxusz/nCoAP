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
package de.uniluebeck.itm.ncoap.application.server.webservice.linkformat;

/**
 * A {@link StringLinkAttribute} is a {@link LinkAttribute} with values of type string
 * ({@link LinkAttribute#STRING_ATTRIBUTE}).
 *
 * @author Oliver Kleine
 */
public class StringLinkAttribute extends LinkAttribute<String> {

    public StringLinkAttribute(String key, String value) {
        super(key, value);

        if(LinkAttribute.getAttributeType(key) != LinkAttribute.STRING_ATTRIBUTE)
            throw new IllegalArgumentException("Attribute \"" + key + "\" must not have string values");
    }

    @Override
    public int hashCode() {
        return getKey().hashCode() | getValue().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof StringLinkAttribute))
            return false;

        StringLinkAttribute other = (StringLinkAttribute) object;

        return this.getKey().equals(other.getKey()) && this.getValue().equals(other.getValue());
    }


}
