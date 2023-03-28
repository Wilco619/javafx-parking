package Parking.models;

import Parking.AdminTable;
import Parking.PrintReciept;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminDbConfig {
    public static String url="jdbc:mysql://localhost:3306/PsignUp";
    public static  String password ="";
    public static String username="root";
    public static Object executeQuery(String query,boolean... t) throws SQLException {

        Connection connection;
        Statement statement;
        ResultSet resultSet;
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
        if(query.toLowerCase().startsWith("select")){
            try{
                resultSet = statement.executeQuery(query);
                if(query.contains("SELECT DATE(date_) as day, AVG(amount) as average FROM payments")){
                    return  barGraph(resultSet);
                }
                if (query.contains("select slot_location from Picked_slot where slot_Id=")) {
                    System.out.println(query);
                    while(resultSet.next()){
                        String loc = resultSet.getString("slot_location");
                        System.out.println(loc);
                        return loc;
                    }
                }
                if (query.contains("SELECT MONTH(date_) as day, AVG(amount)")) {
                    return  areaGraph(resultSet);
                }
                if (query.contains("select * from vehicle")) {
                    return  vehicleTable(resultSet);
                }
                if (query.contains("SELECT * FROM Picked_slot")) {
                    return  printReciept(resultSet);
                }
                connection.close();
            }catch (Exception e){
                System.out.println(e);
            }finally {
                if(connection != null){
                    try{
                        connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if(statement != null){
                    try{
                        statement.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        }else {
            try{
                int row= statement.executeUpdate(query);
                System.out.println(row);
            }catch (Exception e){
                System.out.println(e);
            }finally {
                if(connection != null){
                    try{
                        connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if(statement != null){
                    try{
                        statement.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        connection.close();
        return "null";
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(executeQuery("SELECT * FROM vehicle"));
    }
    private static Object printReciept(ResultSet resultSet) throws SQLException {
        ArrayList<String> arr =  new ArrayList<String>();
        LocalTime time =LocalTime.now();
        String amount = "90";
        while(resultSet.next()){
            String reg_number = resultSet.getString("reg_No");
            String slot_id = resultSet.getString("slot_id");
            String time_in =resultSet.getString("time_in");
            String cat = resultSet.getString("v_category");
            arr.add(Arrays.toString(new String[]{reg_number, slot_id, time_in, cat}));
            System.out.println(slot_id);
        }

        return arr;
    }

    private static ObservableList<AdminTable> vehicleTable(ResultSet resultSet) throws  Exception{
        ArrayList<String> arr =  new ArrayList<String>();
        ObservableList<AdminTable> tablelist = FXCollections.observableArrayList();
        LocalTime time =LocalTime.now();
        String amount = "90";

        while(resultSet.next()){
            String reg_number = resultSet.getString("reg_No");
            String slot_id = resultSet.getString("slot_id");
            String time_in =resultSet.getString("time_in");
            String cat = resultSet.getString("v_category");
            AdminTable table = new AdminTable(reg_number,slot_id,time_in,String.valueOf(time),amount);
            tablelist.add(table);
        }
        return tablelist;
    }
    public void Reciept(ResultSet resultSet){
        ArrayList<String> arr =  new ArrayList<String>();
        ObservableList<AdminTable> tablelist = FXCollections.observableArrayList();
        LocalTime time =LocalTime.now();
        String amount = "90";

    }
    private static Object areaGraph(ResultSet resultSet) throws SQLException {
        ArrayList<Integer> amounts = new ArrayList<Integer>();
        while (resultSet.next()){
            int average = resultSet.getInt("average");
            amounts.add(average);
        }
        return  amounts;
    }
    private static Object barGraph(ResultSet resultSet) throws SQLException {
        ArrayList<Integer> amounts = new ArrayList<Integer>();
        while(resultSet.next()){
            int average= resultSet.getInt("average");
            System.out.println(average);
            amounts.add(average);
        }
        return amounts;
    }
}
