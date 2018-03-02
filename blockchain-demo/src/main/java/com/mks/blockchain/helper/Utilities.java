package com.mks.blockchain.helper;

public class Utilities {
	
	public static int difficulty = 4;

	// will return "0000", if noOfZero = 4
	public static String getZerosBasedOnInput(int noOfZero) {
		return new String(new char[noOfZero]).replace('\0', '0');
	}

}
