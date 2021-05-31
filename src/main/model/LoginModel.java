package main.model;

import main.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.BCrypt;

public class LoginModel {

    private Singleton singleton = Singleton.getInstance();

    public Boolean isDbConnected() {
        try {
            return !singleton.getConnection().isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public String isLogin(String user, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT states.stateName FROM user INNER JOIN states ON user.accountState = states.stateID WHERE user.username = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("stateName").equals("deactivated"))
                    return "deactivated";
            }
            else
                return "false";

            query = "SELECT user.password, accountTypes.accountType, user.employeeID FROM user INNER JOIN accountTypes ON user.accountType = accountTypes.accountTypeID WHERE user.username = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user);
            resultSet = preparedStatement.executeQuery();
            String storedHash = resultSet.getString("password");
            if (BCrypt.checkpw(pass, storedHash)) {
                singleton.setUser(resultSet.getInt("employeeID"));
                return resultSet.getString("accountType");
            }
            return "false";
        } catch (Exception e) {
            return "false";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }

    }

}
