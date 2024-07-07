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


public class AccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        try{
            connection = DatabaseConnection.getConnection();
            PreparedStatement st = connection.prepareStatement("select * from accounts");
            ResultSet rs = st.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while(rs.next()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", rs.getInt("id"));
                jsonObject.put("accountNumber", rs.getString("accountNumber"));
                jsonObject.put("balance", rs.getDouble("balance"));
                jsonObject.put("owner", rs.getString("owner"));
                jsonArray.put(jsonObject);
            }
            out.print(jsonArray.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String accountNumber = request.getParameter("accountNumber");
        String owner = request.getParameter("owner");
        String  balance = request.getParameter("balance");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts(accountNumber,owner,balance) VALUES(?,?,?)");
            statement.setString(1, accountNumber);
            statement.setString(2, owner);
            statement.setDouble(3, Double.valueOf(balance));

            int insertData = statement.executeUpdate();
            if (insertData == 1) {
                out.println("account_created_successfully");
            } else {
                out.println("error_while_creating_account");
            }
        }catch (SQLException e){
            e.printStackTrace();
//            out.println(e.getMessage());

        }catch (Exception e){

            e.printStackTrace();
//            out.println(e.getMessage());

        }finally {
            DatabaseConnection.closeConnection(connection);
        }

    }
}