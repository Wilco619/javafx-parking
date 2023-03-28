package Parking;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class dbHandler {

    static void changeScene(ActionEvent event, String fxmlFile, String title, String usrName) {
        Parent root = null;

        if (usrName != null){
            try{
                FXMLLoader loader = new FXMLLoader(dbHandler.class.getResource(fxmlFile));
                root = loader.load();
                UsrDashboardController usrDashboardController = loader.getController();
                usrDashboardController.setUserInformation(usrName);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try{
                root = FXMLLoader.load(dbHandler.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();

    }
    static void changeScene1(ActionEvent event, String fxmlFile, String title, String usrName, Integer phone, String admin_id) {
        Parent root = null;

        if (usrName != null && phone != null && admin_id != null){
            try{
                FXMLLoader loader = new FXMLLoader(dbHandler.class.getResource(fxmlFile));
                root = loader.load();
                AdminController adminController = loader.getController();
                adminController.setUserName(usrName);
                adminController.setUserPhone(phone);
                adminController.setUserId(admin_id);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try{
                root = FXMLLoader.load(dbHandler.class.getResource(fxmlFile));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();

    }

    public static Connection getConnection() {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            return connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void signUpUser(ActionEvent event, String name, String usrName, String email, Integer phone, String password, String passwordRepeat) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM signUp WHERE usrName = ? AND email = ? AND name = ? AND phone = ?");
            psCheckUserExists.setString(1,usrName);
            psCheckUserExists.setString(2,email);
            psCheckUserExists.setString(3,name);
            psCheckUserExists.setInt(4,phone);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                System.out.println("User Already Exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please Use a different User Name");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT INTO signUp (name, usrName, email, phone, password, passwordRepeat) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1,name);
                psInsert.setString(2,usrName);
                psInsert.setString(3,email);
                psInsert.setInt(4,phone);
                psInsert.setString(5,password);
                psInsert.setString(6,passwordRepeat);
                psInsert.executeUpdate();

//                changeScene(event, "UsrDashboard.fxml", "Welcome", usrName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void logInUser(ActionEvent event, String usrName, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            preparedStatement = connection.prepareStatement("SELECT password, email FROM signUp WHERE usrName = ?");
            preparedStatement.setString(1,usrName);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                System.out.println("User not Found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect UserName Or Password!");
                alert.show();
            }else{
                while(resultSet.next()){
                    String retrievePassword = resultSet.getString("password");
                    if (retrievePassword.equals(password)){
                        changeScene(event, "UsrDashboard.fxml","Welcome!", usrName);
                    }else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect UserName Or Password!");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            connection.close();
        }
    }

    public static void adminSignup(ActionEvent event,String admin_id,String name, String usrName, String email, Integer phone, String pwd, String pwdRepeat) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheck_admin = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            psCheck_admin = connection.prepareStatement("SELECT * FROM admin_login WHERE usrName = ? AND email = ?");
            psCheck_admin.setString(1,usrName);
            psCheck_admin.setString(2,email);
            resultSet = psCheck_admin.executeQuery();


            if (resultSet.isBeforeFirst()){
                System.out.println("Admin already exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Admin exists!");
                alert.show();
            }else{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
                psInsert = connection.prepareStatement("INSERT INTO admin_login (admin_id ,name ,usrName ,email ,phone ,pwd ,pwdRepeat) VALUES (? ,? ,? ,? ,? ,? ,?)");
                psInsert.setString(1,admin_id);
                psInsert.setString(2,name);
                psInsert.setString(3,usrName);
                psInsert.setString(4,email);
                psInsert.setInt(5,phone);
                psInsert.setString(6,pwd);
                psInsert.setString(7,pwdRepeat);
                psInsert.executeUpdate();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheck_admin != null){
                try{
                    psCheck_admin.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            connection.close();
        }

    }
    public static void adminLogin(ActionEvent event, String admin_id, String usrName, String pwd, Integer phone) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            preparedStatement = connection.prepareStatement("SELECT pwd, admin_id FROM admin_login WHERE usrName = ?");
            preparedStatement.setString(1,usrName);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()){
                System.out.println("Admin not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong Credentials!");
                alert.show();
            }else{
                while(resultSet.next()){
                    String a_id = resultSet.getString("admin_id");
                    String retrieve_pwd = resultSet.getString("pwd");
                    if(retrieve_pwd.equals(pwd) && a_id.equals(admin_id)){
                        changeScene1(event,"Admin.fxml","Admin",usrName,phone,admin_id);
                    }else{
                        System.out.println("passwords or ids don't match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Wrong Credentials!");
                        alert.show();
                    }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            connection.close();
        }
    }

    //adding slot
    public static void addSlot(ActionEvent event, String slot_id, String slot_location) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp","root","");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM added_slot WHERE slot_id = ?");
            psCheckUserExists.setString(1,slot_id);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()){
                System.out.println("Slot Exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please Add New Slot");
                alert.show();
            }else{
                psInsert = connection.prepareStatement("INSERT INTO added_slot (slot_id,slot_location) VALUES (?, ?)");
                psInsert.setString(1,slot_id);
                psInsert.setString(2,slot_location);
                psInsert.executeUpdate();

                System.out.println("Slot Added!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Slot Added");
                alert.show();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null){
                try{
                    psCheckUserExists.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            connection.close();
        }
    }
    //selecting parking space
    public static void selectSlot(ActionEvent event, String slot_id, String reg_No, String v_category) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp", "root", "");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM vehicle WHERE slot_id = ? AND reg_No = ?");
            psCheckUserExists.setString(1, slot_id);
            psCheckUserExists.setString(2, reg_No);
            resultSet = psCheckUserExists.executeQuery();
//
            if (resultSet.isBeforeFirst()) {
                System.out.println("Slot Exists!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Payment successful!");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO vehicle (slot_id,reg_No,v_category) VALUES (?, ?, ?)");
                psInsert.setString(1, slot_id);
                psInsert.setString(2, reg_No);
                psInsert.setString(3, v_category);
                psInsert.executeUpdate();

                System.out.println("slot picked!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("you picked a slot!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();
        }
    }
    // deleting selected slots
    public static void deleteSlot(ActionEvent event, String slot_id) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp", "root", "");
            psCheckUserExists = connection.prepareStatement("Select * FROM Picked_slot INNER JOIN vehicle ON Picked_slot.slot_id = vehicle.slot_id WHERE vehicle.slot_id = ?");
            psCheckUserExists.setString(1, slot_id);
            resultSet=psCheckUserExists.executeQuery();
//
            if (resultSet.isBeforeFirst()) {
                System.out.println("Slot Not in Booking pane!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please Enter Viable Slot!");
                alert.show();
            } else {
                String delete = "Delete FROM vehicle WHERE slot_id = ?";
                String select = "select slot_id,location from ";
                psInsert = connection.prepareStatement(delete);
                psInsert.setString(1,slot_id);
                psInsert.executeUpdate();
                System.out.println("Slot Removed successfully!");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Slot removed successfully!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            connection.close();
        }
    }
    // delete slot from free list
    public static void deleteFreeSlot(ActionEvent event, String slot_id) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PsignUp", "root", "");
            psCheckUserExists = connection.prepareStatement("Select * FROM added_slot INNER JOIN vehicle ON added_slot.slot_id = vehicle.slot_id WHERE vehicle.slot_id = ?");
            psCheckUserExists.setString(1, slot_id);
            resultSet=psCheckUserExists.executeQuery();
//
            if (resultSet.isBeforeFirst()) {
                System.out.println("Slot Not in Free Section!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Slot not found!");
                alert.show();
            } else {
                userTablesController utc =new userTablesController();
                String location = (String) utc.executeQuery("select slot_Location from added_slot where slot_Id='"+slot_id+"'");
                utc.executeQuery("Delete FROM vehicle WHERE slot_id ='"+slot_id+"'");
                utc.executeQuery("insert into added_slots(slot_Id,slot_Location)values('"+slot_id+"','"+location+"')");


                System.out.println("Slot Removed successfully!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Slot removed successfully!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}