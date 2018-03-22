package peer.handler.rmi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import peer.CHANNELS;
import peer.DataManager;
import peer.handler.Handler;
import peer.message.MessagePutchunk;

public class StoreHandler extends Handler
{
	private static final int MAX_CHUNK_SIZE = 64000;
	private byte[] file;
	private byte[] metadata;
	private int repDegree;
	
	public StoreHandler(byte[] file, byte[] metadata, int repDegree)
	{
		this.file = file;
		this.metadata = metadata;
		this.repDegree = repDegree;
	}
	
	@Override
	public void run()
	{
		int fileSize = file.length;
		int chunkNo = 0;
		byte[] id = calculateId();
		DataManager.getInstance().registerToNotStore(new String(id, StandardCharsets.US_ASCII));
		int pos = 0;
		
		while(fileSize > 0)
		{
			MessagePutchunk message = new MessagePutchunk(id, chunkNo, repDegree);
			int chunkSize = (fileSize > MAX_CHUNK_SIZE) ? MAX_CHUNK_SIZE : fileSize;
			byte[] chunkData = new byte[chunkSize];
			System.arraycopy(this.file, pos, chunkData, 0, chunkSize);
			message.setBody(chunkData);
			fileSize -= chunkSize;
			pos += chunkSize;
			chunkNo++;
			send(CHANNELS.MDB, message.getMessageBytes());
		}
	}

	private byte[] calculateId()
	{
		MessageDigest digest = null;
		try
		{
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		byte[] fullFile = new byte[this.file.length + this.metadata.length];
		System.arraycopy(this.metadata, 0, fullFile, 0, this.metadata.length);
		System.arraycopy(this.file, 0, fullFile, this.metadata.length, this.file.length);
		
		byte[] hash = digest.digest(fullFile);
		String asciiHash = hexToAscii(hash);
		
		return asciiHash.getBytes();
	}
	
	private String hexToAscii(byte[] hexString)
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < hexString.length; i++)
	        stringBuilder.append(String.format("%02X", hexString[i]));
		return stringBuilder.toString();
	}
}
