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

package org.glite.authz.pap.authz;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This class represents an Access Control List for the PAP authorization engine.
 * PAP ACLs assign permissions (ie, {@link PAPPermission} objects) to PAP administrators
 * (ie, {@link PAPAdmin} objects).
 * 
 * @author andrea
 *
 */
public class ACL {

    /**
     * The permission map.
     */
    Map <PAPAdmin, PAPPermission> permissions;

    /**
     * Constructor.
     */
    public ACL() {

        permissions = new HashMap <PAPAdmin, PAPPermission>();

    }

    /**
     * Returns the permission map for this ACL
     * 
     * @return the permission map
     */
    public Map <PAPAdmin, PAPPermission> getPermissions() {

        return permissions;
    }

    /**
     * Sets the permission map for this ACL
     * 
     * @param permissions, the permission map
     */
    public void setPermissions( Map <PAPAdmin, PAPPermission> permissions ) {

        this.permissions = permissions;
    }

    /**
     * Assigns permission to a given administator in this ACL
     * @param a, a {@link PAPAdmin} administrator 
     * @param p, a {@link PAPPermission} permission
     */
    public void setPermissions( PAPAdmin a, PAPPermission p ) {

        permissions.put( a, p );
    }

    /**
     * Removes all permisssions assigned to an admin in this ACL
     * @param a, the {@link PAPAdmin} for whom the permissions will be removed
     */
    public void removePermissions( PAPAdmin a ) {

        permissions.remove( a );
    }

    /**
     * Returns the permissions assigned to any authenticated user admin.
     * @see PAPAdminFactory#getAnyAuthenticatedUserAdmin()
     * 
     * @return the permissions for any authenticated user
     */
    public PAPPermission getAnyAuthenticatedUserPermissions() {

        return permissions.get( PAPAdminFactory.getAnyAuthenticatedUserAdmin() );
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {

        return "\n"
                + StringUtils.join( permissions.entrySet().iterator(), "\n" );

    }
}
