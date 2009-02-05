package org.glite.authz.pap.services;

import java.rmi.RemoteException;

import org.glite.authz.pap.authz.policymanagement.GetPolicyOperation;
import org.glite.authz.pap.authz.policymanagement.GetPolicySetOperation;
import org.glite.authz.pap.authz.policymanagement.HasPolicyOperation;
import org.glite.authz.pap.authz.policymanagement.HasPolicySetOperation;
import org.glite.authz.pap.authz.policymanagement.ListPoliciesForPAPOperation;
import org.glite.authz.pap.authz.policymanagement.ListPoliciesOperation;
import org.glite.authz.pap.authz.policymanagement.ListPolicySetOperation;
import org.glite.authz.pap.authz.policymanagement.ListPolicySetsForPAPOperation;
import org.glite.authz.pap.authz.policymanagement.RemovePolicyOperation;
import org.glite.authz.pap.authz.policymanagement.RemovePolicySetOperation;
import org.glite.authz.pap.authz.policymanagement.StorePolicyOperation;
import org.glite.authz.pap.authz.policymanagement.StorePolicySetOperation;
import org.glite.authz.pap.authz.policymanagement.UpdatePolicyOperation;
import org.glite.authz.pap.authz.policymanagement.UpdatePolicySetOperation;
import org.glite.authz.pap.distribution.PAPManager;
import org.glite.authz.pap.repository.PAPContainer;
import org.glite.authz.pap.services.xacml_policy_management.axis_skeletons.XACMLPolicyManagement;
import org.glite.authz.pap.ui.wizard.PolicyWizard;
import org.opensaml.xacml.policy.PolicySetType;
import org.opensaml.xacml.policy.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XACMLPolicyManagementService implements XACMLPolicyManagement {

    private static final Logger log = LoggerFactory.getLogger(XACMLPolicyManagementService.class);

    public String addPolicy(String policySetId, String policyIdPrefix, PolicyType policy) throws RemoteException {
        log.info("addPolicy();");

        String policyId = null;

        try {

            String emptyString = "";

            PAPContainer localPAP = PAPManager.getInstance().getLocalPAPContainer();

            if (!localPAP.hasPolicySet(policySetId))
                return emptyString;

            policyId = PolicyWizard.generateId(policyIdPrefix);
            policy.setPolicyId(policyId);

            localPAP.addPolicy(policySetId, policy);

        } catch (RuntimeException e) {
            ServiceClassExceptionManager.logAndThrow(log, e);
        }

        return policyId;
    }

    public PolicyType getPAPPolicy(String papAlias, String policyId) throws RemoteException {
        log.info("getPAPPolicy(\"" + papAlias + "\", \"" + policyId + "\");");
        
        PAPContainer pap = PAPManager.getInstance().getTrustedPAPContainer(papAlias);
        
        PolicyType policy = pap.getPolicy(policyId);
        
        return policy;
    }

    public PolicySetType getPAPPolicySet(String papAlias, String policySetId) throws RemoteException {
        log.info("getPAPPolicySet(\"" + papAlias + "\", \"" + policySetId + "\");");
        
        PAPContainer pap = PAPManager.getInstance().getTrustedPAPContainer(papAlias);
        
        PolicySetType policySet = pap.getPolicySet(policySetId);
        
        return policySet;
    }

    public PolicyType getPolicy(String policyId) throws RemoteException {
        log.info("getPolicy(\"" + policyId + "\");");
        PolicyType policy = GetPolicyOperation.instance(policyId).execute();
        log.info("Policy found!");
        return policy;
    }

    public PolicySetType getPolicySet(String policySetId) throws RemoteException {
        log.info("getPolicySet(\"" + policySetId + "\");");
        PolicySetType policySet = GetPolicySetOperation.instance(policySetId).execute();
        return policySet;
    }

    public boolean hasPolicy(String policyId) throws RemoteException {
        log.info("hasPolicy(\"" + policyId + "\");");
        return HasPolicyOperation.instance(policyId).execute();
    }

    public boolean hasPolicySet(String policySetId) throws RemoteException {
        log.info("hasPolicySet(\"" + policySetId + "\");");
        return HasPolicySetOperation.instance(policySetId).execute();
    }

    public PolicyType[] listPAPPolicies(String papAlias) throws RemoteException {
        log.info("listPolicies(\"" + papAlias + "\");");
        return ListPoliciesForPAPOperation.instance(papAlias).execute();
    }

    public PolicySetType[] listPAPPolicySets(String papAlias) throws RemoteException {
        log.info("listPolicySets(\"" + papAlias + "\");");
        return ListPolicySetsForPAPOperation.instance(papAlias).execute();
    }

    public PolicyType[] listPolicies() throws RemoteException {
        log.info("listPolicies();");
        PolicyType[] policyArray = ListPoliciesOperation.instance().execute();
        log.info("Returning " + policyArray.length + " policies");
        return policyArray;
    }

    public PolicySetType[] listPolicySets() throws RemoteException {
        log.info("listPolicySets();");
        
        PolicySetType[] policySetArray = null;
        
        try {
            policySetArray = ListPolicySetOperation.instance().execute();
        } catch (RuntimeException e) {
            ServiceClassExceptionManager.logAndThrow(log, e);
        }
        
        log.info("Returning " + policySetArray.length + " policy sets");
        
        return policySetArray;
    }

    public boolean removePolicy(String policyId) throws RemoteException {
        log.info("removePolicy(\"" + policyId + "\");");
        return RemovePolicyOperation.instance(policyId).execute();
    }

    public boolean removePolicyAndReferences(String policyId) throws RemoteException {
        log.info("removePolicyAndReferences(\"" + policyId + "\");");

        try {

            PAPContainer localPAP = PAPManager.getInstance().getLocalPAPContainer();

            if (!localPAP.hasPolicy(policyId))
                return false;

            localPAP.removePolicyAndReferences(policyId);

        } catch (RuntimeException e) {
            ServiceClassExceptionManager.logAndThrow(log, e);
        }

        return true;
    }

    public boolean removePolicySet(String policySetId) throws RemoteException {
        log.info("removePolicySet(\"" + policySetId + "\");");
        return RemovePolicySetOperation.instance(policySetId).execute();
    }

    public String storePolicy(String idPrefix, PolicyType policy) throws RemoteException {
        log.info("storePolicy();");
        return StorePolicyOperation.instance(idPrefix, policy).execute();
    }

    public String storePolicySet(String idPrefix, PolicySetType policySet) throws RemoteException {
        log.info("storePolicySet();");
        return StorePolicySetOperation.instance(idPrefix, policySet).execute();
    }

    public boolean updatePolicy(PolicyType policy) throws RemoteException {
        log.info("updatePolicy();");
        return UpdatePolicyOperation.instance(policy).execute();
    }

    public boolean updatePolicySet(PolicySetType policySet) throws RemoteException {
        log.info("updatePolicySet();");
        return UpdatePolicySetOperation.instance(policySet).execute();
    }

}