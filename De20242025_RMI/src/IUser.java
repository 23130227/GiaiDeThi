import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUser extends Remote {
	public boolean checkUser(String username) throws RemoteException;

	public User login(String username, String password) throws RemoteException;
}
