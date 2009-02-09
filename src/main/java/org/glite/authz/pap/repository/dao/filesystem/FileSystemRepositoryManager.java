package org.glite.authz.pap.repository.dao.filesystem;

import java.io.File;
import java.io.IOException;

import org.glite.authz.pap.common.PAP;
import org.glite.authz.pap.common.PAPConfiguration;
import org.glite.authz.pap.repository.RepositoryManager;
import org.glite.authz.pap.repository.exceptions.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemRepositoryManager extends RepositoryManager {

	protected static String fileSystemDatabaseDir;
	private static final String LOCAL_PAP_ID = PAP.localPAPAlias;
	private static final Logger log = LoggerFactory.getLogger(FileSystemRepositoryManager.class);
	private static final String POLICY_FILENAME_PREFIX = "Policy_";
	private static final String POLICYSET_FILENAME_PREFIX = "PolicySet_";
	private static final String ROOT_POLICYSET_ID = "Root";
	private static final String XACML_FILENAME_EXTENSION = ".xml";

	/*
	 * Call the initialize() method before using this class. 
	 */
	
	private FileSystemRepositoryManager() {}

	public static String getFileSystemDatabaseDir() {
		return fileSystemDatabaseDir;
	}

	public static String getLocalPAPId() {
		return LOCAL_PAP_ID;
	}

	public static String getPAPDirAbsolutePath(String papId) {
		return fileSystemDatabaseDir + File.separator + papId + File.separator;
	}

	public static String getPolicyAbsolutePath(String papId, String policyId) {
		return getPAPDirAbsolutePath(papId) + getPolicyFileName(policyId);
	}

	public static String getPolicyFileName(String policyId) {
		return POLICY_FILENAME_PREFIX + policyId + XACML_FILENAME_EXTENSION;
	}

	public static String getPolicyFileNamePrefix() {
		return POLICY_FILENAME_PREFIX;
	}

	public static String getPolicySetAbsolutePath(String papId, String policySetId) {
		return getPAPDirAbsolutePath(papId) + getPolicySetFileName(policySetId);
	}

	public static String getPolicySetFileName(String policySetId) {
		return POLICYSET_FILENAME_PREFIX + policySetId + XACML_FILENAME_EXTENSION;
	}

	public static String getPolicySetFileNamePrefix() {
		return POLICYSET_FILENAME_PREFIX;
	}

	public static String getRootPolicySetId() {
		return ROOT_POLICYSET_ID;
	}

	public static String getXACMLFileNameExtension() {
		return XACML_FILENAME_EXTENSION;
	}

	public static void initialize() {
		log.info("Initializing filesystem repository...");
		
		fileSystemDatabaseDir = PAPConfiguration.instance().getPAPRepositoryDir();

		File rootDir = new File(fileSystemDatabaseDir);

		try {
			createDirectoryPath(rootDir);
		} catch (RepositoryException e) {
			throw new RepositoryException("Cannot create the repository root directory: "
					+ rootDir.getAbsolutePath(), e);
		}

		log.info("Repository root directory is set to: " + rootDir.getAbsolutePath());
	}

	private static void createDirectoryPath(File dir) {

		if (!dir.exists()) {
			if (!dir.mkdirs()) // Find out what went wrong...
				createDirectoryPath(dir.getParentFile());
		}

		if (!dir.canRead())
			throw new RepositoryException("Read permission not set: " + dir.getAbsolutePath());

		if (!dir.canWrite())
			throw new RepositoryException("Write permission not set: " + dir.getAbsolutePath());

		// Workaround for the canExecute method which does not exist in Java 5
		try {
			File tempFile = new File(dir.getAbsoluteFile() + File.separator + "delete_me.tmp");
			tempFile.createNewFile();
			tempFile.delete();
		} catch (IOException e) {
			throw new RepositoryException("Execute permission not set: " + dir.getAbsolutePath(), e);
		}
	}

}
