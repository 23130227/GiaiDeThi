import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ITransactionLog extends Remote {
	public boolean deposit(int stk, double money) throws RemoteException;

	public boolean withdraw(int stk, double money) throws RemoteException;

	public double balance(int stk) throws RemoteException;

	public List<TransactionLog> report(int stk) throws RemoteException;
}
