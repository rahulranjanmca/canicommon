package com.canigenus.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.xml.bind.DatatypeConverter;

import com.canigenus.common.constants.GenericConstant;
import com.canigenus.common.service.JpaGenericServiceImpl;

public class PasswordUtil {
	


	public static Map<String, String> enctyptPassword(String password,
			String salt) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {

		// Salt generation 64 bits long
		byte[] bSalt = new byte[8];

		String sSalt = null;
		if (salt == null) {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(bSalt);
			sSalt = DatatypeConverter.printBase64Binary(bSalt);
		} else {

			sSalt = salt;

		}
		String sDigest = getHash(GenericConstant.HASH_ITERATION_NO, password,
				sSalt);

		Map<String, String> map = new HashMap<String, String>();
		map.put(GenericConstant.PASSWORD, sDigest);
		map.put(GenericConstant.SALT, sSalt);
		return map;
	}

	public static String getHash(int iterationNb, String password, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-384");/* SHA-2 */
		digest.reset();
		digest.update(salt.getBytes("UTF-8"));
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}

		String sDigest = DatatypeConverter.printBase64Binary(input);

		System.out.println("Digested Password:" + sDigest);
		return sDigest;
	}

	/*
	private static byte[] encrypt(String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	private static String decrypt(byte[] encryptionBytes)
			throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException {
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
		String recovered = new String(recoveredBytes);
		return recovered;
	}*/

	public void main(){
  CriteriaPopulator<JpaCriteriaHelper<PasswordUtil>>	populator= 	new CriteriaPopulator<JpaCriteriaHelper<PasswordUtil>>() {

			@Override
			public void populateCriteria(JpaCriteriaHelper<PasswordUtil> t) {
				// TODO Auto-generated method stub
				
			}
			
		
		};
		
		JpaGenericServiceImpl jpaGenericServiceImpl= new JpaGenericServiceImpl() {
			
			@Override
			public <T> void delete(Object id, Class<T> classType) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public EntityManager getEntityManager() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		jpaGenericServiceImpl.getModelCount(PasswordUtil.class, populator);
	}
	
}
