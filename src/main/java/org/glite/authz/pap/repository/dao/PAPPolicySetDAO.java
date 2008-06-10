package org.glite.authz.pap.repository.dao;

import java.util.List;

import org.glite.authz.pap.common.xacml.PolicySet;
import org.glite.authz.pap.common.xacml.XACMLObject;

public interface PAPPolicySetDAO {

	public void createAsFirst(PolicySet policySet);

	public void delete(String papId);

	public boolean exists(String papId);

	public PolicySet get(String papId);

	public List<XACMLObject> getAll(String papId);

	public void update(String papId, PolicySet newPolicySet);

}
