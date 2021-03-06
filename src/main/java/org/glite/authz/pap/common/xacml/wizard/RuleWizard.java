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

package org.glite.authz.pap.common.xacml.wizard;

import java.util.ArrayList;
import java.util.List;

import org.glite.authz.pap.common.utils.Utils;
import org.glite.authz.pap.common.xacml.utils.RuleHelper;
import org.glite.authz.pap.common.xacml.wizard.exceptions.WizardException;
import org.opensaml.xacml.policy.EffectType;
import org.opensaml.xacml.policy.RuleType;

public class RuleWizard {
    
    private EffectType effect = null;
    private RuleType rule;
    private String ruleId = null;
    private final TargetWizard targetWizard;
    
    public RuleWizard(AttributeWizard attributeWizard, EffectType effect) {
        this(getList(attributeWizard), effect);
    }
    
    public RuleWizard(EffectType effect) {
        this(getList(null), effect);
    }
    
    public RuleWizard(List<AttributeWizard> targetAttributeWizardList, EffectType effect) {
        
        if (targetAttributeWizardList == null) {
            targetAttributeWizardList = new ArrayList<AttributeWizard>(0);
        }
        
        targetWizard = new TargetWizard(targetAttributeWizardList);
        this.effect = effect;
        ruleId = WizardUtils.generateId(null);
    }
    
    public RuleWizard(RuleType rule) {
        
        if (rule == null) {
            throw new WizardException("Invalid argument: RuleType is null.");
        }
        
        this.rule = rule;
        ruleId = rule.getRuleId();
        effect = rule.getEffect();
        targetWizard = new TargetWizard(rule.getTarget());
    }
    
    private static List<AttributeWizard> getList(AttributeWizard attributeWizard) {
        
        if (attributeWizard == null) {
            return new ArrayList<AttributeWizard>(0);
        }
        
        List<AttributeWizard> list = new ArrayList<AttributeWizard>(1);
        
        list.add(attributeWizard);
        
        return list;
    }
    
    public boolean deniesAttribute(AttributeWizard attributeWizard) {
        
        if (!(EffectType.Deny.equals(rule.getEffect()))) {
            return false;
        }
        
        for (AttributeWizard targetAttributeWizard : targetWizard.getAttributeWizardList()) {
            if (targetAttributeWizard.equals(attributeWizard)) {
                return true;
            }
        }
        
        return false;
    }
    
    public String getRuleId() {
        return ruleId;
    }
    
    public RuleType getXACML() {
        initRuleTypeIfNotSet();
        return rule;
    }
    
    public boolean isEquivalent(RuleType rule) {
        
        if (effect != rule.getEffect()) {
            return false;
        }
        
        if (rule.getCondition() != null) {
            return false;
        }
        
        if (!(targetWizard.isEquivalent(rule.getTarget()))) {
            return false;
        }
        
        return true;
    }
    
    public void releaseChildrenDOM() {
        targetWizard.releaseChildrenDOM();
        targetWizard.releaseDOM();
        if (rule != null) {
            rule.releaseChildrenDOM(true);
        }
    }
    
    public void releaseDOM() {
        if (rule != null) {
            rule.releaseDOM();
            rule = null;
        }
    }

    public String toFormattedString(boolean printIds) {
        return toFormattedString(0, 4, printIds);
    }
    
    public String toFormattedString(int baseIndentation, int internalIndentation, boolean printIds) {
        
        String baseIndentString = Utils.fillWithSpaces(baseIndentation);
        String indentString = Utils.fillWithSpaces(baseIndentation + internalIndentation);
        StringBuffer sb = new StringBuffer();
        
        String effectString = effect.toString().toLowerCase();
        
        if (printIds) {
            sb.append(String.format("%sid=%s\n", baseIndentString, ruleId));
        }
        
        boolean multipleLines = (targetWizard.getAttributeWizardList().size() > 1);
        
        sb.append(String.format("%srule %s { ", baseIndentString, effectString));
        
        if (multipleLines) {
        	sb.append('\n');
        }
        
        for (AttributeWizard attributeWizard : targetWizard.getAttributeWizardList()) {
            
            if (multipleLines) {
                sb.append(indentString);
            }
            
            sb.append(String.format("%s ", attributeWizard.toFormattedString()));
            
            if (multipleLines) {
            	sb.append('\n');
            }
        }
        
        if (multipleLines) {
            sb.append(baseIndentString);
        }
        
        sb.append("}");
        
        return sb.toString();
    }
    
    private void initRuleTypeIfNotSet() {
        if (rule == null) {
            setRuleType();
        }
    }
    
    private void setRuleType() {
        
        releaseDOM();
        
        rule = RuleHelper.build(ruleId, effect);
        
        rule.setTarget(targetWizard.getXACML());
    }
}
