package com.example.demo;

import java.sql.*;
import java.util.Map;

public class DB {

    private Connection dbConn = null;

    private Connection getDBConn() throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("DB_HOST", "172.20.24.132");
        String port = env.getOrDefault("DB_PORT", "3306");
        String database = env.getOrDefault("DB_NAME", "public");
        String user = env.getOrDefault("DB_USER", "admin");
        String password = env.getOrDefault("DB_HOST", "123321");

        String connectionURL = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC",

                host,
                port,
                database);

        Class.forName("com.mysql.cj.jdbc.Driver");
        

        if (dbConn == null){
            dbConn = DriverManager.getConnection(
                    connectionURL,
                    user,
                    password
            );
        }

        return dbConn;
    }

    public Integer getSotrydnic(String number) throws SQLException, ClassNotFoundException {
        String sql ="SELECT count(*) FROM Sotrydnic WHERE login='"+number+"';";
        Statement statement = getDBConn().createStatement();
        ResultSet result = statement.executeQuery(sql);
        Integer vxod = 0;
        while (result.next()){
            vxod = result.getInt(1);
        }
        return vxod;
    }

    public String getPassword(String number) throws SQLException, ClassNotFoundException {
        String sql ="SELECT Sotrydnic.password FROM Sotrydnic where login='"+number+"'";
        Statement statement = getDBConn().createStatement();
        ResultSet result = statement.executeQuery(sql);
        String vxod = "";
        while (result.next()){
            vxod = result.getString(1);
        }
        return vxod;
    }

    public String getRole(String number) throws SQLException, ClassNotFoundException {
        String sql ="SELECT RoleSotrydnic.Name FROM user02.Sotrydnic, user02.RoleSotrydnic where RoleSotrydnic.RoleSotrydnicID =  Sotrydnic.role and Sotrydnic.login = '"+number+"'";
        Statement statement = getDBConn().createStatement();
        ResultSet result = statement.executeQuery(sql);
        String vxod = "";
        while (result.next()){
            vxod = result.getString(1);
        }
        return vxod;
    }

}
