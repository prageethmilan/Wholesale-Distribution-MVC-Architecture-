package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import view.tm.CustomerTM;
import view.tm.ItemTM;
import view.tm.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class AdminDashBoardFormController {

    public Label lblDate;
    public Label lblTime;
    public AnchorPane adminDashBoardContext;
    public JFXTextField txtItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtQuantityOnHand;
    public JFXTextField txtDiscount;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtSearchCode;
    public JFXButton btnAddItem;
    public TableView<ItemTM> tblItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colQuantity;
    public TableColumn colDiscount;
    public TableColumn colUnitPrice;
    public JFXTextField txtFirstName;
    public JFXTextField txtLastName;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXTextField txtConfirmPassword;
    public JFXComboBox<String> cmbUserType;
    public JFXComboBox<String> cmbYear;
    public TableView<OrderTM> tblAnnualIncome;
    public TableColumn colAnnualOid;
    public TableColumn colAnnualOrderDate;
    public TableColumn colAnnualCustomerNic;
    public TableColumn colAnnualDiscount;
    public TableColumn colAnnualGrossTotal;
    public TableColumn colAnnualNetTotal;
    public TableView<OrderTM> tblMonthlyIncome;
    public TableColumn colMonthlyOrderId;
    public TableColumn colMonthlyOrderDate;
    public TableColumn colMonthlyCustomerNIC;
    public TableColumn colMonthlyDiscount;
    public TableColumn colMonthlyGrossTotal;
    public TableColumn colMonthlyNetTotal;
    public JFXComboBox<String> cmbMonth;
    public TableView<OrderTM> tblDailyIncome;
    public TableColumn colDailyOrderId;
    public TableColumn colDailyOrderDate;
    public TableColumn colDailyCustomerNIC;
    public TableColumn colDailyDiscount;
    public TableColumn colDailyGrossTotal;
    public TableColumn colDailyNetTotal;
    public TableView<OrderTM> tblCustomerWiseIncome;
    public TableColumn colCustomerOrderId;
    public TableColumn colCustomerOrderDate;
    public TableColumn colCustomerNic;
    public TableColumn colCustomerDiscount;
    public TableColumn colCustomerGrossTotal;
    public JFXComboBox<String> cmbCustomerNIC;
    public TableColumn colCustomerNetTotal;
    public Label lblAnnualIncome;
    public Label lblMonthlyIncome;
    public Label lblDailyIncome;
    public Label lblCustomerIncome;
    public JFXDatePicker dailyDate;
    public Label lblMostMovableItem;
    public Label lblLeastMovableItem;
    public JFXComboBox<String> cmbYearSelected;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustId;
    public TableColumn colCustomerTitle;
    public TableColumn colCustomerName;
    public TableColumn colNIC;
    public TableColumn colCustomerAddress;
    public TableColumn colCustomerCity;
    public TableColumn colCustomerProvince;
    public TableColumn colPostalCode;
    ObservableList<OrderTM> annualObList=FXCollections.observableArrayList();
    ObservableList<OrderTM> monthlyObList=FXCollections.observableArrayList();
    ObservableList<OrderTM> dailyObList=FXCollections.observableArrayList();
    ObservableList<OrderTM> customerWiseObList=FXCollections.observableArrayList();


    public void initialize()  {
        new OrderController().deleteDbTable();
        setItemCodeToArrayList();

        cmbUserType.getItems().addAll("Cashier","Admin");
        ArrayList<String> yearList=new OrderController().getYear();
        cmbYear.getItems().addAll(yearList);
        cmbMonth.getItems().addAll("January","February","March","April","May","June","July","August","September","October","November","December");
        cmbYearSelected.getItems().addAll(yearList);
        ArrayList<String> customerNic=new CustomerController().getCustomerNIC();
        cmbCustomerNIC.getItems().addAll(customerNic);

        loadDateAndTime();
        setItemCode();


        try {
            setCustomerToTable(new CustomerController().getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerTitle.setCellValueFactory(new PropertyValueFactory<>("customerTitle"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("custNIC"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCustomerCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        colCustomerProvince.setCellValueFactory(new PropertyValueFactory<>("customerProvince"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));


        try {
            setItemsToTable(new ItemController().getAllItems());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                annualObList.clear();
                setAnnualOrderDetailsToTable(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        cmbMonth.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!cmbYearSelected.getSelectionModel().getSelectedItem().equals("null") && !cmbMonth.getSelectionModel().getSelectedItem().equals("null")) {
                    try {
                        monthlyObList.clear();
                        setMonthlyOrderDetailsToTable(newValue, cmbYearSelected.getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }catch(Exception e){

            }
        });
        cmbYearSelected.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!cmbYearSelected.getSelectionModel().getSelectedItem().equals("null") && !cmbMonth.getSelectionModel().getSelectedItem().equals("null")) {
                    try {
                        monthlyObList.clear();
                        setMonthlyOrderDetailsToTable(cmbMonth.getSelectionModel().getSelectedItem(), newValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){

            }
        });
        cmbCustomerNIC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                customerWiseObList.clear();
                setCustomerWiseOrderDetailsToTable(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void setItemCodeToArrayList() {
        ArrayList<SearchMinMaxItem> itemCodes=new ItemController().getItemCodeForSearch();
        for (SearchMinMaxItem s : itemCodes
                ) {
            new OrderController().getSumOfQty(s.getItemCode());
        }
        new OrderController().getMostMovableItem(lblMostMovableItem);
        new OrderController().getLeastMovableItem(lblLeastMovableItem);
    }

    private void setCustomerToTable(ArrayList<Customer> allCustomers) {
        ObservableList<CustomerTM> customerObList=FXCollections.observableArrayList();
        allCustomers.forEach(e->{
            customerObList.add(new CustomerTM(e.getCustomerId(),e.getCustomerTitle(),e.getCustomerName(),e.getCustomerNIC(),e.getAddress(),e.getCity(),e.getProvince(),e.getPostalCode()));
        });
        tblCustomer.setItems(customerObList);
    }

    private void setCustomerWiseOrderDetailsToTable(String newValue) throws SQLException, ClassNotFoundException {
        ArrayList<OrderTM> customerWiseList=new OrderController().getCustomerWiseOrders(newValue);
        if(customerWiseList.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Orders are not found",ButtonType.CLOSE).show();
            lblAnnualIncome.setText(" ");
        }else {
            customerWiseList.forEach(e -> {
                customerWiseObList.add(new OrderTM(e.getOrderId(), e.getOrderDate(), e.getCustomerNic(), e.getOrderDiscount(), e.getOrderGrossTotal(), e.getOrderNetTotal()));
            });
            tblCustomerWiseIncome.setItems(customerWiseObList);
            setCustomerWiseOrderDataToTable();
            calculateCustomerWiseIncome();
        }
    }

    private void calculateCustomerWiseIncome() {
        double customerWiseNetIncome=0;
        for (OrderTM tempTM : customerWiseObList
        ) {
            customerWiseNetIncome+=tempTM.getOrderNetTotal();
        }
        lblCustomerIncome.setText(customerWiseNetIncome+"0");
    }

    private void setCustomerWiseOrderDataToTable() {
        colCustomerOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colCustomerNic.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colCustomerDiscount.setCellValueFactory(new PropertyValueFactory<>("orderDiscount"));
        colCustomerGrossTotal.setCellValueFactory(new PropertyValueFactory<>("orderGrossTotal"));
        colCustomerNetTotal.setCellValueFactory(new PropertyValueFactory<>("orderNetTotal"));
    }

    private void setMonthlyOrderDetailsToTable(String month,String year) throws SQLException, ClassNotFoundException {
        ArrayList<OrderTM> monthlyList=new OrderController().getMonthlyOrders(month,year);
        if(monthlyList.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Orders are not found",ButtonType.CLOSE).show();
            lblMonthlyIncome.setText(" ");
        }else {
            monthlyList.forEach(e -> {
                monthlyObList.add(new OrderTM(e.getOrderId(), e.getOrderDate(), e.getCustomerNic(), e.getOrderDiscount(), e.getOrderGrossTotal(), e.getOrderNetTotal()));
            });
            tblMonthlyIncome.setItems(monthlyObList);
            setMonthlyOrderDataToTable();
            calculateMonthlyIncome();
        }
    }

    private void calculateMonthlyIncome() {
        double monthlyNetIncome=0;
        for (OrderTM tempTM : monthlyObList
        ) {
            monthlyNetIncome+=tempTM.getOrderNetTotal();
        }
        lblMonthlyIncome.setText(monthlyNetIncome+"0");
    }

    private void setMonthlyOrderDataToTable() {
        colMonthlyOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colMonthlyOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colMonthlyCustomerNIC.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colMonthlyDiscount.setCellValueFactory(new PropertyValueFactory<>("orderDiscount"));
        colMonthlyGrossTotal.setCellValueFactory(new PropertyValueFactory<>("orderGrossTotal"));
        colMonthlyNetTotal.setCellValueFactory(new PropertyValueFactory<>("orderNetTotal"));
    }

    private void setAnnualOrderDetailsToTable(String newValue) throws SQLException, ClassNotFoundException {
        ArrayList<OrderTM> annualList=new OrderController().getAnnualOrders(newValue);
        if(annualList.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Orders are not found",ButtonType.CLOSE).show();
            lblAnnualIncome.setText(" ");
        }else {
            annualList.forEach(e -> {
                annualObList.add(new OrderTM(e.getOrderId(), e.getOrderDate(), e.getCustomerNic(), e.getOrderDiscount(), e.getOrderGrossTotal(), e.getOrderNetTotal()));
            });
            tblAnnualIncome.setItems(annualObList);
            setAnnualOrderDataToTable();
            calculateAnnualIncome();
        }
    }

    private void calculateAnnualIncome() {
        double annualNetIncome=0;
        for (OrderTM tempTM : annualObList
                ) {
            annualNetIncome+=tempTM.getOrderNetTotal();
        }
        lblAnnualIncome.setText(annualNetIncome+"0");
    }

    private void setAnnualOrderDataToTable() {
        colAnnualOid.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colAnnualOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colAnnualCustomerNic.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colAnnualDiscount.setCellValueFactory(new PropertyValueFactory<>("orderDiscount"));
        colAnnualGrossTotal.setCellValueFactory(new PropertyValueFactory<>("orderGrossTotal"));
        colAnnualNetTotal.setCellValueFactory(new PropertyValueFactory<>("orderNetTotal"));
    }

    private void setItemsToTable(ArrayList<Item> items) {
        ObservableList<ItemTM> itemList = FXCollections.observableArrayList();
        items.forEach(e->{
            itemList.add(new ItemTM(e.getItemCode(),e.getDescription(),e.getPackSize(),e.getQuantityOnHand(),e.getDiscount(),e.getUnitPrice()));
        });
        tblItem.setItems(itemList);
    }

    private void setItemCode() {
        try {
            txtItemCode.setText(new ItemController().getItemCode());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDateAndTime() {
        Date date=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy - MM - dd");
        lblDate.setText(f.format(date));

        Timeline time=new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime=LocalTime.now();
            SimpleDateFormat s=new SimpleDateFormat("hh:mm aa");
            System.out.println(s.format(new Date()));
            lblTime.setText(
                    currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        Stage stg = (Stage) adminDashBoardContext.getScene().getWindow();
        stg.close();
        URL resource = getClass().getResource("../view/LogInForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Wholesale Distribution");
        stage.show();
    }

    public void clearOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        adminDashBoardContext.getChildren().clear();
        adminDashBoardContext.getChildren().add(load);
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        if(!txtDescription.getText().equals("") && !txtPackSize.getText().equals("") && !txtQuantityOnHand.getText().equals("") && !txtUnitPrice.getText().equals("") && !txtDiscount.getText().equals("")){
            try {
                Item i = new Item(
                        txtItemCode.getText(),
                        txtDescription.getText(),
                        txtPackSize.getText(),
                        Integer.parseInt(txtQuantityOnHand.getText()),
                        Double.parseDouble(txtDiscount.getText()),
                        Double.parseDouble(txtUnitPrice.getText())
                );
                if (new ItemController().updateItem(i)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully.", ButtonType.CLOSE).showAndWait();
                    URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                    Parent load = FXMLLoader.load(resource);
                    adminDashBoardContext.getChildren().clear();
                    adminDashBoardContext.getChildren().add(load);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again.", ButtonType.CLOSE).showAndWait();
                }
            }catch (Exception e){
                new Alert(Alert.AlertType.ERROR,"Error! Enter correct data format and try again",ButtonType.CLOSE).showAndWait();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please fill all fields.", ButtonType.CLOSE).showAndWait();
        }
    }

    public void addItemOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        if(!txtDescription.getText().equals("") && !txtPackSize.getText().equals("") && !txtQuantityOnHand.getText().equals("") && !txtUnitPrice.getText().equals("") && !txtDiscount.getText().equals("")){
            try {
                Item i1 = new Item(
                        txtItemCode.getText(),
                        txtDescription.getText(),
                        txtPackSize.getText(),
                        Integer.parseInt(txtQuantityOnHand.getText()),
                        Double.parseDouble(txtDiscount.getText()),
                        Double.parseDouble(txtUnitPrice.getText())
                );
                if (new ItemController().addItem(i1)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Item Saved Successfully", ButtonType.CLOSE).showAndWait();
                    URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
                    Parent load = FXMLLoader.load(resource);
                    adminDashBoardContext.getChildren().clear();
                    adminDashBoardContext.getChildren().add(load);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!", ButtonType.CLOSE).showAndWait();
                }
            }catch(Exception e){
                new Alert(Alert.AlertType.ERROR,"Error! Enter correct data format and try again",ButtonType.CLOSE).showAndWait();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please fill all fields.", ButtonType.CLOSE).showAndWait();
        }
    }

    public void deleteItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        if(new ItemController().deleteItem(txtSearchCode.getText())){
            new Alert(Alert.AlertType.CONFIRMATION,"Item Deleted Successfully",ButtonType.CLOSE).showAndWait();
            URL resource = getClass().getResource("../view/AdminDashBoardForm.fxml");
            Parent load = FXMLLoader.load(resource);
            adminDashBoardContext.getChildren().clear();
            adminDashBoardContext.getChildren().add(load);
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again!",ButtonType.CLOSE).showAndWait();
        }
    }

    public void searchItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        String code=txtSearchCode.getText();
        Item i1 = new ItemController().getItem(code);
        if(i1==null){
            new Alert(Alert.AlertType.WARNING,"Item does not exist.",ButtonType.CLOSE).showAndWait();

        }else{
            setItemData(i1);
            btnAddItem.setDisable(true);
        }
    }

    void setItemData(Item i) {
        txtItemCode.setText(i.getItemCode());
        txtDescription.setText(i.getDescription());
        txtPackSize.setText(i.getPackSize());
        txtQuantityOnHand.setText(String.valueOf(i.getQuantityOnHand()));
        txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
        txtDiscount.setText(String.valueOf(i.getDiscount()));
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

    public void setDailyOrderDetailsToTable(ActionEvent actionEvent) {
        dailyObList.clear();
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date=dailyDate.getValue().format(dateFormatter);
        try {
            ArrayList<OrderTM> dailyList=new OrderController().getDailyOrders(date);
            if(dailyList.isEmpty()){
                new Alert(Alert.AlertType.WARNING,"Orders are not found",ButtonType.CLOSE).show();
                lblDailyIncome.setText(" ");
            }else{
                dailyList.forEach(e -> {
                    dailyObList.add(new OrderTM(e.getOrderId(), e.getOrderDate(), e.getCustomerNic(), e.getOrderDiscount(), e.getOrderGrossTotal(), e.getOrderNetTotal()));
                });
                tblDailyIncome.setItems(dailyObList);
                setDailyOrderDataToTable();
                calculateDailyIncome();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void calculateDailyIncome() {
        double dailyNetIncome=0;
        for (OrderTM tempTM : dailyObList
        ) {
            dailyNetIncome+=tempTM.getOrderNetTotal();
        }
        lblDailyIncome.setText(dailyNetIncome+"0");
    }

    private void setDailyOrderDataToTable() {
        colDailyOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDailyOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colDailyCustomerNIC.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colDailyDiscount.setCellValueFactory(new PropertyValueFactory<>("orderDiscount"));
        colDailyGrossTotal.setCellValueFactory(new PropertyValueFactory<>("orderGrossTotal"));
        colDailyNetTotal.setCellValueFactory(new PropertyValueFactory<>("orderNetTotal"));
    }

    public void moveToPackSize(ActionEvent actionEvent) {
        txtPackSize.requestFocus();
    }

    public void moveToQtyOnHand(ActionEvent actionEvent) {
        txtQuantityOnHand.requestFocus();
    }

    public void moveToDiscount(ActionEvent actionEvent) {
        txtDiscount.requestFocus();
    }

    public void moveToUnitPrice(ActionEvent actionEvent) {
        txtUnitPrice.requestFocus();
    }

    public void moveToLastName(ActionEvent actionEvent) {
        txtLastName.requestFocus();
    }

    public void moveToUserName(ActionEvent actionEvent) {
        txtUserName.requestFocus();
    }

    public void moveToPassword(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void moveToConfirmPassword(ActionEvent actionEvent) {
        txtConfirmPassword.requestFocus();
    }
}
