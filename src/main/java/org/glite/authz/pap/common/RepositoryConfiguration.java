package org.glite.authz.pap.common;

import java.io.File;

public class RepositoryConfiguration {

	private static final String fileSystemDatabaseDir = "/tmp/paprep";
	private static final String rootPolicySetId = "Root";
	private static final String rootPAPPolicySetId = "PAPRoot";
	private static final String policySetFileNamePrefix = "PolicySet_";
	private static final String policyFileNamePrefix = "Policy_";
	private static final String xacmlFileNameExtension = ".xml";
	private static final String rootPolicySetTemplatePath = "files/RootPolicySetTemplate.xml";
	private static final String papPolicySetTemplatePath = "files/RootPAPPolicySetTemplate.xml";

	public static String getFileSystemDatabaseDir() {
		return fileSystemDatabaseDir;
	}

	public static String getPAPDirAbsolutePath(String papId) {
		return fileSystemDatabaseDir + File.separator + papId + File.separator;
	}

	public static String getPapPolicySetTemplatePath() {
		return papPolicySetTemplatePath;
	}

	public static String getPolicyAbsolutePath(String papId, String policyId) {
		return getPAPDirAbsolutePath(papId) + getPolicyFileName(policyId);
	}

	public static String getPolicyFileName(String policyId) {
		return policyFileNamePrefix + policyId + xacmlFileNameExtension;
	}

	public static String getPolicyFileNamePrefix() {
		return policyFileNamePrefix;
	}

	public static String getPolicySetAbsolutePath(String papId,
			String policySetId) {
		return getPAPDirAbsolutePath(papId) + getPolicySetFileName(policySetId);
	}

	public static String getPolicySetFileName(String policySetId) {
		return policySetFileNamePrefix + policySetId + xacmlFileNameExtension;
	}

	public static String getPolicySetFileNamePrefix() {
		return policySetFileNamePrefix;
	}

	public static String getRootPAPPolicySetId() {
		return rootPAPPolicySetId;
	}

	public static String getRootPolicySetFileName() {
		return rootPolicySetId;
	}

	public static String getRootPolicySetTemplatePath() {
		return rootPolicySetTemplatePath;
	}

	public static String getXACMLFileNameExtension() {
		return xacmlFileNameExtension;
	}

}
