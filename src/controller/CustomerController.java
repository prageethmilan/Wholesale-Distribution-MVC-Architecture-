package controller;

import db.DbConnection;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerController implements CustomerService{

    @Override
    public boolean addCustomer(Customer c) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?)");
        stm.setObject(1,c.getCustomerId());
        stm.setObject(2,c.getCustomerTitle());
        stm.setObject(3,c.getCustomerName());
        stm.setObject(4,c.getCustomerNIC());
        stm.setObject(5,c.getAddress());
        stm.setObject(6,c.getCity());
        stm.setObject(7,c.getProvince());
        stm.setObject(8,c.getPostalCode());
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateCustomer(Customer c) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Customer SET custID=?, custTitle=?, custName=?, custAddress=?, city=?, province=?, postalCode=? WHERE custNIC=?");
        stm.setObject(1,c.getCustomerId());
        stm.setObject(2,c.getCustomerTitle());
        stm.setObject(3,c.getCustomerName());
        stm.setObject(4,c.getAddress());
        stm.setObject(5,c.getCity());
        stm.setObject(6,c.getProvince());
        stm.setObject(7,c.getPostalCode());
        stm.setObject(8,c.getCustomerNIC());
        return stm.executeUpdate()>0;
    }

    @Override
    public String getCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT custID FROM Customer ORDER BY custID DESC LIMIT 1").executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "C-00"+tempId;
            }else if(tempId<=99){
                return "C-0"+tempId;
            }else {
                return "C-"+tempId;
            }

        }else{
            return "C-001";
        }
    }

    @Override
    public Customer getCustomer(String nic) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE custNIC=?");
        stm.setObject(1,nic);
        ResultSet rst = stm.executeQuery();
        if(rst.next()){
            return new Customer(
                    rst.getString(1),
                    rst.getString(3),
                    rst.getString(2),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7),
                    rst.getString(8)
            );
        }else{
            return null;
        }
    }

    public ArrayList<String> getCustomerNIC() {
        ArrayList<String> customerNICS=new ArrayList<>();
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
            ResultSet rst = stm.executeQuery();
            while(rst.next()){
                customerNICS.add(rst.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customerNICS;
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer ORDER BY custID ASC;");
        ResultSet rst = stm.executeQuery();
        ArrayList<Customer> allCustomers=new ArrayList<>();
        while(rst.next()){
            allCustomers.add(new Customer(rst.getString(1),rst.getString(3),rst.getString(2),rst.getString(4),rst.getString(5),rst.getString(6), rst.getString(7),rst.getString(8)));
        }
        return allCustomers;
    }
}
