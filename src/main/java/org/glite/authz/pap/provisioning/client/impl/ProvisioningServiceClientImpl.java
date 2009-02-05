/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 File : ProvisioningClient.java

 Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>

 **************************************************************************/

package org.glite.authz.pap.provisioning.client.impl;

import org.glite.authz.pap.provisioning.client.ProvisioningServiceClient;
import org.glite.authz.pap.provisioning.client.ProvisioningServicePortType;

public class ProvisioningServiceClientImpl implements ProvisioningServiceClient {

    public ProvisioningServicePortType getProvisioningServicePortType(String url) {
	return new ProvisioningServicePortTypeImpl(url);
    }

}