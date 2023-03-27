package Parking;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Admin_loginController implements Initializable {

    @FXML
    private Button admin_login;

    @FXML
    private Button exit_btn;
    @FXML
    private TextField idField;
    @FXML
    private TextField usrNameField;
    @FXML
    private PasswordField pwdField;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.adminLogin(event, idField.getText(),usrNameField.getText(),pwdField.getText(),null);
                idField.setText("");usrNameField.setText("");pwdField.setText("");
            }
        });

        exit_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene1(event,"Dashboard1.fxml","LogIn",null,null,null);
            }
        });
    }


}
