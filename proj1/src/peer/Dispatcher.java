package peer;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class Dispatcher extends Thread
{
	protected static final int MAX_BUFFER = 65000;
	ThreadPoolExecutor threads;
	
	abstract void processMessage(byte[] message);
}
