package server.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class Utils
{
	public static String byteToString(ArrayList<Byte> message)
	{
		byte[] array = new byte[message.size()];
		for (int i = 0; i < message.size(); i++)
			array[i] = message.get(i);
		return new String(array, StandardCharsets.US_ASCII);
	}

	public static String hashPassword(String password)
	{
		MessageDigest digest = null;
		try
		{
			digest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		byte[] hash = digest.digest(password.getBytes());
		String asciiHash = hexToAscii(hash);
		
		return asciiHash;
	}
	
	static public String hexToAscii(byte[] hexString)
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < hexString.length; i++)
	        stringBuilder.append(String.format("%02X", hexString[i]));
		return stringBuilder.toString();
	}
	
	static public String decode(String encoded)
	{
		byte[] dec = Base64.getDecoder().decode(encoded);
		return new String(dec, StandardCharsets.US_ASCII);
	}
	
	static public String encode(String decoded)
	{
		byte[] en = Base64.getEncoder().encode(decoded.getBytes());
		return new String(en, StandardCharsets.US_ASCII);
	}
}
