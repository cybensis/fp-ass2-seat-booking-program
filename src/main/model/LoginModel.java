package main.model;

import main.SQLConnection;
import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.BCrypt;

public class LoginModel {

    Connection connection;

    public LoginModel(){

        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);

    }

    public Boolean isDbConnected(){
        try {
            return !connection.isClosed();
        }
        catch(Exception e){
            return false;
        }
    }

    public String isLogin(String user, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT user.password, accountTypes.accountType FROM user INNER JOIN accountTypes ON user.accountType = accountTypes.accountTypeID WHERE user.username = ?";
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");
                System.out.println(BCrypt.checkpw(pass, storedHash));
                if (BCrypt.checkpw(pass, storedHash)) {
                    System.out.println("test");
                    return resultSet.getString("accountType");
                }
                return "false";
            }
            else{
                return "false";
            }
        }
        catch (Exception e)
        {
            return "false";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }

    }

}
