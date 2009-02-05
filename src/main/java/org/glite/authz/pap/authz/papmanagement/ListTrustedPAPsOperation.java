package org.glite.authz.pap.authz.papmanagement;

import java.util.List;

import org.glite.authz.pap.authz.BasePAPOperation;
import org.glite.authz.pap.authz.PAPPermission;
import org.glite.authz.pap.authz.PAPPermission.PermissionFlags;
import org.glite.authz.pap.common.PAP;
import org.glite.authz.pap.distribution.PAPManager;
import org.glite.authz.pap.services.pap_management.axis_skeletons.PAPData;


public class ListTrustedPAPsOperation extends BasePAPOperation<PAPData[]> {

    
    
    protected ListTrustedPAPsOperation() {

        // TODO Auto-generated constructor stub
    }
    
    
    public static ListTrustedPAPsOperation instance() {

        return new ListTrustedPAPsOperation();
    }
    
    
    @Override
    protected PAPData[] doExecute() {
        
        List<PAP> papList = PAPManager.getInstance().getAllTrustedPAPs();
        
        PAPData[] papArray = new PAPData[papList.size()];
        
        for (int i=0; i<papArray.length; i++) {
            PAP pap = papList.get(i);
            
            PAPData papData = new PAPData();
            
            papData.setAlias(pap.getAlias());
            papData.setDn(pap.getDn());
            papData.setHostname(pap.getHostname());
            papData.setPapId(pap.getPapId());
            papData.setPath(pap.getPath());
            papData.setPort(pap.getPort());
            papData.setProtocol(pap.getProtocol());
            papData.setVisibilityPublic(pap.isVisibilityPublic());
            
            papArray[i] = papData;
        }

        return papArray;
    }

    @Override
    protected void setupPermissions() {

        addRequiredPermission( PAPPermission.of( PermissionFlags.CONFIGURATION_READ ) );

    }

}