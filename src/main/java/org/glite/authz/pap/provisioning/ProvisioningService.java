/**
 *
 * Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)
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
 *
 * File : ProvisioningService.java
 *
 * Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>
 * 
 */

package org.glite.authz.pap.provisioning;

import org.glite.authz.pap.common.xacml.PolicySet;
import org.glite.authz.pap.repository.dao.DAOFactory;
import org.glite.authz.pap.repository.dao.RootPolicySetDAO;
import org.opensaml.saml2.core.Response;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.profile.saml.XACMLPolicyQueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class ProvisioningService {

  final Logger logger = LoggerFactory.getLogger( ProvisioningService.class );

  public Response XACMLPolicyQuery( XACMLPolicyQueryType query )
      throws java.rmi.RemoteException {

    if ( logger.isDebugEnabled() ) {
      logger.debug( ProvisioningServiceUtils.xmlObjectToString( query ) );
    }

    /* check a few things about the query */

    try {
      ProvisioningServiceUtils.checkQuery( query );
    } 
    catch ( VersionMismatchException e ) {
      return ProvisioningServiceUtils.createResponse( query , e );
    } 
    catch ( MissingIssuerException e ) {
      return ProvisioningServiceUtils.createResponse( query , e );
    } 
    catch ( WrongFormatIssuerException e ) {
      return ProvisioningServiceUtils.createResponse( query , e );
    }

    /* get a DAO */
    
    DAOFactory daoFactory = DAOFactory.getDAOFactory(); 
    
    RootPolicySetDAO rootPolicySetDAO = daoFactory.getRootPolicySetDAO();
    
    PolicySet policySet = rootPolicySetDAO.get();
    
    Element policySetElement = (Element) policySet.getDOM();
    
    /* convert the policy set element to an OpenSAML object */

    PolicySetType resultPolicySet = 
      ProvisioningServiceUtils.createPolicySet( policySetElement );

    /* prepare the response */

    Response response = 
      ProvisioningServiceUtils.createResponse( query , resultPolicySet );

    if ( logger.isDebugEnabled() ) {
      logger.debug( ProvisioningServiceUtils.xmlObjectToString( response ) );
    }

    return response;
  }

}
