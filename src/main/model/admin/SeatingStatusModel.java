package main.model.admin;

import main.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatingStatusModel {
    private Singleton singleton = Singleton.getInstance();

    public String getSeatingStatus() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String checkAlreadyBooked = "SELECT covidConditions.condition FROM dateConditions INNER JOIN covidConditions ON dateConditions.condition = covidConditions.conditionID  WHERE date = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("condition");
            }
            // If it doesn't have a condition set, then by default, it is normal.
            return "Normal";
        } catch (Exception e) {
            return "error";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public boolean checkForBookings() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String checkAlreadyBooked = "SELECT count(*) FROM userBookings WHERE date = ?";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt("count(*)") > 0)
                    return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public String setSeatingStatus(String status) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            int conditionID = getStatusID(status);
            if (conditionID == 0)
                return "An error occurrred";
            String checkAlreadyBooked = "INSERT INTO dateConditions (date, condition)  VALUES (?,?);";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            preparedStatement.setInt(2, conditionID);
            preparedStatement.executeUpdate();
            return "This date condition was successfully set";
        } catch (Exception e) {
            return "An error occurred";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    public int getStatusID(String statusName) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String checkAlreadyBooked = "SELECT conditionID FROM covidConditions WHERE condition = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, statusName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("conditionID");
            }
            return 0;
        } catch (Exception e) {
            return 0;
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }



}
