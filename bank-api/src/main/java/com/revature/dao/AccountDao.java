package com.revature.dao;

import com.revature.model.Account;
import com.revature.utility.ConnectionUtility;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    //POST
    public Account addAccount (Account account, int clientId) throws SQLException{
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO accounts (a_type, a_number, date_opened, client_id) " +
                    "VALUES ( ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, account.getAccountType());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.setDate(3, Date.valueOf(account.getDateOpened()));
            pstmt.setInt(4, clientId);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt("id");
            BigDecimal balance = rs.getBigDecimal("balance");
            return new Account (id, account.getAccountType(), account.getAccountNumber(), balance, account.getDateOpened(), clientId);
        }
    };

    //GET ALL
    public List<Account> getAccountsByClient (int clientId ) throws SQLException{
      List<Account> accounts = new ArrayList<>();

      try (Connection con = ConnectionUtility.getConnection()) {

          String sql = "SELECT * FROM accounts where client_id =?";

          PreparedStatement pstmt = con.prepareStatement(sql);
          pstmt.setInt(1, clientId);
          ResultSet rs = pstmt.executeQuery();

          while(rs.next()){
              int id = rs.getInt("id");
              String accountType = rs.getString("a_type");
              String accountNumber = rs.getString("a_number");
              BigDecimal balance = rs.getBigDecimal("balance");
              String dateOpened = rs.getString("date_opened");

              accounts.add(new Account(id, accountType, accountNumber, balance, dateOpened, clientId));
          }
      }
        return accounts;
    };

    //GET ONE
    public Account getAccountById (int clientId, int accountId) throws SQLException{
      try (Connection con = ConnectionUtility.getConnection()) {
          String sql = "select * from accounts where client_id = ? and id = ?";
          PreparedStatement pstmt = con.prepareStatement(sql);
          pstmt.setInt(1, clientId);
          pstmt.setInt(2, accountId);

          ResultSet rs = pstmt.executeQuery();

          if(rs.next()){
              String accountType = rs.getString("a_type");
              String accountNumber = rs.getString("a_number");
              BigDecimal balance = rs.getBigDecimal("balance");
              String dateOpened = rs.getString("date_opened");

              return new Account(accountId, accountType, accountNumber, balance, dateOpened, clientId);
          }
      }
        return null;
    };

    //PUT
    public boolean updateAccount(Account account, int clientId, int accountId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "update accounts " +
                    "set " +
                    "balance = ? " +
                    "where client_id= ? " +
                    "and id = ?";
            PreparedStatement pstsm = con.prepareStatement(sql);

            pstsm.setBigDecimal(1, account.getBalance());
            pstsm.setInt(2, clientId);
            pstsm.setInt(3, accountId);

            int numberOfRecordsUpdated = pstsm.executeUpdate();

            if (numberOfRecordsUpdated == 1) {
                return true;
            }
        }
        return false;
    };

    public boolean deleteAccount(int clientId, int accountId) throws SQLException{
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM accounts WHERE client_id = ? and id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, accountId);
            int numberOfRecordsDeleted = pstmt.executeUpdate(); // executeUpdate() is used with INSERT, UPDATE, DELETE

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }
        return false;
    };
}
