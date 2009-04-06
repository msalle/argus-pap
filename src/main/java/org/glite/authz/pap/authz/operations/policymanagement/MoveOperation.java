package org.glite.authz.pap.authz.operations.policymanagement;

import org.glite.authz.pap.authz.BasePAPOperation;
import org.glite.authz.pap.authz.PAPPermission;
import org.glite.authz.pap.authz.PAPPermission.PermissionFlags;
import org.glite.authz.pap.common.PAP;
import org.glite.authz.pap.common.xacml.utils.PolicySetHelper;
import org.glite.authz.pap.common.xacml.wizard.PolicySetWizard;
import org.glite.authz.pap.papmanagement.PapContainer;
import org.glite.authz.pap.papmanagement.PapManager;
import org.glite.authz.pap.repository.exceptions.RepositoryException;
import org.glite.authz.pap.services.XACMLPolicyManagementServiceException;
import org.opensaml.xacml.policy.PolicySetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveOperation extends BasePAPOperation<Object> {

    private static final Logger log = LoggerFactory.getLogger(MoveOperation.class);

    String alias;
    String id;
    String pivotId;
    boolean moveAfter;

    protected MoveOperation(String alias, String id, String pivotId, boolean moveAfter) {
        this.alias = alias;
        this.id = id;
        this.pivotId = pivotId;
        this.moveAfter = moveAfter;
    }

    public static MoveOperation instance(String alias, String id, String pivotId, boolean moveAfter) {
        return new MoveOperation(alias, id, pivotId, moveAfter);
    }

    protected Object doExecute() {

        if ((id == null) || (pivotId == null)) {
            return null;
        }

        if (id.equals(pivotId)) {
            return null;
        }

        if (alias == null) {
            alias = PAP.DEFAULT_PAP_ALIAS;
        }
        
        PAP pap = PapManager.getInstance().getPAP(alias);

        if (pap.isRemote()) {
            throw new XACMLPolicyManagementServiceException("Forbidden operation for a remote PAP");
        }

        PapContainer papContainer = new PapContainer(pap);

        if (papContainer.hasPolicySet(id)) {

            movePolicySetId(papContainer);

        } else {

            movePolicyId(papContainer);

        }

        return null;
    }

    private void movePolicySetId(PapContainer papContainer) {

        // now we have only two levels so... all the policy sets (resource <id>) are referenced by the PAP
        // root policy set
        PolicySetType rootPAPPolicySet = papContainer.getPAPRootPolicySet();

        if (!(PolicySetHelper.hasPolicySetReferenceId(rootPAPPolicySet, pivotId))) {
            throw new RepositoryException("Id not found: " + pivotId);
        }

        if (!(PolicySetHelper.deletePolicySetReference(rootPAPPolicySet, id))) {
            throw new RepositoryException(String.format("Id \"%s\" not found into resource \"%s\"",
                                                        id,
                                                        rootPAPPolicySet.getPolicySetId()));
        }

        int pivotIndex = PolicySetHelper.getPolicySetIdReferenceIndex(rootPAPPolicySet, pivotId);

        if (moveAfter) {
            pivotIndex++;
        }
        log.debug("New position for PolicySet is: " + pivotIndex);

        PolicySetHelper.addPolicySetReference(rootPAPPolicySet, pivotIndex, id);

        String version = rootPAPPolicySet.getVersion();

        PolicySetWizard.increaseVersion(rootPAPPolicySet);

        papContainer.updatePolicySet(version, rootPAPPolicySet);
    }

    private void movePolicyId(PapContainer papContainer) {
        PolicySetType targetPolicySet = null;

        // get the target policy set
        for (PolicySetType policySet : papContainer.getAllPolicySets()) {

            PolicySetType tempPolicySet = policySet;

            if (PolicySetHelper.hasPolicyReferenceId(tempPolicySet, pivotId)) {
                targetPolicySet = tempPolicySet;
                break;
            }
        }

        if (targetPolicySet == null) {
            throw new RepositoryException("Id not found: " + pivotId);
        }

        if (!(PolicySetHelper.deletePolicyReference(targetPolicySet, id))) {
            throw new RepositoryException(String.format("Id \"%s\" not found into resource \"%s\"",
                                                        id,
                                                        targetPolicySet.getPolicySetId()));
        }

        int pivotIndex = PolicySetHelper.getPolicyIdReferenceIndex(targetPolicySet, pivotId);

        if (moveAfter) {
            pivotIndex++;
        }
        log.debug("New position for Policy is: " + pivotIndex);

        PolicySetHelper.addPolicyReference(targetPolicySet, pivotIndex, id);

        String version = targetPolicySet.getVersion();

        PolicySetWizard.increaseVersion(targetPolicySet);

        papContainer.updatePolicySet(version, targetPolicySet);
    }

    @Override
    protected void setupPermissions() {
        addRequiredPermission(PAPPermission.of(PermissionFlags.POLICY_WRITE));
    }
}
