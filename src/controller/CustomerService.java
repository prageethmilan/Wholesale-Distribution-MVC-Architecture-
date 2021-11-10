package controller;

import model.Customer;

import java.sql.SQLException;

public interface CustomerService {
    public boolean addCustomer(Customer c) throws SQLException, ClassNotFoundException;
    public boolean updateCustomer(Customer c) throws SQLException, ClassNotFoundException;
    public String getCustomerId() throws SQLException, ClassNotFoundException;
    public Customer getCustomer(String nic) throws SQLException, ClassNotFoundException;
}
