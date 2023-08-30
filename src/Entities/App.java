package Entities;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import JDBC.connDB;

public class App {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        try {
            connDB.conectToDB();
            System.out.println("--------------Conexión exitosa------------------");

            System.out.println("DATABASE name : ");

            String database = sc.nextLine();

            ResultSet sp = connDB.executeQueryWithParams(
                    "SELECT ROUTINE_TYPE, ROUTINE_NAME FROM information_schema.ROUTINES WHERE ROUTINE_SCHEMA = ?  order by ROUTINE_NAME  asc",
                    database);
            // Imprime todas las Stored Procedures de la base de datos

            if (sp != null) {
                while (sp.next()) {
                    System.out.println(
                            "TYPE : " + sp.getString("ROUTINE_TYPE") + "   ROUTINE_NAME : "
                                    + sp.getString("ROUTINE_NAME"));
                }
            }

            System.out.println("Ingrese el nombre de la Stored Procedure que desea ejecutar: ");
            String spName = sc.nextLine();

            ResultSet spFeatures = connDB.executeQueryWithParams(
                    "SELECT * FROM information_schema.PARAMETERS WHERE SPECIFIC_SCHEMA = 'escuelas' AND SPECIFIC_NAME = ? ",
                    spName);

            List<ParameterInfo> parametersInfoList = new ArrayList<>();
            // Imprime la metadata del Stored Procedure
            if (spFeatures.next()) {
                System.out.println("Las características del siguiente Stored Procedure son:\n");
                do {

                    String parameterName = spFeatures.getString("PARAMETER_NAME");
                    String dataType = spFeatures.getString("DATA_TYPE");

                    System.out.println("\tPARAMETER_NAME : " +
                            parameterName + " DATA_TYPE : "
                            + dataType + " PARAMETER_MODE : "
                            + spFeatures.getString("PARAMETER_MODE"));

                    parametersInfoList.add(new ParameterInfo(parameterName, dataType));

                } while (spFeatures.next());
                connDB.loadStoredProcedureParameters(parametersInfoList);
                String procedureCallQuery = connDB.buildQuery(spName);
                connDB.callStoredProcedure(procedureCallQuery);
            } else {
                System.out.println("El Stored Procedure no existe");
            }

        } catch (Exception e) {
            throw new Error(e.getMessage());
        } finally {
            sc.close();
            connDB.conectToDB().close();
        }
    }
}
