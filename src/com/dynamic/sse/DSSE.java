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

import textExtract.TextExtractPar;
import textExtract.TextProc;

public class DSSE {

	public DSSE() {
	}

	protected void wordExtract() {
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
		String path = null;
		try {
			path = buffReader.readLine();
			ArrayList<File> listOfFiles = new ArrayList<File>();
			TextProc.listf(path, listOfFiles);
			TextProc.TextProc(false, path);
		} catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

}
