package controller;

import db.DbConnection;
import model.Item;
import model.SearchMinMaxItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController implements ItemService{

    @Override
    public boolean addItem(Item i) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?,?)");
        stm.setObject(1,i.getItemCode());
        stm.setObject(2,i.getDescription());
        stm.setObject(3,i.getPackSize());
        stm.setObject(4,i.getUnitPrice());
        stm.setObject(5,i.getQuantityOnHand());
        stm.setObject(6,i.getDiscount());
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean updateItem(Item i) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET description=?, packSize=?, unitPrice=?, qtyOnHand=?, discount=? WHERE itemCode=?");
        stm.setObject(1,i.getDescription());
        stm.setObject(2,i.getPackSize());
        stm.setObject(3,i.getUnitPrice());
        stm.setObject(4,i.getQuantityOnHand());
        stm.setObject(5,i.getDiscount());
        stm.setObject(6,i.getItemCode());
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        if (DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Item WHERE itemCode='" + code + "'").executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Item getItem(String code) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE itemCode=?");
        stm.setObject(1,code);
        ResultSet rst = stm.executeQuery();
        if(rst.next()){
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(4)
            );
        }else{
            return null;
        }
    }

    @Override
    public String getItemCode() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT itemCode FROM Item ORDER BY itemCode DESC LIMIT 1").executeQuery();
        if (rst.next()){

            int tempCode = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempCode=tempCode+1;
            if (tempCode<=9){
                return "I-00"+tempCode;
            }else if(tempCode<=99){
                return "I-0"+tempCode;
            }else{
                return"I-"+tempCode;
            }

        }else{
            return "I-001";
        }
    }

    @Override
    public ArrayList<Item> getAllItems() throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM Item");
        ResultSet rst = stm.executeQuery();
        ArrayList<Item> items=new ArrayList<>();
        while (rst.next()) {
            items.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(4)
            ));
        }
        return items;
    }
    public List<String> getItemDescription() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item").executeQuery();
        List<String> itemNames=new ArrayList<>();
        while(rst.next()){
            itemNames.add(
                    rst.getString(2)
            );
        }
        return itemNames;
    }

    public Item getItemDescription(String description) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE description=?");
        stm.setObject(1,description);
        ResultSet rst = stm.executeQuery();
        if(rst.next()){
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getDouble(4)
            );
        }else{
            return null;
        }
    }

    public int checkQtyOnHand(String itemCode) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE itemCode=?");
        stm.setObject(1,itemCode);
        ResultSet rst = stm.executeQuery();
        while(rst.next()){
            if(rst.getString(1).equals(itemCode)){
                return rst.getInt(5);
            }
        }
        return 0;
    }

    public ArrayList<SearchMinMaxItem> getItemCodeForSearch() {
        ArrayList<SearchMinMaxItem> itemCodes=new ArrayList<>();
        try {
            PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT itemCode FROM Item");
            ResultSet rst = stm.executeQuery();
            while(rst.next()){
                itemCodes.add(new SearchMinMaxItem(rst.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemCodes;
    }
}
