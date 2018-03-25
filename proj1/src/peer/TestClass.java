package peer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestClass 
{
	public static void main(String[] args) 
	{
		testDataManager();
		testPutchunk();
	}

	public static void testDataManager()
	{
		DataManager.getInstance().init("1.0", 1, new McastID[] {
				new McastID("224.0.16.0", "10001"),
				new McastID("225.0.32.0", "20001"),
				new McastID("226.0.64.0", "30001")
		});
		String s = "abcdefghijklmnopqrstuvwxyz0123456789\n";
		
		DataManager.getInstance().store("1234", 1, 5, s.getBytes());
		DataManager.getInstance().store("1234", 2, 5, s.getBytes());
		DataManager.getInstance().store("1234", 3, 5, s.getBytes());
		DataManager.getInstance().store("1234", 4, 5, s.getBytes());
		DataManager.getInstance().store("1234", 5, 5, s.getBytes());
		DataManager.getInstance().store("1234", 6, 5, s.getBytes());
		DataManager.getInstance().store("1234", 7, 5, s.getBytes());
		DataManager.getInstance().store("1234", 8, 5, s.getBytes());
		DataManager.getInstance().store("1234", 9, 5, s.getBytes());
		DataManager.getInstance().store("1234", 10, 5, s.getBytes());
		
		DataManager.getInstance().delete("1234", 5);
		DataManager.getInstance().delete("1234", 2);
		
		byte[] data = DataManager.getInstance().retrieve("1234", 1);
		System.out.println(new String(data));
	}
	
	public static byte[] testPutchunk()
	{
		byte[] file = null;
		try
		{
			file = Files.readAllBytes(Paths.get("../scripts/test3.pdf"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return file;
	}
}
