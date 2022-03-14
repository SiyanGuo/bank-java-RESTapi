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
            String sqlInsert = "INSERT INTO accounts (a_type, a_number, date_opened, client_id) " +
                    "VALUES ( ?, nextval('account_number'), ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sqlInsert);

            pstmt.setString(1, account.getAccountType());
            pstmt.setDate(2, Date.valueOf(account.getDateOpened()));
            pstmt.setInt(3, clientId);
            pstmt.executeUpdate();

            String sqlSelect = "select * from accounts where client_id = ? and id = (select max(id) from accounts where client_id = ?)";
            PreparedStatement ps = con.prepareStatement(sqlSelect);
            ps.setInt(1, clientId);
            ps.setInt(2, clientId);

            ResultSet rs = ps.executeQuery();

            rs.next();
            int id = rs.getInt("id");
            String accountType = rs.getString("a_type");
            BigDecimal balance = rs.getBigDecimal("balance");
            int accountNumber = rs.getInt("a_number");
            String dateOpened = rs.getString("date_opened");

            return new Account (id, accountType ,accountNumber, balance, dateOpened, clientId);
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
              int accountNumber = rs.getInt("a_number");
              BigDecimal balance = rs.getBigDecimal("balance");
              String dateOpened = rs.getString("date_opened");

              accounts.add(new Account(id, accountType, accountNumber, balance, dateOpened, clientId));
          }
      }
        return accounts;
    };

    //GET ACCOUNTS BY PATH PARAMS
    public List<Account> getAccountsByGreaterThan (int clientId, int amountGreater ) throws SQLException{
        List<Account> accounts = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "SELECT * FROM accounts where client_id =? and balance > ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, amountGreater);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) { return accounts;}

            do {
                int id = rs.getInt("id");
                String accountType = rs.getString("a_type");
                int accountNumber = rs.getInt("a_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                String dateOpened = rs.getString("date_opened");

                accounts.add(new Account(id, accountType, accountNumber, balance, dateOpened, clientId));
            }while(rs.next());
        }
        return accounts;
    };

    public List<Account> getAccountsByLessThan (int clientId, int amountLess ) throws SQLException{
        List<Account> accounts = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "SELECT * FROM accounts where client_id =? and balance < ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, amountLess);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) { return accounts;}
            do {
                int id = rs.getInt("id");
                String accountType = rs.getString("a_type");
                int accountNumber = rs.getInt("a_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                String dateOpened = rs.getString("date_opened");

                accounts.add(new Account(id, accountType, accountNumber, balance, dateOpened, clientId));
            } while(rs.next());
        }
        return accounts;
    };

    public List<Account> getAccountsByGreatAndLessThan (int clientId, int min, int max ) throws SQLException{
        List<Account> accounts = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "SELECT * FROM accounts where client_id =? and balance > ? and balance < ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, min);
            pstmt.setInt(3, max);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) { return accounts;}

            do {
                int id = rs.getInt("id");
                String accountType = rs.getString("a_type");
                int accountNumber = rs.getInt("a_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                String dateOpened = rs.getString("date_opened");

                accounts.add(new Account(id, accountType, accountNumber, balance, dateOpened, clientId));
            } while(rs.next());
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
              int accountNumber = rs.getInt("a_number");
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

    //DELETE
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
