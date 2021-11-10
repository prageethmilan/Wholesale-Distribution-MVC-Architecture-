package controller;

import db.DbConnection;
import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserController {
    public boolean signupUser(User u) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO `UserDetail` VALUES (?,?,?,?,?)");
        stm.setObject(1,u.getFirstName());
        stm.setObject(2,u.getLastName());
        stm.setObject(3,u.getUserType());
        stm.setObject(4,u.getUserName());
        stm.setObject(5,u.getPassword());

        return stm.executeUpdate()>0;
    }

}
