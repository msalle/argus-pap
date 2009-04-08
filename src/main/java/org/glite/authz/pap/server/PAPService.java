package org.glite.authz.pap.server;

import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.glite.authz.pap.authz.AuthorizationEngine;
import org.glite.authz.pap.common.Pap;
import org.glite.authz.pap.common.PAPConfiguration;
import org.glite.authz.pap.common.PAPVersion;
import org.glite.authz.pap.common.exceptions.PAPConfigurationException;
import org.glite.authz.pap.common.xacml.wizard.AttributeWizardTypeConfiguration;
import org.glite.authz.pap.distribution.DistributionModule;
import org.glite.authz.pap.monitoring.MonitoredProperties;
import org.glite.authz.pap.papmanagement.PapContainer;
import org.glite.authz.pap.papmanagement.PapManager;
import org.glite.authz.pap.repository.RepositoryManager;
import org.glite.authz.pap.repository.RepositoryUtils;
import org.glite.authz.pap.repository.exceptions.RepositoryException;
import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;
import org.slf4j.Logger;

public final class PAPService {

    static final Logger logger = org.slf4j.LoggerFactory.getLogger(PAPService.class);

    protected static void setStartupMonitoringProperties() {

        // TODO: find a more reliable naming scheme

        // Property: service startup time
        PAPConfiguration.instance()
                        .setMonitoringProperty(MonitoredProperties.SERVICE_STARTUP_TIME_PROP_NAME,
                                               ((new GregorianCalendar()).getTimeInMillis() / 1000));

        PapManager papManager = PapManager.getInstance();

        // Property: number of local policies
        int numberOfLocalPolicies = papManager.getPapContainer(Pap.DEFAULT_PAP_ALIAS).getNumberOfPolicies();
        PAPConfiguration.instance().setMonitoringProperty(MonitoredProperties.NUM_OF_LOCAL_POLICIES_PROP_NAME,
                numberOfLocalPolicies);

        // Property: number of remote policies
        int numOfRemotePolicies = 0;
        for (PapContainer papContainer : PapContainer.getContainers(papManager.getRemotePaps())) {
            numOfRemotePolicies += papContainer.getNumberOfPolicies();
        }
        PAPConfiguration.instance().setMonitoringProperty(MonitoredProperties.NUM_OF_REMOTE_POLICIES_PROP_NAME,
                numOfRemotePolicies);

        // Property: number of policies
        PAPConfiguration.instance().setMonitoringProperty(MonitoredProperties.NUM_OF_POLICIES_PROP_NAME,
                numberOfLocalPolicies + numOfRemotePolicies);

        // Property: policy last modification time
        String policyLastModificationTimeString = papManager.getPap(Pap.DEFAULT_PAP_ALIAS).getPolicyLastModificationTimeInSecondsString();
        PAPConfiguration.instance().setMonitoringProperty(MonitoredProperties.POLICY_LAST_MODIFICATION_TIME_PROP_NAME,
                policyLastModificationTimeString);
    }

    public static void start(ServletContext context) {

        logger.info("Starting PAP service version {} ...", PAPVersion.instance().getVersion());

        // Initialize configuaration
        PAPConfiguration conf = PAPConfiguration.initialize(context);
        
        // Start autorization service
        logger.info("Starting authorization engine...");

        AuthorizationEngine.initialize(conf.getPapAuthzConfigurationFileName());

        // Bootstrap opensaml
        try {

            logger.info("Bootstraping OpenSAML...");
            DefaultBootstrap.bootstrap();

        } catch (ConfigurationException e) {

            logger.error("Error configuring OpenSAML:" + e.getMessage());
            throw new PAPConfigurationException("Error configuring OpenSAML:" + e.getMessage(), e);
        }
        
        // Boostrap wizard attributes
        AttributeWizardTypeConfiguration.bootstrap();

        // Start repository manager
        logger.info("Starting repository manager...");
        RepositoryManager.bootstrap();
        
        PapManager.initialize();
        
        boolean performRepositoryValidationCheck;

        try {
            
            performRepositoryValidationCheck = PAPConfiguration.instance().getBoolean("repository.consistency_check");
            
        } catch (NoSuchElementException e) {
            
            performRepositoryValidationCheck = false;
            logger.info("Skipping repository validation check");
            
        }

        if (performRepositoryValidationCheck) {
            
            boolean repair;
            
            try {
            
                repair = PAPConfiguration.instance().getBoolean("repository.consistency_check.repair");
            
            } catch (NoSuchElementException e) {
            
                repair = false;
            }
            
            logger.info("Starting repository validation. repair=" + repair);
            
            if (RepositoryUtils.performAllChecks(repair) == false) {
            
                throw new RepositoryException("Repository validation check failed");
                
            }
        }
        
        setStartupMonitoringProperties();

        logger.info("Starting pap distribution module...");
        DistributionModule.getInstance().startDistributionModule();

    }

    public static void stop() {

        logger.info("Shutting down PAP service...");

        logger.info("Shutting down distribution module...");
        DistributionModule.getInstance().stopDistributionModule();

        logger.info("Shutting down authorization module...");
        if (AuthorizationEngine.instance() != null)
            AuthorizationEngine.instance().shutdown();

    }

}
