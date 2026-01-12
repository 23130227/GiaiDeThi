import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAO {
	Connection connection;

	public DAO() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:ucanaccess://D:/De20242025_RMI.accdb");
	}

	public boolean checkUser(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	public User login(String username, String password) throws SQLException {
		User user = null;
		if (!checkUser(username)) {
			return user;
		}
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user = new User(rs.getString("username"), rs.getString("password"), rs.getInt("stk"),
					rs.getDouble("money"));
		}
		return user;
	}

	public boolean deposit(int stk, double money) throws SQLException {
		String sql = "UPDATE users SET money = money + ? WHERE stk = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDouble(1, money);
		ps.setInt(2, stk);
		insertTracsactionLog(new TransactionLog(stk, true, LocalDate.now(), money));
		return ps.executeUpdate() > 0;
	}

	public boolean withdraw(int stk, double money) throws SQLException {
		String sql = "UPDATE users SET money = money - ? WHERE stk = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDouble(1, money);
		ps.setInt(2, stk);
		insertTracsactionLog(new TransactionLog(stk, false, LocalDate.now(), money));
		return ps.executeUpdate() > 0;
	}

	public boolean insertTracsactionLog(TransactionLog transactionLog) throws SQLException {
		String sql = "INSERT INTO transaction_logs VALUES (?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, transactionLog.getStk());
		ps.setBoolean(2, transactionLog.isOperation());
		ps.setDate(3, java.sql.Date.valueOf(transactionLog.getDate()));
		ps.setDouble(4, transactionLog.getMoney());
		return ps.executeUpdate() > 0;
	}

	public double balance(int stk) throws SQLException {
		String sql = "SELECT money FROM users WHERE stk = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, stk);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getDouble("money");
	}

	public List<TransactionLog> report(int stk) throws SQLException {
		List<TransactionLog> result = new ArrayList<TransactionLog>();
		String sql = "SELECT * from transaction_logs WHERE stk = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, stk);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			TransactionLog transactionLog = new TransactionLog(rs.getInt("stk"), rs.getBoolean("operation"),
					rs.getDate("date").toLocalDate(), rs.getDouble("money"));
			result.add(transactionLog);
		}
		return result;
	}
}
