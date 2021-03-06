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

package org.glite.authz.pap.client.impl.axis;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.SecureSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.ValidationError;
import eu.emi.security.authn.x509.ValidationErrorListener;
import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.HostnameMismatchCallback;
import eu.emi.security.authn.x509.impl.OpensslCertChainValidator;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;
import eu.emi.security.authn.x509.impl.X500NameUtils;

/**
 * This class provides a CANL-based replacement for Axis 1.x trustmanager secure
 * socket factory class.
 * 
 * @author andreaceccanti
 *
 */
public class CANLAxis1SocketFactory implements SecureSocketFactory,
		HostnameMismatchCallback, ValidationErrorListener {
	
	public static final Logger log = LoggerFactory.getLogger(CANLAxis1SocketFactory.class.getName());

	private String sslProtocol;

	private String certFile;

	private String keyFile;
	private String keyPassword;

	private String proxyFile;

	private int timeout;

	private boolean enforcingHostnameChecks;
	
	private String secureRandomAlgorithm;
	
	private X509CertChainValidatorExt certChainValidator;
	
	private static CANLAxis1SocketFactoryConfigurator configurator;

	public CANLAxis1SocketFactory(@SuppressWarnings("rawtypes")Hashtable attributes) {
		CertificateUtils.configureSecProvider();
		configurator.configure(this);
	}

	public static synchronized void setConfigurator(CANLAxis1SocketFactoryConfigurator conf){
		configurator = conf;
	}
	

	private KeyManager[] getKeymanagers() throws Exception {

		PEMCredential cred;

		if (proxyFile != null){
			cred = new PEMCredential(new FileInputStream(proxyFile), (char[])null);
		}else{

			if (keyPassword != null)
				cred = new PEMCredential(keyFile, certFile, keyPassword.toCharArray());
			else
				cred = new PEMCredential(keyFile, certFile, null);
		}

		return new KeyManager[] { cred.getKeyManager() };

	}

	private TrustManager[] getTrustmanagers() throws Exception {
		
		X509TrustManager trustManager = SocketFactoryCreator
				.getSSLTrustManager(certChainValidator);

		TrustManager[] trustManagers = new TrustManager[] { trustManager };

		return trustManagers;

	}

	private SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
		return new SecureRandom();
	}

	private SSLSocketFactory createSocketFactory() throws Exception {

		SSLContext context = SSLContext.getInstance(sslProtocol);

		KeyManager[] keyManagers = getKeymanagers();
		TrustManager[] trustManagers = getTrustmanagers();
		SecureRandom random = getSecureRandom();

		context.init(keyManagers, trustManagers, random);

		return context.getSocketFactory();

	}

	public Socket create(String host, int port, StringBuffer otherHeaders,
			BooleanHolder useFullURL) throws Exception {
		
		SocketFactory fac = createSocketFactory();

		SSLSocket socket = (SSLSocket) fac.createSocket(host,port);
		socket.setEnabledProtocols(new String[] { sslProtocol });
		socket.setSoTimeout(timeout);
		SocketFactoryCreator.connectWithHostnameChecking(socket, this);
		return socket;
	}

	public void nameMismatch(SSLSocket socket, X509Certificate peerCertificate,
			String hostName) throws SSLException {
		
		if (enforcingHostnameChecks) {
			try {

				socket.close();

			} catch (IOException e) {

			}

			String peerCertificateSubject = X500NameUtils
					.getReadableForm(peerCertificate.getSubjectX500Principal());
			String message = String.format(
					"Peer certificate subject %s does not match hostname %s",
					peerCertificateSubject, hostName);
			throw new SSLException(message);

		}
	}

	public boolean onValidationError(ValidationError error) {
		log.error("Certificate validation error: {}", error);
		return false;
	}

	/**
	 * @param sslProtocol the sslProtocol to set
	 */
	public synchronized void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	/**
	 * @param certFile the certFile to set
	 */
	public synchronized void setCertFile(String certFile) {
		this.certFile = certFile;
	}

	/**
	 * @param keyFile the keyFile to set
	 */
	public synchronized void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}

	/**
	 * @param keyPassword the keyPassword to set
	 */
	public synchronized void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	/**
	 * @param proxyFile the proxyFile to set
	 */
	public synchronized void setProxyFile(String proxyFile) {
		this.proxyFile = proxyFile;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public synchronized void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param enforcingHostnameChecks the enforcingHostnameChecks to set
	 */
	public synchronized void setEnforcingHostnameChecks(
			boolean enforcingHostnameChecks) {
		this.enforcingHostnameChecks = enforcingHostnameChecks;
	}

	/**
	 * @param secureRandomAlgorithm the secureRandomAlgorithm to set
	 */
	public synchronized void setSecureRandomAlgorithm(String secureRandomAlgorithm) {
		this.secureRandomAlgorithm = secureRandomAlgorithm;
	}

	/**
	 * @return the certChainValidator
	 */
	public synchronized X509CertChainValidatorExt getCertChainValidator() {
		return certChainValidator;
	}

	/**
	 * @param certChainValidator the certChainValidator to set
	 */
	public synchronized void setCertChainValidator(
			X509CertChainValidatorExt certChainValidator) {
		this.certChainValidator = certChainValidator;
	}	
	
	
}
