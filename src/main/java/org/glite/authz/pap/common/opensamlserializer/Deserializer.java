/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.glite.authz.pap.common.opensamlserializer;

import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.MessageElement;
import org.glite.authz.pap.common.xacml.utils.XMLObjectHelper;
import org.opensaml.xml.XMLObject;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 * 
 */
public class Deserializer extends DeserializerImpl {

	private static final long serialVersionUID = 8668604610394087233L;

	public void onStartElement(java.lang.String namespace, java.lang.String localName,
			java.lang.String prefix, org.xml.sax.Attributes attributes, DeserializationContext context)
			throws SAXException {

		try {

				MessageElement messageElement = context.getCurElement();
				Element element = messageElement.getAsDOM();

				XMLObject xmlObject = XMLObjectHelper.axisUnmarshall(element);

				setValue(xmlObject);
		}

		catch (Exception exception) {

			throw new SAXException("Error deserializing " + " : " + exception.getClass() + " : "
					+ exception.getMessage());

		}

	}

}
