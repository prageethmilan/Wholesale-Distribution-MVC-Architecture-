package controller;

import model.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemService {
    public boolean addItem(Item i) throws SQLException, ClassNotFoundException;
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException;
    public Item getItem(String code) throws SQLException, ClassNotFoundException;
    public String getItemCode() throws SQLException, ClassNotFoundException;
    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException;
}
