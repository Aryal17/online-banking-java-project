package com.prashantaryal.onlinebankingsystem.servlets;

import com.prashantaryal.onlinebankingsystem.classes.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TransferServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT t.id, t.amount, t.account_id_from, t.account_id_to, " +
                    "a_from.accountNumber AS fromAccountNumber, a_from.balance AS fromAccountBalance, a_from.owner AS fromAccountOwner, " +
                    "a_to.accountNumber AS toAccountNumber, a_to.balance AS toAccountBalance, a_to.owner AS toAccountOwner " +
                    "FROM transactions t " +
                    "JOIN accounts a_from ON t.account_id_from = a_from.id " +
                    "JOIN accounts a_to ON t.account_id_to = a_to.id "+
                    "ORDER BY t.id ASC";;
            PreparedStatement st = connection.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("transactionId", rs.getInt("id"));
                jsonObject.put("amount", rs.getDouble("amount"));

                JSONObject fromAccount = new JSONObject();
                fromAccount.put("account_id_from", rs.getString("account_id_from"));
                fromAccount.put("accountNumber", rs.getString("fromAccountNumber"));
                fromAccount.put("balance", rs.getDouble("fromAccountBalance"));
                fromAccount.put("owner", rs.getString("fromAccountOwner"));
                jsonObject.put("fromAccount", fromAccount);

                JSONObject toAccount = new JSONObject();
                toAccount.put("account_id_to", rs.getString("account_id_to"));
                toAccount.put("accountNumber", rs.getString("toAccountNumber"));
                toAccount.put("balance", rs.getDouble("toAccountBalance"));
                toAccount.put("owner", rs.getString("toAccountOwner"));
                jsonObject.put("toAccount", toAccount);

                jsonArray.put(jsonObject);
            }
            out.print(jsonArray.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fromAccount = request.getParameter("account_id_from");
        String toAccount = request.getParameter("account_id_to");
        String amount = request.getParameter("amount");
        PrintWriter out = response.getWriter();
        Connection connection = null;

        try {
            // Parse the input parameters
            int fromAccountId = Integer.parseInt(fromAccount);
            int toAccountId = Integer.parseInt(toAccount);
            double transactionAmount = Double.parseDouble(amount);

            if (transactionAmount <= 0) {
                out.println("amount_must_be_positive");
                return;
            }

            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Begin transaction

            // Check if the fromAccount has enough balance
            PreparedStatement checkBalanceStmt = connection.prepareStatement("SELECT balance FROM accounts WHERE id = ?");
            checkBalanceStmt.setInt(1, fromAccountId);
            ResultSet balanceRs = checkBalanceStmt.executeQuery();
            if (!balanceRs.next()) {
                out.println("from_account_does_not_exist");
                connection.rollback();
                return;
            }
            double fromAccountBalance = balanceRs.getDouble("balance");

            if (fromAccountBalance < transactionAmount) {
                out.println("insufficient_funds");
                connection.rollback();
                return;
            }

            // Insert transaction record
            PreparedStatement insertTransaction = connection.prepareStatement("INSERT INTO transactions(account_id_from, account_id_to, amount) VALUES (?, ?, ?)");
            insertTransaction.setInt(1, fromAccountId);
            insertTransaction.setInt(2, toAccountId);
            insertTransaction.setDouble(3, transactionAmount);
            int insertData = insertTransaction.executeUpdate();

            if (insertData == 1) {
                // Update balance of fromAccount
                PreparedStatement updateFromAccount = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
                updateFromAccount.setDouble(1, transactionAmount);
                updateFromAccount.setInt(2, fromAccountId);
                int updateFromResult = updateFromAccount.executeUpdate();

                // Update balance of toAccount
                PreparedStatement updateToAccount = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
                updateToAccount.setDouble(1, transactionAmount);
                updateToAccount.setInt(2, toAccountId);
                int updateToResult = updateToAccount.executeUpdate();

                if (updateFromResult == 1 && updateToResult == 1) {
                    connection.commit(); // Commit transaction
                    out.println("transaction_successful");
                } else {
                    connection.rollback(); // Rollback transaction if any update fails
                    out.println("error_while_processing_transaction");
                }
            } else {
                connection.rollback(); // Rollback transaction if insert fails
                out.println("error_while_creating_transaction");
            }
        } catch (NumberFormatException e) {
            out.println("invalid_input_format");
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback transaction in case of any error
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            out.println("error_while_processing_transaction");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("error_while_processing_transaction");
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Restore default auto-commit behavior
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }


}