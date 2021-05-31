package main.model.user;

import main.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateBookingModel {

    private Singleton singleton = Singleton.getInstance();

    // Since the user cannot sit in a seat that was previously booked, this method finds the last booked seat to block
    // it, but another problem is, that a user could book a desk in the future, then book that same seat, the day before
    // it, since the method would only check for the previously sat desk, so now this method checks for both the
    // previously booked seat, and the closest future dates booked seat.
    public int[] getBlockedDesks() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Since there are no desks that are negative numbers, -1 essentially means, there is no blocked seat.
        int blockedDesks[] = new int[]{-1, -1};
        try {
            String query = "SELECT deskID FROM userBookings WHERE date = (SELECT MAX(date) FROM userBookings WHERE employeeID = ? AND date < ?);";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, singleton.getUser());
            preparedStatement.setString(2, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                blockedDesks[0] = resultSet.getInt("deskID");
            }
            query = "SELECT deskID FROM userBookings WHERE date = (SELECT MIN(date) FROM userBookings WHERE employeeID = ? AND date > ?);";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, singleton.getUser());
            preparedStatement.setString(2, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                blockedDesks[1] = resultSet.getInt("deskID");
            }
            return blockedDesks;
        } catch (Exception e) {
            return null;
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public String getSeatingStatus() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT covidConditions.condition FROM dateConditions INNER JOIN covidConditions ON dateConditions.condition = covidConditions.conditionID  WHERE date = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("condition");
            }
            return "Normal";
        } catch (Exception e) {
            return "Error";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public ArrayList<Integer> blacklistedDesks() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Integer> tablesBooked = new ArrayList<>();
        try {
            String query = "SELECT deskID FROM userBookings WHERE date = ? AND state = 1;";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                tablesBooked.add(resultSet.getInt("deskID"));
            }
            return tablesBooked;
        } catch (Exception e) {
            return null;
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }

    }

    public String addBooking() throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "INSERT INTO userBookings (deskID, employeeID, date, state) VALUES (?,?,?,2)";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, singleton.getChosenDesk());
            preparedStatement.setInt(2, singleton.getUser());
            preparedStatement.setString(3, String.valueOf(singleton.getDate()));
            int response = preparedStatement.executeUpdate();
            return "Success";
        }
        catch (SQLException error) {
            return "An unexpected error occurred";
        }
    }

    public String updateBooking() throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "UPDATE userBookings SET deskID = ?, employeeID = ?, date = ?, state = 2 WHERE employeeID  = ? AND date = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, singleton.getChosenDesk());
            preparedStatement.setInt(2, singleton.getUser());
            preparedStatement.setString(3, String.valueOf(singleton.getDate()));
            preparedStatement.setInt(4, singleton.getUser());
            preparedStatement.setString(5, String.valueOf(singleton.getBookingDate()));
            preparedStatement.executeUpdate();
            return "Success";
        }
        catch (SQLException error) {
            return "An unexpected error occurred";
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

}
