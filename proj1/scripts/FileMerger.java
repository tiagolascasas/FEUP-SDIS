import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileMerger
{
	public static void main(String[] args) throws IOException
	{
		String name = args[0];
		String id = args[1];
		int n = Integer.parseInt(args[2]);
		
		File outfile = new File(name);
		DataOutputStream stream = new DataOutputStream(new FileOutputStream(outfile));
		
		int x = 0;
		for (int i = 0; i < n; i++)
		{
			File file = new File(id + "#" + i);
		    byte[] fileData = new byte[(int) file.length()];
		    DataInputStream dis = new DataInputStream(new FileInputStream(file));
		    dis.readFully(fileData);
		    dis.close();
		    
			stream.write(fileData, 0, fileData.length);
		}
		stream.close();
	}
}
