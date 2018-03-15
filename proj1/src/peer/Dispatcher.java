package peer;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class Dispatcher extends Thread
{
	ThreadPoolExecutor threads;
	
	abstract void processMessage(byte[] message);
}
