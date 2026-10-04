package org.example.bankappspring.repository;

import org.example.bankappspring.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class AccountRepo {
  @Autowired
  DataSource dataSource;

  public ArrayList<Account> getAllAccounts() {
    ArrayList<Account> list = new ArrayList<>();
    String sql = "SELECT * FROM account";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        int accNo = rs.getInt("accountNo");
        String owner = rs.getString("owner");
        double balance = rs.getDouble("balance");
        list.add(new Account(accNo, owner, balance));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public Account getAccount(int accountNo) {
    Account acc=null;
    String sql = "SELECT * FROM account WHERE accountNo=?";
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setInt(1, accountNo);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int accNo = rs.getInt("accountNo");
        String owner = rs.getString("owner");
        double balance = rs.getDouble("balance");
        acc=new Account(accNo, owner, balance);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return acc;
  }

  public void deposit(int accountNo, double amount){
    String sql="UPDATE account SET balance=balance+? WHERE accountNo=?;";
    System.out.println(sql);
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setDouble(1, amount);
      ps.setInt(2, accountNo);
      ps.executeUpdate();
    } catch (SQLException e) { e.printStackTrace(); }
  }

  public void withdraw(int accountNo, double amount) throws Exception {
    try (Connection con = dataSource.getConnection()) { // auto close con
      try {
        // Start transaction
        con.setAutoCommit(false);
        System.out.println("withdraw(): autocommit="+con.getAutoCommit());

        // Get balance and Lock row (FOR UPDATE)
        String sql1="SELECT balance FROM account WHERE accountNo=? FOR UPDATE;";
        System.out.println(sql1);
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setInt(1, accountNo);
        ResultSet rs = ps1.executeQuery();
        if (rs.next()) {
          int balance= rs.getInt("balance");
          if (balance<amount) throw new Exception("Withdraw failed");
        } else throw new Exception("Withdraw failed");

        // Update row
        String sql2="UPDATE account SET balance=balance-? WHERE accountNo=?;";
        System.out.println(sql2);
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setDouble(1, amount);
        ps2.setInt(2, accountNo);
        ps2.executeUpdate();

        // Commit transaction and release lock
        System.out.println("withdraw(): commit");
        con.commit();

      } catch (Exception e) {
        // Rollback on exceptions
        System.out.println("withdraw(): rollback");
        if (con!=null) con.rollback();
        throw e; // rethrow ex - for error handling in controller

      } finally {
        // Reset autocommit
        System.out.println("withdraw(): reset autocommit");
        if (con!=null) con.setAutoCommit(true);
      }
    } // Automatic close connection
  }

  public void transfer(int accountNoFrom, int accountNoTo, double amount)
    throws Exception {
    String sql1="UPDATE account SET balance=balance-? WHERE accountNo=?;";
    String sql2="UPDATE account SET balance=balance+? WHERE accountNo=?;";
    try ( Connection con = dataSource.getConnection()){
      try {
        // Start transaction
        con.setAutoCommit(false);
        System.out.println("transfer(): autocommit="+con.getAutoCommit());

        // Withdraw amount
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setDouble(1, amount);
        ps1.setInt(2, accountNoFrom);
        if (ps1.executeUpdate()==0) throw new Exception("Transfer failed");

        // Provoke exception in transfers from account 2
        if (accountNoFrom==2) throw new Exception("Transfer failed");

        // Deposit amount
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setDouble(1, amount);
        ps2.setInt(2, accountNoTo);
        if (ps2.executeUpdate()==0) throw new Exception("Transfer failed");

        // Commit transaction
        System.out.println("transfer(): commit");
        con.commit();

      } catch (Exception e) {
        // Rollback on exceptions
        System.out.println("transfer(): rollback");
        if (con!=null) con.rollback();
        throw e; // rethrow ex - for error handling in controller

      } finally {
        // Reset autocommit
        System.out.println("transfer(): reset autocommit");
        if (con!=null) con.setAutoCommit(true);
      }
    } // Automatic close connection
  }
}