package org.glite.authz.pap.common.xacml.wizard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeWizardType {

    private static final Logger log = LoggerFactory.getLogger(AttributeWizardType.class);

    public enum TargetElement {
        ACTION, ENVIRONMENT, RESOURCE, SUBJECT
    }

    private String dataType;
    private String id;
    private TargetElement targetElement;
    private String xacmlId;
    private String matchFunction;
    
    private String matchFunctionDataType;

    public AttributeWizardType(String id, String xacmlId, String dataType, TargetElement category, String matchFunction, String matchFunctionDataType) {
        this.id = id;
        this.xacmlId = xacmlId;
        this.dataType = dataType;
        this.targetElement = category;
        this.matchFunction = matchFunction;
        this.matchFunctionDataType = matchFunctionDataType;
    }

    public String getDataType() {
        return dataType;
    }

    public String getMatchFunction() {
        return matchFunction;
    }

    public String getId() {
        return id;
    }

    public AttributeWizardType.TargetElement getTargetElement() {
        return targetElement;
    }

    public String getXacmlId() {
        return xacmlId;
    }

    
    public String getMatchFunctionDataType() {
		return matchFunctionDataType;
	}


	public boolean equals(Object object) {

        if (!(object instanceof AttributeWizardType)) {
            log.trace("equals(): false. Not an AttributeWizardType: " + object.getClass().getName());
            return false;
        }

        AttributeWizardType attributeWizardType = (AttributeWizardType) object;

        if (!(id.equals(attributeWizardType.getId()))) {
            log.trace("equals(): false. this.id=" + id + " id=" + attributeWizardType.getId());
            return false;
        }

        if (!(xacmlId.equals(attributeWizardType.getXacmlId()))) {
            log.trace("equals(): false. this.xacmlId=" + xacmlId + " xacmlId=" + attributeWizardType.getXacmlId());
            return false;
        }

        if (!(dataType.equals(attributeWizardType.getDataType()))) {
            log.trace("equals(): false. this.dataType=" + dataType + " dataType=" + attributeWizardType.getDataType());
            return false;
        }

        if (!(targetElement.equals(attributeWizardType.getTargetElement()))) {
            log.trace("equals(): false. this.targetElement=" + targetElement + " targetElement=" + attributeWizardType.getTargetElement());
            return false;
        }

        if (!(matchFunction.equals(attributeWizardType.getMatchFunction()))) {
            log.trace("equals(): false. this.matchFunction=" + matchFunction + " matchFunction=" + attributeWizardType.getMatchFunction());
            return false;
        }

        return true;
    }
}
