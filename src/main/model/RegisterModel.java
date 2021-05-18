package main.model;

import main.BCrypt;
import main.SQLConnection;

import java.sql.*;

public class RegisterModel {
    //Holds the amount of different input fields that are required for an account.
    private static final int ACCOUNT_FIELDS = 8;
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

    public String attemptRegister(Integer employeeID, String role, String firstName, String surname, String username, String password, String secretQ, String secretQAnswer, int accountTypeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String doesUserExist = doesUserExist(username, employeeID);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if (!doesUserExist.equals("noUser")) {
                    return doesUserExist;
            }
            else{
                try {
                    preparedStatement = null;
                    resultSet = null;
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
                    preparedStatement.setInt(9, accountTypeID);
                    int response = preparedStatement.executeUpdate();
                    return "Success";
                }
                catch (Exception e)
                {
                    return "An error occurred, please try again later";
                }finally {
                    if (preparedStatement != null)
                        preparedStatement.close();
                    if (resultSet != null)
                        resultSet.close();
                }
            }
    }


    public String[] retrieveUserDetails(String employeeID, String accountType) throws SQLException {
        String accountDetails[] = new String[ACCOUNT_FIELDS];
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT * FROM user INNER JOIN accountTypes ON user.accountType = accountTypes.accountTypeID WHERE user.employeeID = ? AND accountTypes.accountType = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            preparedStatement.setString(2, accountType);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Instead of manually assigning each array index to a result, using resultSet.get('password'), etc, I
                // get the metadata of the resultSet keys, then access them in a for loop via getColumnName.
                ResultSetMetaData keys = resultSet.getMetaData();
                for (int i = 0; i < ACCOUNT_FIELDS; i++) {
                    // For some reason, ResultSetMetaData has its first index as 1 not 0, hence why it has the i+1
                    accountDetails[i] = resultSet.getString(keys.getColumnName(i+1));
                }
            }
        }
        catch (Exception e)
        {

            accountDetails[0] = "Error";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return accountDetails;
    }

    public String updateUserDetails(int originalID, String originalUsername, Integer employeeID, String role, String firstName, String surname, String username, String password, String secretQ, String secretQAnswer, int accountTypeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String doesUserExist = "noUser";
        if (originalID != employeeID)
            doesUserExist = checkExistingID(employeeID);
        if (!originalUsername.equals(username) && !doesUserExist.equals("noUser"))
            doesUserExist = checkExistingUsername(username);
        if (!doesUserExist.equals("noUser"))
            return doesUserExist;
        else{
            try {
                preparedStatement = null;
                resultSet = null;
                String insertQuery = "UPDATE user SET firstName = ?,lastName = ?,password = ?,employeeID = ?,secretQuestion = ?,secretQuestionAnswer = ?, username = ?,employeeRole = ? WHERE employeeID = ?";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, password);
                preparedStatement.setInt(4, employeeID);
                preparedStatement.setString(5, secretQ);
                preparedStatement.setString(6, secretQAnswer);
                preparedStatement.setString(7, username);
                preparedStatement.setString(8, role);
                preparedStatement.setInt(9, originalID);
                int response = preparedStatement.executeUpdate();
                return "Success";
            }
            catch (Exception e)
            {
                return "An error occurred, please try again later";
            }finally {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (resultSet != null)
                    resultSet.close();
            }
        }
    }

    private String doesUserExist(String username, int employeeID) {
        String response = checkExistingID(employeeID);
        if (response.equals("noUser"))
            return checkExistingUsername(username);
        else
            return response;
    }
    private String checkExistingID(int employeeID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT employeeID FROM user WHERE employeeID = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "There is already an account registered with this employee ID";
            }
            else
                return "noUser";
        }
        catch (SQLException error) {
            return "An unexpected error occurred";
        }
    }

    private String checkExistingUsername(String username) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT username FROM user WHERE username = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                    return "There is already a user registered with this username, please use another";
            }
            else
                return "noUser";
        }
        catch (SQLException error) {
            return "An unexpected error occurred";
        }
    }

}
