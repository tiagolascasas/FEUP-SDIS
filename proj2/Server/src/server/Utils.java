package server;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Utils
{
	public static String byteToString(ArrayList<Byte> message)
	{
		byte[] array = new byte[message.size()];
		for (int i = 0; i < message.size(); i++)
			array[i] = message.get(i);
		return new String(array, StandardCharsets.US_ASCII);
	}
}
