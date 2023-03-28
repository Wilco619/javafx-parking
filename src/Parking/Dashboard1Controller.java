package Parking;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Dashboard1Controller implements Initializable {

    @FXML
    private TextField loginUsrName;

    @FXML
    private PasswordField loginPwd;

    @FXML
    private Button admin_btn;

    @FXML
    private Label labelForgot;

    @FXML
    private Button logIn_btn;
    @FXML
    private Button signUp_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logIn_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    dbHandler.logInUser(event, loginUsrName.getText(), loginPwd.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                loginUsrName.setText("");loginPwd.setText("");
            }
        });
        signUp_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event, "SignUp.fxml","Sign Up!",null);
            }
        });
        admin_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event,"admin_login.fxml","Login",null);
            }
        });

    }
}