package main.model;

import main.BCrypt;
import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.BCrypt;
import main.Singleton;

public class ResetPasswordModel {

    private Singleton singleton = Singleton.getInstance();


    public String changePassword(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "UPDATE user SET password = ? WHERE username = ?";
        String message = "error";
        System.out.println(password);
        String passwordHash = BCrypt.hashpw(password,BCrypt.gensalt());
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, passwordHash);
            preparedStatement.setString(2, username);
            int response = preparedStatement.executeUpdate();
            message = "success";
        } catch (Exception e) {
            message = "error";
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            return message;
        }
    }

    public String[] getSecretQuestion(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT secretQuestion,secretQuestionAnswer FROM user WHERE username = ?";
        String secretQuestion[] = new String[2];
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                secretQuestion[0] = resultSet.getString("secretQuestion");
                secretQuestion[1] = resultSet.getString("secretQuestionAnswer");
            }
            else
            secretQuestion[0] = "noAccount";
        } catch (Exception e) {
            secretQuestion[0] = "error";
        }
    finally {
        if (preparedStatement != null)
            preparedStatement.close();
        if (resultSet != null)
            resultSet.close();
        return secretQuestion;
    }
    }

}
