package Parking;
import java.sql.*;
import java.util.ArrayList;

public class userTablesController {

        public static String url="jdbc:mysql://localhost:3306/PsignUp";
        public static  String password ="";
        public static String username="root";

    private static Object Location_(ResultSet resultSet) throws SQLException {
        String location = null;
        while (resultSet.next()) {
            location = resultSet.getString("slot_Location");
        }
        System.out.println(location);
        return location;
    }
































    private static Object delete(ResultSet resultSet) throws SQLException {
        String location = null;
        while (resultSet.next()) {
            location = resultSet.getString("slot_Location");
        }
        return location;
    }








        public static Object executeQuery(String query,boolean... t) throws SQLException {
            Connection connection;
            Statement statement;
            ResultSet resultSet;
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            if(query.toLowerCase().startsWith("select")){
                try{
                    resultSet = statement.executeQuery(query);
                    if (query.contains("select slot_Location from added_slot where slot_Id=")){
                        return Location_(resultSet);
                    }
                    if (query.contains("select * from Picked_slot where slot_Id='")){
                        return Alldata(resultSet);
                    }
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
//150 time
    private static Object Alldata(ResultSet resultSet) throws SQLException {
        System.out.println("ddddd");
        ArrayList<String> vals = new ArrayList<>();
        while(resultSet.next()){
            System.out.println("aaaaaaaa");
            String slot_id = resultSet.getString("slot_Id");
            String location =resultSet.getString("slot_Location");
            vals.add(slot_id);
            vals.add(location);
        }
        return vals;
    }


}


