package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAOImpl implements AccountDAO {
    
    @Override
    public Account createAccount(Account account) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                account.setAccount_id(generatedId);
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Account getAccountByUsername(String username) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Account WHERE username = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Account getAccountById(int accountId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Account account = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}