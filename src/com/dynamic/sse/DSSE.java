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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.NoSuchPaddingException;

import com.dynamic.sse.CryptoPrimitives;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sun.corba.se.impl.ior.ByteBuffer;

import textExtract.TextExtractPar;
import textExtract.TextProc;

public class DSSE {

	static Multimap<String, String> words = ArrayListMultimap.create();
	static Multimap<String, String> files = ArrayListMultimap.create();
	static CryptoPrimitives cp = new CryptoPrimitives();
	static List<byte[]> listSK = new ArrayList<byte[]>();

	public DSSE() {
	}

	public static void main(String args[]) throws Exception {
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
		listSK = cp.keyGen();
		DSSE dsse = new DSSE();
		dsse.encryptFiles();
		ArrayList<String> ab = new ArrayList<String>();
		HashMap<String,ArrayList<Integer>> wordTable = new HashMap<String,ArrayList<Integer>>();
		int i = 0;
		for( Entry<String, Collection<String>> entry : words.asMap().entrySet())
		{
			Object key = entry.getKey();
			Object value = entry.getValue();
			String s = cp.generateCMAC(listSK.get(0), key.toString());
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i++);
			temp.add(0);
			wordTable.put(s, temp);
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
		for( Entry<String, Collection<String>> entry : files.asMap().entrySet())
		{
			Object key = entry.getKey();
			Object value = entry.getValue();
			String s = cp.generateCMAC(listSK.get(0), key.toString());
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(j++);
			temp.add(0);
			fileTable.put(s, temp);
		}
		
		for (Entry<String, ArrayList<Integer>> ee : fileTable.entrySet()) 
		{
			
		    String key = ee.getKey();
		    List<Integer> values = ee.getValue();
		    
		    System.out.println("Hash Table for files");
		    System.out.println(key + " :::::: " + values);
		}
	}

	private void encryptFiles() {
		HashMap<String, Integer> fileMap = new HashMap<String, Integer>();
		int count = 1;
		for (String f : files.keySet()) {
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
			words = TextExtractPar.lp1;
			files = TextExtractPar.lp2;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchProviderException | NoSuchPaddingException | InvalidKeySpecException | IOException e) {
			e.printStackTrace();
		}
	}
}