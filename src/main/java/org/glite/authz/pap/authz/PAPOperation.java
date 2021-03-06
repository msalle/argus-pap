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

import java.util.Map;

/** An authorized trusted PAP management operation. **/
public interface PAPOperation<T> {

    /**
     * Executes the PAP mamangement operation 
     * @return T, the return value of the operation
     */
    public T execute();

    /**
     * Returns the required permissions to execute this operation
     * @return a map with the required permissions for this operation
     */
    Map <PAPContext, PAPPermission> getRequiredPermission();

}
