package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class SignUpFormController {

    public AnchorPane signUpFormContext;
    public JFXTextField txtFirstName;
    public JFXTextField txtLastName;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXTextField txtConfirmPassword;
    public JFXComboBox<String> cmbUserType;

    public void initialize(){
        cmbUserType.getItems().addAll("Admin");
    }

    public void backToLoginForm(MouseEvent mouseEvent) throws IOException {
        URL resource = getClass().getResource("../view/LogInForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage)signUpFormContext.getScene().getWindow();
        window.setScene(new Scene(load));
    }

    public void signUpUsersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(txtPassword.getText().equals(txtConfirmPassword.getText())) {
            if(!txtFirstName.getText().equals("") && !txtLastName.getText().equals("") && !txtUserName.getText().equals("") && !txtPassword.getText().equals("") && !txtConfirmPassword.getText().equals("") && !cmbUserType.getSelectionModel().getSelectedItem().equalsIgnoreCase("null")) {
                User u1 = new User(txtFirstName.getText(), txtLastName.getText(), cmbUserType.getSelectionModel().getSelectedItem(), txtUserName.getText(), txtPassword.getText());
                if (new UserController().signupUser(u1)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Registration Successful", ButtonType.CLOSE).showAndWait();
                    txtFirstName.clear();
                    txtLastName.clear();
                    txtUserName.clear();
                    txtPassword.clear();
                    txtConfirmPassword.clear();
                    cmbUserType.getSelectionModel().clearSelection();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again", ButtonType.CLOSE).showAndWait();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Please fill all the fields",ButtonType.CLOSE).showAndWait();
                return;
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Password is not matched.",ButtonType.CLOSE).showAndWait();
        }
    }

    public void moveToLastName(ActionEvent actionEvent) {
        txtLastName.requestFocus();
    }

    public void moveToUserName(ActionEvent actionEvent) {
        txtUserName.requestFocus();
    }

    public void moveToSignupPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void moveToConfirmPassword(ActionEvent actionEvent) {
        txtConfirmPassword.requestFocus();
    }
}
