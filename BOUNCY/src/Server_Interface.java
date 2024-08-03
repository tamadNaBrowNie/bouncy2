import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

//import javafx.scene.Node;

public interface Server_Interface extends Remote{
//	List<Entity> getBalls() throws RemoteException;
	boolean joinGame(double x, double y, String name)throws RemoteException;
	List<Entity> updateServer(double x, double y, String name, boolean face_right) throws RemoteException, InterruptedException, ExecutionException;
//	void updatePos(double x, double y, String name)throws RemoteException;
	void leaveGame(String name)throws RemoteException;
//	void updatePos(double x, double y, String name, boolean face_right) throws RemoteException, InterruptedException, ExecutionException;
}
