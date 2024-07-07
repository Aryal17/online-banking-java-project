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

    }
}