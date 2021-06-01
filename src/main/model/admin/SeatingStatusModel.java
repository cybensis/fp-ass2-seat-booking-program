package main.model.admin;

import main.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatingStatusModel {
    private Singleton singleton = Singleton.getInstance();

    // This gets the seatings status for the selected date, either normal, lockdown or covid conditions, and if the
    // query returns nothing, then that means it is normal.
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

    // This checks for bookings on the selected date as the admin cannot change the seating status if the date has
    // bookings (unless changing the status to normal since nothing is blocked in normal conditions).
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

    // This sets the seating status for the selected date.
    public String setSeatingStatus(String status) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            int conditionID = getStatusID(status);
            if (conditionID == 0)
                return "An error occurred";
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

    // Gets the ID of the status, as the system only knows the actual string name.
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
