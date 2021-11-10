package controller;

import db.DbConnection;
import javafx.scene.control.Label;
import model.ItemDetails;
import model.Order;
import model.SavedOrder;
import model.SearchMinMaxItem;
import view.tm.OrderTM;
import view.tm.SavedOrderDetailsTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderController {
    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT oId FROM OrderId ORDER BY oId DESC LIMIT 1").executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "O-00"+tempId;
            }else if(tempId<=99){
                return "O-0"+tempId;
            }else {
                return "O-"+tempId;
            }

        }else{
            return "O-001";
        }
    }

    public boolean saveOrder(String orderId, String custNIC, ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException {
        try {
            int count = 0;
            for (ItemDetails temp : items
            ) {
                PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `SavedOrder` VALUES (?,?,?,?,?,?,?,?,?)");
                stm.setObject(1, orderId);
                stm.setObject(2, custNIC);
                stm.setObject(3, temp.getItemCode());
                stm.setObject(4, temp.getDescription());
                stm.setObject(5, temp.getPackSize());
                stm.setObject(6, temp.getQtyForSell());
                stm.setObject(7, temp.getUnitPrice());
                stm.setObject(8, temp.getDiscount());
                stm.setObject(9, temp.getTotal());
                stm.executeUpdate();
                count++;
            }
            if (count != 0) {
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }
    public ArrayList<SavedOrder> getOrderIdAndNic() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `SavedOrder`");
        ResultSet rst = stm.executeQuery();
        ArrayList<SavedOrder> savedOrders=new ArrayList<>();
        while(rst.next()){
            savedOrders.add(new SavedOrder(rst.getString(1),rst.getString(2)));
        }
        return savedOrders;
    }

    public ArrayList<SavedOrderDetailsTM> getOrderDetails(String newValue, Label lblCustomerNIC) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `SavedOrder` WHERE oId=?");
        stm.setObject(1,newValue);
        ResultSet rst = stm.executeQuery();
        ArrayList<SavedOrderDetailsTM> items=new ArrayList<>();
        while(rst.next()){
            items.add(new SavedOrderDetailsTM(rst.getString(3),rst.getString(4),rst.getString(5),rst.getInt(6),rst.getDouble(7),rst.getDouble(8),rst.getDouble(9)));
            lblCustomerNIC.setText(rst.getString(2));
        }
        return items;
    }

    public boolean deleteOrder(String oId) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `SavedOrder` WHERE oId=?");
        stm.setObject(1,oId);
        if (stm.executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    public boolean placeOrder(Order order) {
        Connection con=null;
        try {
            con=DbConnection.getInstance().getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO `Order` VALUES (?,?,?,?,?,?)");
            stm.setObject(1,order.getOrderId());
            stm.setObject(2,order.getOrderDate());
            stm.setObject(3,order.getOrderTime());
            stm.setObject(4,order.getCustNIC());
            stm.setObject(5,order.getDiscount());
            stm.setObject(6,order.getCost());

            if(stm.executeUpdate()>0){
                if(saveOrderDetails(order.getOrderId(),order.getItems())){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveOrderDetails(String orderId, ArrayList<ItemDetails> items) throws SQLException, ClassNotFoundException {
        for (ItemDetails item :items
                ) {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?,?)");
            stm.setObject(1,orderId);
            stm.setObject(2,item.getItemCode());
            stm.setObject(3,item.getQtyForSell());
            stm.setObject(4,item.getDiscount());
            stm.setObject(5,item.getTotal());
            if (stm.executeUpdate()>0){
                if(updateQty(item.getItemCode(),item.getQtyForSell())){

                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qtyForSell) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET qtyOnHand=(qtyOnHand-" + qtyForSell + ") WHERE itemCode='" + itemCode + "'");
        return stm.executeUpdate()>0;
    }

    public void saveorderId(String oId, String cId) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO OrderId VALUES(?,?)");
            stm.setObject(1,oId);
            stm.setObject(2,cId);
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<OrderTM> getAnnualOrders(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order` WHERE YEAR(orderDate) =?");
        stm.setObject(1,newValue);
        ResultSet rst = stm.executeQuery();
        ArrayList<OrderTM> annualOrderList=new ArrayList<>();
        while(rst.next()){
            annualOrderList.add(new OrderTM(rst.getString(1),rst.getString(2),rst.getString(4),rst.getDouble(5),rst.getDouble(6)+rst.getDouble(5),rst.getDouble(6)));
        }
        return annualOrderList;
    }

    public ArrayList<OrderTM> getMonthlyOrders(String month,String year) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order` WHERE MONTHNAME(orderDate) =? AND YEAR(orderDate) =?");
        stm.setObject(1,month);
        stm.setObject(2,year);
        ResultSet rst = stm.executeQuery();
        ArrayList<OrderTM> monthlyOrderList=new ArrayList<>();
        while(rst.next()){
            monthlyOrderList.add(new OrderTM(rst.getString(1),rst.getString(2),rst.getString(4),rst.getDouble(5),rst.getDouble(6)+rst.getDouble(5),rst.getDouble(6)));
        }
        return monthlyOrderList;
    }

    public ArrayList<OrderTM> getDailyOrders(String date) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order` WHERE orderDate=?");
        stm.setObject(1,date);
        ResultSet rst = stm.executeQuery();
        ArrayList<OrderTM> dailyOrderList=new ArrayList<>();
        while(rst.next()){
            dailyOrderList.add(new OrderTM(rst.getString(1),rst.getString(2),rst.getString(4),rst.getDouble(5),rst.getDouble(6),rst.getDouble(6)-rst.getDouble(5)));
        }
        return dailyOrderList;
    }

    public void getMostMovableItem(Label lblMostMovableItem) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT orderItemCode FROM `Search Movable Item` ORDER BY sumOfQty DESC LIMIT 1;");
            ResultSet rst = stm.executeQuery();
            if(rst.next()){
                String itemDescription=getItemDescriptionToLabel(rst.getString(1));
                lblMostMovableItem.setText(itemDescription);
            }else{
                lblMostMovableItem.setText(" ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getItemDescriptionToLabel(String itemCode) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT description FROM Item WHERE itemCode=?");
            stm.setObject(1,itemCode);
            ResultSet rst = stm.executeQuery();
            if(rst.next()){
                return rst.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getLeastMovableItem(Label lblLeastMovableItem) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT orderItemCode FROM `Search Movable Item` ORDER BY sumOfQty ASC LIMIT 1;");
            ResultSet rst = stm.executeQuery();
            if(rst.next()){
                String itemDescription=getItemDescriptionToLabel(rst.getString(1));
                lblLeastMovableItem.setText(itemDescription);
            }else{
                lblLeastMovableItem.setText(" ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getYear() {
        ArrayList<String> yearList=new ArrayList<>();
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT YEAR(orderDate) FROM `Order`");
            ResultSet rst = stm.executeQuery();
            while(rst.next()){
                if(isYearExists(rst.getString(1),yearList)){

                }else{
                    yearList.add(rst.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return yearList;
    }

    private boolean isYearExists(String string,ArrayList<String> yearList) {
        for (String year : yearList
                ) {
            if(year.equals(string)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<OrderTM> getCustomerWiseOrders(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order` WHERE cNIC=?");
        stm.setObject(1,newValue);
        ResultSet rst = stm.executeQuery();
        ArrayList<OrderTM> customerWiseOrderList=new ArrayList<>();
        while(rst.next()){
            customerWiseOrderList.add(new OrderTM(rst.getString(1),rst.getString(2),rst.getString(4),rst.getDouble(5),rst.getDouble(6)+rst.getDouble(5),rst.getDouble(6)));
        }
        return customerWiseOrderList;
    }

    public void getSumOfQty(String itemCode) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT iCode,sum(orderQty) FROM `order detail` WHERE iCode=?");
            stm.setObject(1,itemCode);
            ResultSet result = stm.executeQuery();
            if(result.next()){
                SearchMinMaxItem s1=new SearchMinMaxItem(result.getString(1),result.getInt(2));
                insertSearchMinMaxItemToTable(s1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertSearchMinMaxItemToTable(SearchMinMaxItem s1) {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `Search Movable Item` VALUES (?,?)");
            stm.setObject(1,s1.getItemCode());
            stm.setObject(2,s1.getSumOfQty());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deleteDbTable() {
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM `Search Movable Item`");
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
