package main.model;

import main.BCrypt;
import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterModel {

    Connection connection;

    public RegisterModel(){

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

    public String attempRegister(Integer employeeID, String role, String firstName, String surname, String username, String password, String secretQ, String secretQAnswer, int accountTypeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "select username,employeeID from user where username = ? OR employeeID = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, employeeID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //String storedHash = resultSet.getString("password");
                if (resultSet.getString("username").equals(username))
                    return "There is already a user registered with this username, please use another";
                else
                    return "There is already an account registered with this employee ID";
            }
            else{
                String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());
                preparedStatement = null;
                resultSet=null;
                String insertQuery = "INSERT INTO user (firstName,lastName,password,employeeID,secretQuestion,secretQuestionAnswer,username,employeeRole, accountType) VALUES (?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.setInt(4, employeeID);
                preparedStatement.setString(5, secretQ);
                preparedStatement.setString(6, secretQAnswer);
                preparedStatement.setString(7, username);
                preparedStatement.setString(8, role);
                preparedStatement.setInt(8, accountTypeID);
                int response = preparedStatement.executeUpdate();
                return "Success";
            }
        }
        catch (Exception e)
        {
            return "An error occurred, please try again, if this occurs please change some of your values";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }

    }

}
