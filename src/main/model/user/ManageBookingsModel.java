package main.model.user;


import main.Singleton;
import main.controller.user.ManageBookingTableRow;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class ManageBookingsModel {

    private Singleton singleton = Singleton.getInstance();

    // Gets all bookings for the current user, starting from the current date, and returns an array of booking objects.
    public ManageBookingTableRow[] getUserBookings() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String currentDate = String.valueOf(LocalDate.now());
        String query = "SELECT count(*) FROM userBookings WHERE date >= '" + currentDate + "' AND employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, singleton.getUser());
            resultSet = preparedStatement.executeQuery();
            int returnedRows = 0;
            if (resultSet.next())
                returnedRows = resultSet.getInt("count(*)");
            query = "SELECT userBookings.deskID, userBookings.date, states.stateName FROM userBookings INNER JOIN states ON userBookings.state = states.stateID WHERE date >= '" + currentDate + "' AND employeeID = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, singleton.getUser());
            resultSet = preparedStatement.executeQuery();
            if (returnedRows > 0) {
                ManageBookingTableRow bookingTableRow[] = new ManageBookingTableRow[returnedRows];
                int i = 0;
                while (resultSet.next()) {
                    bookingTableRow[i] = new ManageBookingTableRow(resultSet.getString("stateName"), resultSet.getString("deskID"), resultSet.getString("date"));
                    i++;
                }
                if (i == 0)
                    return null;
                else
                    return bookingTableRow;
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }


    // Removes a booking when the user chooses to remove a booking
    public void removeBooking(LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "DELETE FROM userBookings WHERE employeeID = ? AND date = ?;";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, singleton.getUser());
            preparedStatement.setString(2, String.valueOf(date));
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    // This checks if the user already has a booking on the selected date.
    public String alreadyBooked(LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String checkAlreadyBooked = "SELECT deskID FROM userBookings WHERE date = ? AND employeeID = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(date));
            preparedStatement.setInt(2, singleton.getUser());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return "alreadyBooked";
            }
        } catch (Exception e) {
            return "error";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return "noBookings";
    }


    // When the user logs in and enters the Manage bookings page, once and only once, this method is ran to clear up the
    // database, as it deletes all old entries THAT WERE NOT ACCEPTED, and only for the logged in user, since the
    // bookings that weren't accepted aren't needed.
    public void deleteOldEntries() throws SQLException {
        PreparedStatement preparedStatement = null;
        String tomorrowDate = String.valueOf(LocalDate.now().plusDays(1));
        String insertQuery = "DELETE FROM userBookings WHERE employeeID = ? AND date <= ? AND state = 'review';";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, singleton.getUser());
            preparedStatement.setString(2, tomorrowDate);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }

    }

}
