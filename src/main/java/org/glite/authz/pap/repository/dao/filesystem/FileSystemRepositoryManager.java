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

package org.glite.authz.pap.repository.dao.filesystem;

import java.io.File;
import java.io.IOException;

import org.glite.authz.pap.common.PAPConfiguration;
import org.glite.authz.pap.repository.RepositoryManager;
import org.glite.authz.pap.repository.exceptions.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemRepositoryManager extends RepositoryManager {

    /** Prefix used for the file names of the policies: {@value} */
    protected static final String POLICY_FILENAME_PREFIX = "Policy_";
    
    /** Prefix used for the file names of the policy sets: {@value} */
    protected static final String POLICYSET_FILENAME_PREFIX = "PolicySet_";
    
    /** Extension used for the file names of the policies: {@value} */
    protected static final String XACML_FILENAME_EXTENSION = ".xml";

    private static final Logger log = LoggerFactory.getLogger(FileSystemRepositoryManager.class);
    private static String fileSystemDatabaseDir;
    private static boolean initialized = false;
    private static FileSystemRepositoryManager instance = null;

    /**
     * Call the initialize() method before using this class.
     */

    private FileSystemRepositoryManager() {}
    
    public static String getFileNameExt() {
        return XACML_FILENAME_EXTENSION;
    }

    public static String getFileSystemDatabaseDir() {
        return fileSystemDatabaseDir;
    }

    public static FileSystemRepositoryManager getInstance() {
        
        if (instance == null) {
            instance = new FileSystemRepositoryManager();
        }
        return instance;
    }

    public static String getPAPDirAbsolutePath(String papId) {
        return fileSystemDatabaseDir + File.separator + papId + File.separator;
    }

    public static String getPolicyFileNamePrefix() {
        return POLICY_FILENAME_PREFIX;
    }

    public static String getPolicySetFileNamePrefix() {
        return POLICYSET_FILENAME_PREFIX;
    }

    public static String getXACMLFileNameExtension() {
        return XACML_FILENAME_EXTENSION;
    }

    /**
     * This method checks that the directory path leading to the repository is in 
     * good shape. If the directory path does not exists, it is created.
     * 
     * @param dir, the repository directory
     * @throws RepositoryException, if something goes wrong
     */
    private static void setupRepositoryDirectory(File dir) {

        if (!dir.exists()) {
            if (!dir.mkdirs()) // Find out what went wrong...
                setupRepositoryDirectory(dir.getParentFile());
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

    public String getRepositoryVersion() {
    	if (!initialized) {
    		throw new RepositoryException("FileSytemRepository not initialized");
    	}
		return FileSystemPapDAO.getInstance().getVersion();
	}
    
    public void initialize() {
        log.info("Initializing filesystem repository...");

        fileSystemDatabaseDir = PAPConfiguration.instance().getPAPRepositoryDir();

        File rootDir = new File(fileSystemDatabaseDir);

        try {
            setupRepositoryDirectory(rootDir);
        } catch (RepositoryException e) {
            throw new RepositoryException("Cannot create the repository root directory: " + rootDir.getAbsolutePath(), e);
        }

        initialized = true;
        log.info("Repository root directory is set to: " + rootDir.getAbsolutePath());
    }
}
