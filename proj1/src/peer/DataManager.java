package peer;

public class DataManager
{
	private static DataManager instance = new DataManager();
	private String version;
	private int id;
	
	private DataManager() {}
	
	public static DataManager getInstance()
	{
		return instance;
	}
	
	public void init(String version, int id)
	{
		this.setVersion(version);
		this.setId(id);
	}

	public String getVersion()
	{
		return version;
	}

	private void setVersion(String version)
	{
		this.version = version;
	}

	public int getId()
	{
		return id;
	}

	private void setId(int id)
	{
		this.id = id;
	}
}
