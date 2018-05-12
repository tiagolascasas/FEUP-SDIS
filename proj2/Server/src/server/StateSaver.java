package server;

public class StateSaver extends Thread
{
	private static final long SAVE_INTERVAL = 500;

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(SAVE_INTERVAL);
				ServerManager.getInstance().saveState();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
