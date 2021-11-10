package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInFormController {

    public AnchorPane logInContext;
    public JFXComboBox<String> cmbUserType;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Label lblText;
    public JFXButton btnSignUp;

    public void initialize() {
        cmbUserType.getItems().addAll("Cashier", "Admin");
        try {
            ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `UserDetail`").executeQuery();
            while (rst.next()){
                if(rst.getString(3).equals("Admin")){
                    btnSignUp.setDisable(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openSignUpForm(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/SignUpForm.fxml");
        Parent load = FXMLLoader.load(resource);
        logInContext.getChildren().clear();
        logInContext.getChildren().add(load);
    }

    public void openDashBoardFormOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        try {
            String uType = cmbUserType.getSelectionModel().getSelectedItem();
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `UserDetail` WHERE userType=?");
            stm.setObject(1, uType);
            ResultSet rst = stm.executeQuery();
            if (!txtUserName.getText().equals("") && !txtPassword.getText().equals("") && !cmbUserType.getSelectionModel().getSelectedItem().equals("null")) {
                while (rst.next()) {
                    if (cmbUserType.getSelectionModel().getSelectedItem().equalsIgnoreCase("Cashier")) {
                        if (rst.getString(3).equals(cmbUserType.getSelectionModel().getSelectedItem())) {
                            if (txtPassword.getText().equals(rst.getString(5)) && txtUserName.getText().equals(rst.getString(4))) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Log in successful.", ButtonType.CLOSE).showAndWait();
                                Stage stg = (Stage) logInContext.getScene().getWindow();
                                stg.close();
                                URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
                                Parent load = FXMLLoader.load(resource);
                                Scene scene = new Scene(load);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle("Cashier DashBoard");
                                stage.show();
                                return;
                            }
                        }
                    }

                    if (cmbUserType.getSelectionModel().getSelectedItem().equalsIgnoreCase("Admin")) {
                        if (rst.getString(3).equals(cmbUserType.getSelectionModel().getSelectedItem())) {
                            if (txtPassword.getText().equals(rst.getString(5)) && txtUserName.getText().equals(rst.getString(4))) {
                                new Alert(Alert.AlertType.CONFIRMATION, "Log in successful.", ButtonType.CLOSE).showAndWait();
                                Stage stg = (Stage) logInContext.getScene().getWindow();
                                stg.close();
                                URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                                Parent load = FXMLLoader.load(resource);
                                Scene scene = new Scene(load);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle("Admin DashBoard");
                                stage.show();
                                return;
                            }
                        }
                    }

                }
                new Alert(Alert.AlertType.ERROR,"Username , password or usertype incorrect!",ButtonType.CANCEL).showAndWait();
            } else {
                new Alert(Alert.AlertType.WARNING, "Please Fill all the fields.", ButtonType.CLOSE).show();
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING, "Please Fill all the fields.", ButtonType.CLOSE).show();
        }
    }

    public void moveToPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }
}
