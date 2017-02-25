package com.dynamic.sse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;


import textExtract.TextProc;
import textExtract.TextExtractPar;

public class DSSE {

	private byte[] K = Hex.decode("404142434445464748494a4b4c4d4e4f");
	private byte[] N = Hex.decode("10111213141516");

	public DSSE() {
	}
	
	
	public static void main(String args[])
	{
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
		String path = null;
		System.out.println("Enter folder path to index files");
		try 
		{
			path = buffReader.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		wordExtract(path);
	}
	public byte[] generateCMAC(String msg)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException {
		CMac cmac = new CMac(new AESFastEngine());
		byte[] data = msg.getBytes("UTF-8");
		byte[] output = new byte[cmac.getMacSize()];
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
		generator.init(128);
		Key keyToBeWrapped = generator.generateKey();
		cmac.init(new KeyParameter(keyToBeWrapped.getEncoded()));
		cmac.reset();
		cmac.update(data, 0, data.length);
		cmac.doFinal(output, 0);
		return output;
	}

	protected String encryptCCM(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(N);
			SecretKeySpec skeySpec = new SecretKeySpec(K, "AES");
			Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	protected String decryptCCM(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(N);
			SecretKeySpec skeySpec = new SecretKeySpec(K, "AES");
			Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected static void wordExtract(String filePath) {
			ArrayList<File> listOfFiles = new ArrayList<File>();
			TextProc.listf(filePath, listOfFiles);
			try 
			{
				TextProc.TextProc(false, filePath);
				System.out.println(TextExtractPar.lp1);
			} 
			catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
					| NoSuchProviderException | NoSuchPaddingException | InvalidKeySpecException | IOException e) 
			{
				e.printStackTrace();
			}
		 
	}

}