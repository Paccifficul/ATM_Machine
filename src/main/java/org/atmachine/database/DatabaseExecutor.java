package org.atmachine.database;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.atmachine.bank.BankAccountModel;

public class DatabaseExecutor {
    private static DatabaseExecutor instance = new DatabaseExecutor();

    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static final String TABLE_NAME = "bank_account";
    private Connection connection;
    private Statement statement;

    public static DatabaseExecutor getInstance() {
        return instance;
    }

    private DatabaseExecutor() {
        if (instance == null) instance = this;
        else {
            System.out.println("Object is already exists.");
        }
    }

    public BankAccountModel getDataFromDb(String accountNumber, short passcode) {
        try {
            String sqlExec = "select * from " + TABLE_NAME +
                    " where number = " + accountNumber +
                    " AND passcode = " + passcode;

            ResultSet resultSet = statement.executeQuery(sqlExec);

            if (resultSet.getInt("id") == 0) {
                throw new SQLException();
            }

            return new BankAccountModel(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getDouble("amount"),
                    Long.parseLong(accountNumber),
                    passcode,
                    resultSet.getString("payment_system")
            );
        } catch (SQLException ex) {
            return null;
        }
    }

    public void updateAmountInDb(String accountNumber, short passcode, double newAmount) {
        try {
            String sqlExec = "update " + TABLE_NAME +
                    " set amount = " + newAmount +
                    " where number = " + accountNumber +
                    " AND passcode = " + passcode;

            statement.executeUpdate(sqlExec);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean openConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
