package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.ItemDetails;
import model.Order;
import view.tm.CartTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CashierDashBoardFormController {

    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtAddress;
    public JFXTextField txtPostalCode;
    public JFXTextField txtCustomerName;
    public JFXComboBox<String> cmbCity;
    public JFXComboBox<String> cmbProvince;
    public AnchorPane cashierDashBoardContext;
    public JFXTextField txtCustomerNIC;
    public JFXTextField txtSearchNIC;
    public JFXButton btnAddCustomer;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtPackSize;
    public JFXTextField txtQuantityOnHand;
    public JFXTextField txtDiscount;
    public JFXTextField txtQuantityForSell;
    public TableView<CartTM> tblOrderDetail;
    public Label lblGrossAmount;
    public Label lblDiscount;
    public Label lblNetAmount;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQuantity;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public JFXTextField txtItemCode;
    public JFXComboBox<String> cmbDescription;
    public TableColumn colPackSize;
    public Label lblOrderId;
    public JFXButton btnAddToCart;
    public JFXTextField txtUpdateQty;
    double netAmount=0;
    int cartSelectedRow=-1;


    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));

        btnAddToCart.setDisable(true);
        loadDateAndTime();
        loadCity();
        loadProvince();
        setCustomerId();
        setOrderId();
        try {
            loadItemDescriptions();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbDescription.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setItemData(newValue);


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        tblOrderDetail.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            cartSelectedRow= (int) newValue;
        });
    }

    private void setOrderId() {
        try {
            lblOrderId.setText(new OrderController().getOrderId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemData(String newValue) throws SQLException, ClassNotFoundException {
        Item i1 = new ItemController().getItemDescription(newValue);
        if(i1==null){

        }else{
            txtItemCode.setText(i1.getItemCode());
            txtUnitPrice.setText(String.valueOf(i1.getUnitPrice()));
            txtPackSize.setText(i1.getPackSize());
            Optional<CartTM> optOrderDetail = tblOrderDetail.getItems().stream().filter(detail -> detail.getDescription().equals(newValue)).findFirst();
            txtQuantityOnHand.setText((optOrderDetail.isPresent() ? i1.getQuantityOnHand() - optOrderDetail.get().getQty() : i1.getQuantityOnHand()) + "");
            txtDiscount.setText(String.valueOf(i1.getDiscount()));
        }
    }

    private void loadItemDescriptions() throws SQLException, ClassNotFoundException {
        List<String> itemNames = new ItemController().getItemDescription();
        cmbDescription.getItems().addAll(itemNames);
    }


    private void setCustomerId() {
        try {
            txtCustomerId.setText(new CustomerController().getCustomerId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadProvince() {
        cmbProvince.getItems().addAll("Northern Province","Eastern Province","Southern Province","Western Province","Central Province","North Central Province","North Western Province","Sabaragamuwa Province","Uva Province");
    }

    private void loadCity() {
        cmbCity.getItems().addAll("Ampara","Anuradhapura","Badulla","Batticaloa","Colombo","Gampaha","Galle","Hambantota","Jaffna","Kaluthara","Kegalle","Kandy","Kilinochchi","Kurunegala","Mannar","Matale","Matara","Monaragala","Mullathivu","Nuwara Eliya","Polonnaruwa","Puttalam","Ratnapura","Trincomalee","Vavuniya");
    }

    private void loadDateAndTime() {
        Date date=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy - MM - dd");
        lblDate.setText(f.format(date));

        Timeline time=new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime=LocalTime.now();
            lblTime.setText(
                    currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        if(!txtCustomerName.getText().equals("") && !txtCustomerTitle.getText().equals("") && !txtCustomerNIC.getText().equals("") && !txtAddress.getText().equals("") && !txtPostalCode.getText().equals("") && !cmbCity.getSelectionModel().getSelectedItem().equalsIgnoreCase("null") && !cmbProvince.getSelectionModel().getSelectedItem().equalsIgnoreCase("null")) {
            Customer c1 = new Customer(
                    txtCustomerId.getText(),
                    txtCustomerName.getText(),
                    txtCustomerTitle.getText(),
                    txtCustomerNIC.getText(),
                    txtAddress.getText(),
                    cmbCity.getSelectionModel().getSelectedItem(),
                    cmbProvince.getSelectionModel().getSelectedItem(),
                    txtPostalCode.getText()
            );
            if (new CustomerController().addCustomer(c1)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved Successfully", ButtonType.CLOSE).showAndWait();
                URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                cashierDashBoardContext.getChildren().clear();
                cashierDashBoardContext.getChildren().add(load);
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again", ButtonType.CLOSE).showAndWait();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields.", ButtonType.CLOSE).showAndWait();
        }
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        Stage stg = (Stage) cashierDashBoardContext.getScene().getWindow();
        stg.close();
        URL resource = getClass().getResource("../view/LogInForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Wholesale Distribution");
        stage.show();
    }

    public void searchCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String nic=txtSearchNIC.getText();
        Customer c1 = new CustomerController().getCustomer(nic);
        if(c1==null){
            new Alert(Alert.AlertType.WARNING,"Customer does not exist.",ButtonType.CLOSE).showAndWait();
        }else{
            setCustomerData(c1);
            btnAddCustomer.setDisable(true);
            btnAddToCart.setDisable(false);
        }
    }

    void setCustomerData(Customer c1) {
        txtCustomerId.setText(c1.getCustomerId());
        txtAddress.setText(c1.getAddress());
        txtCustomerName.setText(c1.getCustomerName());
        txtCustomerNIC.setText(c1.getCustomerNIC());
        txtCustomerTitle.setText(c1.getCustomerTitle());
        txtPostalCode.setText(c1.getPostalCode());
        cmbCity.setValue(c1.getCity());
        cmbProvince.setValue(c1.getProvince());
    }

    public void updateCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        if(!txtCustomerName.getText().equals("") && !txtCustomerTitle.getText().equals("") && !txtCustomerNIC.getText().equals("") && !txtAddress.getText().equals("") && !txtPostalCode.getText().equals("") && !cmbCity.getSelectionModel().getSelectedItem().equalsIgnoreCase("null") && !cmbProvince.getSelectionModel().getSelectedItem().equalsIgnoreCase("null")) {
            Customer c1 = new Customer(
                    txtCustomerId.getText(),
                    txtCustomerName.getText(),
                    txtCustomerTitle.getText(),
                    txtCustomerNIC.getText(),
                    txtAddress.getText(),
                    cmbCity.getSelectionModel().getSelectedItem(),
                    cmbProvince.getSelectionModel().getSelectedItem(),
                    txtPostalCode.getText()
            );
            if (new CustomerController().updateCustomer(c1)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully.", ButtonType.CLOSE).showAndWait();
                URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                cashierDashBoardContext.getChildren().clear();
                cashierDashBoardContext.getChildren().add(load);
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again.", ButtonType.CLOSE).showAndWait();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.", ButtonType.CLOSE).showAndWait();
        }
    }

    public void clearCustomerDetails(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        cashierDashBoardContext.getChildren().clear();
        cashierDashBoardContext.getChildren().add(load);
    }


    ObservableList<CartTM> obList= FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {
        try {
            if (!cmbDescription.getSelectionModel().getSelectedItem().equals("null") && !txtQuantityForSell.getText().equals("")) {
                String itemCode = txtItemCode.getText();
                String description = cmbDescription.getSelectionModel().getSelectedItem();
                double unitPrice = Double.parseDouble(txtUnitPrice.getText());
                String packSize = txtPackSize.getText();
                int qtyOnHand = Integer.parseInt(txtQuantityOnHand.getText());
                double discount = Double.parseDouble(txtDiscount.getText());
                int qtyForSell = Integer.parseInt(txtQuantityForSell.getText());
                double totalDiscount = discount * qtyForSell * Double.parseDouble(packSize);
                double total = unitPrice * qtyForSell * Double.parseDouble(packSize);

                if (qtyOnHand < qtyForSell) {
                    new Alert(Alert.AlertType.WARNING, "Invalid Quantity", ButtonType.CLOSE).showAndWait();
                    return;
                }
                CartTM cartTm = new CartTM(itemCode, description, String.valueOf(packSize), qtyForSell, unitPrice, totalDiscount, total);
                int rowNumber = isExists(cartTm);

                if (rowNumber == -1) {
                    obList.add(cartTm);
                } else {
                    CartTM temp = obList.get(rowNumber);
                    CartTM newTm = new CartTM(
                            temp.getItemCode(),
                            temp.getDescription(),
                            temp.getPackSize(),
                            temp.getQty() + qtyForSell,
                            unitPrice,
                            totalDiscount + temp.getDiscount(),
                            total + temp.getTotal()
                    );

                    obList.remove(rowNumber);
                    obList.add(newTm);
                }
                tblOrderDetail.setItems(obList);
                calculateCost();
                txtItemCode.clear();
                txtUnitPrice.clear();
                txtPackSize.clear();
                txtQuantityOnHand.clear();
                txtDiscount.clear();
                txtQuantityForSell.clear();
                cmbDescription.getSelectionModel().clearSelection();
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a item", ButtonType.CLOSE).showAndWait();
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING, "Please select a item", ButtonType.CLOSE).showAndWait();
        }
    }

    private void calculateCost() {
        double ttl=0;
        double discount=0;
        for (CartTM tm :obList
                ) {
            ttl+=tm.getTotal();
        }
        for (CartTM tm :obList
                ) {
            discount+=tm.getDiscount();
        }
        netAmount=ttl-discount;
        lblGrossAmount.setText(ttl+"0");
        lblDiscount.setText(discount+"0");
        lblNetAmount.setText(netAmount+"0");
    }

    private int isExists(CartTM cartTm) {
        for (int i = 0; i < obList.size(); i++) {
            if(cartTm.getItemCode().equals(obList.get(i).getItemCode())){
                return i;
            }
        }
        return -1;
    }

    public void clearOnAction(ActionEvent actionEvent) {
        if(cartSelectedRow==-1){
            new Alert(Alert.AlertType.WARNING,"Please select a raw.",ButtonType.CLOSE).showAndWait();
        }else{
            obList.remove(cartSelectedRow);
            calculateCost();
            tblOrderDetail.refresh();
        }
    }


    public void openStockReport(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/StockReport.fxml"));
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle("Stock Report");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        try {
            int updateQuantity= Integer.parseInt(txtUpdateQty.getText());
            if (cartSelectedRow == -1) {
                new Alert(Alert.AlertType.WARNING, "Please select a raw.", ButtonType.CLOSE).showAndWait();
            } else {
                if(updateQuantity<new ItemController().checkQtyOnHand(obList.get(cartSelectedRow).getItemCode())) {
                    obList.get(cartSelectedRow).setQty(Integer.parseInt(txtUpdateQty.getText()));
                    calculateCost();
                    tblOrderDetail.refresh();
                    txtUpdateQty.clear();
                }else{
                    new Alert(Alert.AlertType.WARNING,"Invalid Qty!",ButtonType.CLOSE).showAndWait();
                }
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.WARNING, "Please fill quantity field", ButtonType.CLOSE).showAndWait();
        }
    }

    public void saveOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        ArrayList<ItemDetails> items=new ArrayList<>();
        for (CartTM tempTm :obList
             ) {
            items.add(new ItemDetails(
                    tempTm.getItemCode(),
                    tempTm.getDescription(),
                    tempTm.getPackSize(),
                    tempTm.getQty(),
                    tempTm.getUnitPrice(),
                    tempTm.getDiscount(),
                    tempTm.getTotal()
            ));
        }
        Order order=new Order(lblOrderId.getText(),txtCustomerNIC.getText(),items);
        if(new OrderController().saveOrder(order.getOrderId(),order.getCustNIC(),order.getItems())){
            new OrderController().saveorderId(lblOrderId.getText(),txtCustomerId.getText());
            new Alert(Alert.AlertType.CONFIRMATION,"Order Saved Successfully.",ButtonType.CLOSE).showAndWait();
            URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
            Parent load = FXMLLoader.load(resource);
            cashierDashBoardContext.getChildren().clear();
            cashierDashBoardContext.getChildren().add(load);
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again",ButtonType.CLOSE).showAndWait();
        }
    }

    public void cancleOrderOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../view/CashierDashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        cashierDashBoardContext.getChildren().clear();
        cashierDashBoardContext.getChildren().add(load);
    }

    public void loadSavedOrdersOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/SavedOrdersForm.fxml"));
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle("Saved Orders");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void moveToCustomerAddress(ActionEvent actionEvent) {
        txtAddress.requestFocus();
    }

    public void moveToCustomerNic(ActionEvent actionEvent) {
        txtCustomerNIC.requestFocus();
    }

    public void moveToCustomerTitle(ActionEvent actionEvent) {
        txtCustomerTitle.requestFocus();
    }
}
