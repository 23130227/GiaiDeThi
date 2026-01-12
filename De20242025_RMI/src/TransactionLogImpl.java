import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class TransactionLogImpl extends UnicastRemoteObject implements ITransactionLog {
	private DAO dao;

	public TransactionLogImpl() throws RemoteException, SQLException {
		this.dao = new DAO();
	}

	@Override
	public boolean deposit(int stk, double money) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return dao.deposit(stk, money);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean withdraw(int stk, double money) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return dao.withdraw(stk, money);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public double balance(int stk) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return dao.balance(stk);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	@Override
	public List<TransactionLog> report(int stk) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			return dao.report(stk);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
