package Parking;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField usrNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField pwdField;
    @FXML
    private TextField pwdRepeatField;
    @FXML
    private Button signup_btn;
    @FXML
    private Button login_btn;
    @FXML
    private Label pwdMatch_Label;


    @Override
    public void initialize(URL url, ResourceBundle resources) {

        signup_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!nameField.getText().trim().isEmpty() && !usrNameField.getText().trim().isEmpty() && !emailField.getText().trim().isEmpty() && !phoneField.getText().trim().isEmpty() && !pwdField.getText().trim().isEmpty() && !pwdRepeatField.getText().trim().isEmpty()){
                    if(!pwdField.getText().equals(pwdRepeatField.getText())){
                        System.out.println("passwords don't match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("passwords don't match!");
                        alert.show();
                    }else{
                        System.out.println("SignUp Successful");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("SignUp Successful!");
                        alert.setTitle("Good Job!");
                        alert.show();

                        dbHandler.signUpUser(event, nameField.getText(), usrNameField.getText(), emailField.getText(), Integer.valueOf(phoneField.getText()), pwdField.getText(), pwdRepeatField.getText());
                        nameField.setText("");usrNameField.setText("");emailField.setText("");phoneField.setText("");pwdField.setText("");pwdRepeatField.setText("");
                    }

                }else{
                    System.out.println("Fill in all fields!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Fill in All Fields!");
                    alert.show();
                }
            }
        });

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                dbHandler.changeScene(event, "Dashboard1.fxml","Log In!",null);
            }
        });

    }
}
