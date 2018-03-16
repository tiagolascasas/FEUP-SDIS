package peer;

public class TestClass 
{
	public static void main(String[] args) 
	{
		testDataManager();
	}
	
	public static void testDataManager()
	{
		DataManager.getInstance().init("1.0", 1);
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
}
