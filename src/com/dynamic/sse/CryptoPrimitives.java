package com.dynamic.sse;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptoPrimitives {

	public CryptoPrimitives() {

	}

	public List<byte[]> keyGen() throws Exception {

		List<byte[]> listOfkeys = new ArrayList<byte[]>();
		Security.addProvider(new BouncyCastleProvider());
		// Generation of two keys for CMAC/CCM
		listOfkeys.add(keyGeneration());
		listOfkeys.add(keyGeneration());
		listOfkeys.add(keyGeneration());

		// Generation random vector for CCM
		listOfkeys.add(randomBytes(8));

		return listOfkeys;

	}

	public static byte[] keyGeneration() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
		generator.init(128);
		Key keyToBeWrapped = generator.generateKey();
		return keyToBeWrapped.getEncoded();
	}

	public static byte[] randomBytes(int sizeOfSalt) {
		byte[] salt = new byte[sizeOfSalt];
		ThreadedSeedGenerator thread = new ThreadedSeedGenerator();
		SecureRandom random = new SecureRandom();
		random.setSeed(thread.generateSeed(20, true));
		random.nextBytes(salt);
		return salt;
	}

	public byte[] generateCMAC(byte[] key, String msg) throws Exception {
		CMac cmac = new CMac(new AESFastEngine());
		byte[] data = msg.getBytes("UTF-8");
		byte[] output = new byte[cmac.getMacSize()];
		cmac.init(new KeyParameter(key));
		cmac.reset();
		cmac.update(data, 0, data.length);
		cmac.doFinal(output, 0);
		return output;
	}

	protected String encryptCCM(byte[] key, byte[] vector, String msg) {
		try {
			IvParameterSpec iv = new IvParameterSpec(vector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(msg.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	protected String decryptCCM(byte[] key, byte[] vector, String msg) {
		try {
			IvParameterSpec iv = new IvParameterSpec(vector);
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(msg));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
