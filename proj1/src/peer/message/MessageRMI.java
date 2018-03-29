package peer.message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageRMI extends Remote {
	byte[] sendMessage(String operation, String[] args, byte[][] file) throws RemoteException;
}
