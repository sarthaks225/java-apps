package com.hr.dl.dao;
import com.hr.dl.exceptions.*;
import java.sql.*;
public class ConnectionDAO
{

    private ConnectionDAO()
    {
    }

    public static Connection getConnection() throws DAOException
    {
        Connection connection=null;
    try
    {
       
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","sarthak","");
    }catch(Exception e)
    {
        throw new DAOException(e.getMessage());
    }

        return connection;
    }

}