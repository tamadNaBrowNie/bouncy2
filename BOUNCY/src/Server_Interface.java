import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Server_Interface extends Remote {
	boolean joinGame(double x, double y, String name) throws RemoteException;

	List<Entity> updateServer(double x, double y, String name, boolean face_right)
			throws RemoteException, InterruptedException, ExecutionException;

	void leaveGame(String name) throws RemoteException;
}
