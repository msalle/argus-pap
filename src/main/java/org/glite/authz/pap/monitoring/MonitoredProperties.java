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

package org.glite.authz.pap.monitoring;

import java.util.Map;
import java.util.TreeMap;

import org.glite.authz.pap.common.PAPConfiguration;
import org.glite.authz.pap.common.Version;

/**
 * 
 * Creates a runtime snapshot of pap monitored properties.
 *
 */
public class MonitoredProperties {

    /**
     * Number of policies property name
     */
    public static final String NUM_OF_POLICIES_PROP_NAME = "NumberOfPolicies";
    /**
     * Number of local policies property name
     */
    public static final String NUM_OF_LOCAL_POLICIES_PROP_NAME = "NumberOfLocalPolicies";
    
    /**
     * Number of remote policies property name
     */
    public static final String NUM_OF_REMOTE_POLICIES_PROP_NAME = "NumberOfRemotePolicies";
    /**
     * Policy last modification time property name
     */
    public static final String POLICY_LAST_MODIFICATION_TIME_PROP_NAME = "PolicyLastModificationTime";
    
    /**
     * Policy last modification time millis property name
     */
    public static final String POLICY_LAST_MODIFICATION_TIME_MILLIS_PROP_NAME = "PolicyLastModificationTimeMillis";
    
    /**
     * Service startup time property name
     */
    public static final String SERVICE_STARTUP_TIME_PROP_NAME = "ServiceStartupTime";
    
    /**
     * Service startup time millis property name
     */
    public static final String SERVICE_STARTUP_TIME_MILLIS_PROP_NAME = "ServiceStartupTimeMillis";
    
    /**
     * Status property name
     */
    public static final String STATUS_PROP_NAME = "Status";
    /**
     * Used memory property name
     */
    public static final String USED_MEMORY_PROP_NAME = "UsedMemory";
    /**
     * Max memory property name
     */
    public static final String MAX_MEMORY_PROP_NAME = "MaxMemory";
    
    /**
     * Pap version property name
     */
    public static final String SERVICE_VERSION_PROP_NAME = "ServiceVersion";
    
    /**
     * Service name property name
     */
    public static final String SERVICE_NAME_PROP_NAME = "Service";
    
    
    private static final String[] propertyOrdering = {
    		STATUS_PROP_NAME,
    		SERVICE_NAME_PROP_NAME,
    		SERVICE_VERSION_PROP_NAME,
    		SERVICE_STARTUP_TIME_PROP_NAME,
    		SERVICE_STARTUP_TIME_MILLIS_PROP_NAME,
    		MAX_MEMORY_PROP_NAME,
    		USED_MEMORY_PROP_NAME,
    		POLICY_LAST_MODIFICATION_TIME_PROP_NAME,
    		POLICY_LAST_MODIFICATION_TIME_MILLIS_PROP_NAME,
    		NUM_OF_POLICIES_PROP_NAME,
    		NUM_OF_LOCAL_POLICIES_PROP_NAME,
    		NUM_OF_REMOTE_POLICIES_PROP_NAME
    };
    

    /**
     * The map that holds the monitored properties
     */
    TreeMap<String, String> props;

    
    /**
     * Constructor
     */
    private MonitoredProperties() {

        props = new TreeMap<String, String>();

        PAPConfiguration conf = PAPConfiguration.instance();
        
        props.put(STATUS_PROP_NAME, "OK");
        props.put(SERVICE_NAME_PROP_NAME, "Argus PAP");
        props.put(SERVICE_VERSION_PROP_NAME, Version.getServiceVersion() );
        props.put(SERVICE_STARTUP_TIME_PROP_NAME, conf.getMonitoringProperty(SERVICE_STARTUP_TIME_PROP_NAME).toString());
        props.put(SERVICE_STARTUP_TIME_MILLIS_PROP_NAME, conf.getMonitoringProperty(SERVICE_STARTUP_TIME_MILLIS_PROP_NAME).toString());
        
        props.put(NUM_OF_POLICIES_PROP_NAME, conf.getMonitoringProperty(NUM_OF_POLICIES_PROP_NAME).toString());
        props.put(NUM_OF_LOCAL_POLICIES_PROP_NAME, conf.getMonitoringProperty(NUM_OF_LOCAL_POLICIES_PROP_NAME).toString());
        props.put(NUM_OF_REMOTE_POLICIES_PROP_NAME, conf.getMonitoringProperty(NUM_OF_REMOTE_POLICIES_PROP_NAME).toString());
        props.put(POLICY_LAST_MODIFICATION_TIME_PROP_NAME, conf.getMonitoringProperty(POLICY_LAST_MODIFICATION_TIME_PROP_NAME)
                .toString());
        
        props.put(POLICY_LAST_MODIFICATION_TIME_MILLIS_PROP_NAME, conf.getMonitoringProperty(POLICY_LAST_MODIFICATION_TIME_MILLIS_PROP_NAME)
                .toString());

        Runtime r = Runtime.getRuntime();
        props.put(USED_MEMORY_PROP_NAME, "" + r.totalMemory() + " bytes");
        props.put(MAX_MEMORY_PROP_NAME, "" + r.maxMemory() + " bytes");

    }

    /**
     * 
     * @return an updated instance of {@link MonitoredProperties}
     */
    public static MonitoredProperties instance() {

        return new MonitoredProperties();
    }

    private String propertyString(String propName){
    	
    	return propName + ": "+props.get(propName);
    }
    
    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        
        for (String s: propertyOrdering)
        	builder.append(propertyString(s)+"\n");
        
        return builder.toString();
    }

}
