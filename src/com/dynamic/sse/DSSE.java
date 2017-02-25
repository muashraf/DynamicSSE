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