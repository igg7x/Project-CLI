
//classpath: ./lib/mysql-connector-java-5.1.48.jar
package JDBC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Entities.ParameterInfo;

public class connDB {

    private static String jdbcString = "jdbc:mysql://localhost:3306/escuelas?useSSL=false";
    private static String user = "root";
    private static String password = "root";
    private static ArrayList<String> parameters = new ArrayList<String>();

    public static Connection conectToDB() {
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcString, user, password);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }

    }

    public static ResultSet executeQuery(String query) throws SQLException {
        Connection conn = conectToDB();
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public static ResultSet executeQueryWithParams(String query, String param) throws SQLException {
        Connection conn = conectToDB();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, param);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public static void loadStoredProcedureParameters(List<ParameterInfo> params) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nIngrese los parámetros de la Stored Procedure: \n");

        for (ParameterInfo parameter : params) {
            System.out.println("Ingrese el parámetro " + parameter.getParameterName() + " de tipo "
                    + parameter.getDataType() + ": ");
            String param = sc.nextLine();
            parameters.add(param);
        }
        System.out.println("Los parámetros cargados de la Stored Procedure son: " + parameters);
        sc.close();

    }

    public static String buildQuery(String spName) {
        String query = "{CALL " + spName + "(";
        for (int i = 0; i < parameters.size(); i++) {
            query += "?,";
        }
        query = query.substring(0, query.length() - 1);
        query += ")}";
        return query;
    }

    public static void callStoredProcedure(String query) throws SQLException {

        CallableStatement callableStatement = conectToDB().prepareCall(query);
        for (int i = 0; i < parameters.size(); i++) {
            callableStatement.setString(i + 1, parameters.get(i));
        }
        callableStatement.execute();
        callableStatement.close();
        conectToDB().close();
        System.out.println("----------------- Stored Procedure ejecutada con éxito ----------------");
    }

}
