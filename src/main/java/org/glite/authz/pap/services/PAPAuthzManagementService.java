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

package org.glite.authz.pap.services;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.glite.authz.pap.authz.ACL;
import org.glite.authz.pap.authz.AuthorizationEngine;
import org.glite.authz.pap.authz.PAPAdmin;
import org.glite.authz.pap.authz.PAPAdminFactory;
import org.glite.authz.pap.authz.PAPContext;
import org.glite.authz.pap.authz.PAPPermission;
import org.glite.authz.pap.authz.VOMSFQAN;
import org.glite.authz.pap.authz.X509Principal;
import org.glite.authz.pap.authz.exceptions.PAPAuthzException;
import org.glite.authz.pap.authz.operations.authzmanagement.AddACEOperation;
import org.glite.authz.pap.authz.operations.authzmanagement.GetACLOperation;
import org.glite.authz.pap.authz.operations.authzmanagement.RemoveACEOperation;
import org.glite.authz.pap.authz.operations.authzmanagement.SetACLOperation;
import org.glite.authz.pap.common.utils.PathNamingScheme;
import org.glite.authz.pap.services.authz_management.axis_skeletons.PAPACE;
import org.glite.authz.pap.services.authz_management.axis_skeletons.PAPAuthorizationManagement;
import org.glite.authz.pap.services.authz_management.axis_skeletons.PAPException;
import org.glite.authz.pap.services.authz_management.axis_skeletons.PAPPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PAPAuthzManagementService implements PAPAuthorizationManagement{

	public static final Logger log = LoggerFactory
			.getLogger(PAPAuthzManagementService.class);

	protected PAPAdmin principalToAdmin(PAPPrincipal principal) {

		if (principal.getType().equals("x509-dn")) {

		    return PAPAdminFactory.getDn( principal.getName() );

		} else if (principal.getType().equals("voms-fqan")) {
		    
		    return PAPAdminFactory.getFQAN( principal.getName() );
		    
		} else
			throw new PAPAuthzException("Unsupported principal type '"
					+ principal.getType() + "'.");

	}

	protected void checkPAPPrincipal(PAPPrincipal principal) {

		if (principal == null)
			throw new PAPAuthzException("Null principal passed as argument!");

		if (principal.getType().equals("x509-dn")) {

			if (principal.getName() == null || principal.getName().equals(""))
				throw new PAPAuthzException(
						"X509 Principal with NULL or empty DN passed as argument!");

		} else if (principal.getType().equals("voms-fqan")) {

			PathNamingScheme.checkSyntax(principal.getName());
		}
	}



	public void addACE(String context, PAPPrincipal principal,
			String[] permissions) throws RemoteException, PAPException {

	    
	    log.info( "addACE('{}','{}','{}')", new Object[]{context,
	            principal.getName(),permissions});
	    
		PAPContext papContext = null;

		checkPAPPrincipal(principal);

		PAPAdmin admin = principalToAdmin(principal);

		if (permissions == null || permissions.length == 0)
			throw new PAPAuthzException(
					"Cannot set NULL permissions for principal '" + admin
							+ "'.");

		if (context == null || context.equals("")
				|| context.equals("global-context"))
			papContext = AuthorizationEngine.instance().getGlobalContext();
		else
			throw new PAPAuthzException(
					"Only the context 'global-context' is currently supported!");

		PAPPermission perms = PAPPermission.fromStringArray(permissions);

		AddACEOperation.instance(org.glite.authz.pap.authz.PAPACE.instance(papContext, admin, perms))
				.execute();
		
		AuthorizationEngine.instance().saveConfiguration();

	}

	public void removeACE(String context, PAPPrincipal principal)
			throws RemoteException, PAPException {

	    log.info( "removeACE('{}','{}')",context,principal.getName());
	    
		PAPContext papContext = null;

		checkPAPPrincipal(principal);

		PAPAdmin admin = principalToAdmin(principal);

		if (context == null || context.equals("")
				|| context.equals("global-context"))
			papContext = AuthorizationEngine.instance().getGlobalContext();
		else
			throw new PAPAuthzException(
					"Only the context 'global-context' is currently supported!");

		RemoveACEOperation.instance(org.glite.authz.pap.authz.PAPACE.instance(papContext, admin))
				.execute();
		
		AuthorizationEngine.instance().saveConfiguration();

	}

    public void setACL(
            String context,
            org.glite.authz.pap.services.authz_management.axis_skeletons.PAPACE[] acl )
            throws RemoteException , PAPException {
        
        log
                .info( "setACL("
                        + StringUtils.join( new Object[] { context, acl }, ',' )
                        + ");" );
        
        PAPContext papContext;
        
        if (context == null || context.equals("")
                || context.equals("global-context"))
            papContext = AuthorizationEngine.instance().getGlobalContext();
        else
            throw new PAPAuthzException(
                    "Only the context 'global-context' is currently supported!");
        
        ACL globalACL = papContext.getAcl();
        
        SetACLOperation.instance( context, convertACL( acl )).execute();
        
        AuthorizationEngine.instance().saveConfiguration();
        return;

                
    }

    public org.glite.authz.pap.services.authz_management.axis_skeletons.PAPACE[] getACL(
            String context ) throws RemoteException , PAPException {

        log.info( "getACL('{}')", context);
        
        PAPContext papContext;
        
        if (context == null || context.equals("")
                || context.equals("global-context"))
            papContext = AuthorizationEngine.instance().getGlobalContext();
        else
            throw new PAPAuthzException(
                    "Only the context 'global-context' is currently supported!");
        
        
        Map<PAPAdmin, PAPPermission> permissions = GetACLOperation.instance( context ).execute();
        int permsSize = permissions.size(); 
        
        if ( permsSize == 0)
            return null;
        
        PAPACE[] entries = new PAPACE[permsSize];
        
        int i=0;
        
        for (Map.Entry <PAPAdmin, PAPPermission> entry: permissions.entrySet()){
            
            PAPACE wsEntry = new PAPACE();
            wsEntry.setPrincipal( adminToPrincipal( entry.getKey() ) );
            wsEntry.setPermissions( entry.getValue().toStringArray() );
            entries[i++] = wsEntry;
            
        }
        
        return entries;
        
    }
    
    
    protected PAPACE convertACE(org.glite.authz.pap.authz.PAPACE ace){
        
        PAPACE wsACE = new PAPACE();
        
        wsACE.setPrincipal( adminToPrincipal( ace.getAdmin() ) );
        if (ace.getPerms() == null)
            wsACE.setPermissions( null );
        else
            wsACE.setPermissions( ace.getPerms().toStringArray() );
        
        return wsACE;
        
    }
    protected PAPPrincipal adminToPrincipal(PAPAdmin admin){
        
        PAPPrincipal principal = new PAPPrincipal();
        
        if (admin instanceof X509Principal){
        
            principal.setType( "x509-dn" );
            principal.setName( admin.getName() );
        
        } else if (admin instanceof VOMSFQAN){
            
            principal.setType( "voms-fqan" );
            principal.setName( admin.getName());
            
        }
        
        return principal;
    }
    
    protected Map<PAPAdmin, PAPPermission> convertACL(PAPACE[] entries){
        
        HashMap <PAPAdmin, PAPPermission> permissions = new HashMap <PAPAdmin, PAPPermission>(entries.length);
        
        for (PAPACE entry: entries)
            permissions.put( principalToAdmin( entry.getPrincipal()), PAPPermission.fromStringArray( entry.getPermissions() ));
        
        return permissions;
    }
        
        

}
