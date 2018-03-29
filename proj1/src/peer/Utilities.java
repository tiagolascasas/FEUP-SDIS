package peer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class Utilities
{
	static public byte[] fileToBinary(String path)
	{
		byte[] file = null;
		try
		{
			file = Files.readAllBytes(Paths.get(path));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}
	
	static public void binaryToFile(byte[] data, String name)
	{
		try
		{
			File outfile = new File(name);
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(outfile));
			stream.write(data, 0, data.length);
			stream.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	static public byte[] calculateMetadataIdentifier(String path)
	{
		Path file = Paths.get(path);
		BasicFileAttributes attrib = null;
		try
		{
			 attrib = Files.readAttributes(file, BasicFileAttributes.class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		StringBuilder metadata = new StringBuilder();
		metadata.append(attrib.creationTime());
		metadata.append(attrib.lastModifiedTime());
		return metadata.toString().getBytes();
	}
	
	static public String calculateFileId(byte[] metadata, byte[] file)
	{
		MessageDigest digest = null;
		try
		{
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		byte[] fullFile = new byte[file.length + metadata.length];
		System.arraycopy(metadata, 0, fullFile, 0, metadata.length);
		System.arraycopy(file, 0, fullFile, metadata.length, file.length);
		
		byte[] hash = digest.digest(fullFile);
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
	
	static public int calculateNumberOfChunks(byte[] data)
	{
		return data.length / 64000 + 1;
	}
}
