package com.dynamic.sse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

import com.dynamic.sse.CryptoPrimitives;

import textExtract.TextExtractPar;
import textExtract.TextProc;

public class DSSE {

	static CryptoPrimitives cp = new CryptoPrimitives();
	static List<byte[]> listSK = new ArrayList<byte[]>();

	public DSSE() {
	}

	public static void main(String args[]) throws Exception {
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
		String path = null;
		System.out.println("Enter folder path to index files:");
		
		try 
		{
			path = buffReader.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		wordExtract(path);
		listSK = cp.keyGen();
		
		DSSE dsse = new DSSE();
		

		Set<String> words = TextExtractPar.lp1.keySet();
		Set<String> files = TextExtractPar.lp2.keySet();
		
		HashMap<String,ArrayList<Integer>> wordTable = new HashMap<String,ArrayList<Integer>>();
		int i = 0;
		for( String w: words)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i++);
			temp.add(0);
			wordTable.put(cp.generateCMAC(listSK.get(0), w), temp);
		}
		
		for (Entry<String, ArrayList<Integer>> ee : wordTable.entrySet()) {
		    String key = ee.getKey();
		    List<Integer> values = ee.getValue();
		    System.out.println("Hash Table for words");
		   System.out.println(key + " :::::: " + values);
		}
		
		
		System.out.println("/////////////////////////////////////////");
		
		HashMap<String,ArrayList<Integer>> fileTable = new HashMap<String,ArrayList<Integer>>();
		int j = 0;
		for( String f: files)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(j++);
			temp.add(0);
			fileTable.put(cp.generateCMAC(listSK.get(0), f), temp);
		}
		
		for (Entry<String, ArrayList<Integer>> ee : fileTable.entrySet()) 
		{
			
		    String key = ee.getKey();
		    List<Integer> values = ee.getValue();
		    
		    System.out.println("Hash Table for files");
		    System.out.println(key + " :::::: " + values);
		}
		
		dsse.encryptFiles();
		Matrix A = Matrix.random(wordTable.size(), fileTable.size());
	}

	private void encryptFiles() {
		HashMap<String, Integer> fileMap = new HashMap<String, Integer>();
		int count = 1;
		for (String f : TextExtractPar.lp2.keySet()) {
			String fileEncrypt = cp.encryptCCM(listSK.get(2), listSK.get(3), f);
			fileMap.put(fileEncrypt, count);
			count++;
		}
	}

	protected static void wordExtract(String filePath) {
		ArrayList<File> listOfFiles = new ArrayList<File>();
		TextProc.listf(filePath, listOfFiles);
		try {
			TextProc.TextProc(false, filePath);

		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException | InvalidKeySpecException | IOException e) {
			e.printStackTrace();
		}
	}
}