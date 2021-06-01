package main.model.admin;

import main.SQLConnection;
import main.Singleton;
import main.controller.admin.BookingTableRow;

import java.sql.*;
import java.time.LocalDate;

public class ViewBookingsModel {
    private Singleton singleton = Singleton.getInstance();
    private final static int TABLE_ELEMENTS = 4;

    // This method starts by getting the count of active bookings for a specific date, then initialising an array with
    // that number, then doing another query to return the actual data for each booking, and filling the array with said
    // data.
    public BookingTableRow[] getBookings(String state) throws SQLException {
        int stateID = 0;
        if (state.equals("active"))
            stateID = 1;
        else
            stateID = 2;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String currentDate = String.valueOf(LocalDate.now());
        String query = "SELECT count(deskID) FROM userBookings WHERE date >= '" + currentDate + "' AND state = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, stateID);
            resultSet = preparedStatement.executeQuery();
            int returnedRows = 0;
            if (resultSet.next())
                    returnedRows = resultSet.getInt("count(deskID)");
            query = "SELECT user.employeeID, user.firstName, user.lastName, userBookings.deskID, userBookings.date FROM userBookings INNER JOIN user ON userBookings.employeeID = user.employeeID WHERE date >= '" + currentDate + "' AND state = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, stateID);
            resultSet = preparedStatement.executeQuery();
            if (returnedRows > 0) {
                BookingTableRow bookingTableRow[] = new BookingTableRow[returnedRows];
                int i = 0;
                while (resultSet.next()) {
                    bookingTableRow[i] = new BookingTableRow(resultSet.getString("employeeID"),resultSet.getString("firstName") + " " + resultSet.getString("lastName"),resultSet.getString("deskID"),resultSet.getString("date"));
                    i++;
                }
                return bookingTableRow;
            }
            else
                return new BookingTableRow[]{null};
            // In ViewBookingsController, I check if this method returns null to check for errors, but having no
            // booking requests would also be null, but having a null array element is very different to having a
            // null array, so I use this to distinguish between an error and no booking requests.
        }
        catch (Exception e)
        {
            return null;
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    // This is used by admins to accept bookings, by changing the state from 3 (review) to 1 (active).
    public String acceptBooking() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "UPDATE userBookings SET state = 1 WHERE date = ? AND employeeID = ? AND state = 2";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            preparedStatement.setInt(2, singleton.getUser());
            preparedStatement.executeUpdate();
            return "success";
        }
        catch (SQLException e)
        {
            return "errorOccured";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    // This is used by admins and basic employees to remove bookings
    public String removeBooking() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "DELETE FROM userBookings WHERE employeeID = ? AND date = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, singleton.getUser());
            preparedStatement.setString(2, String.valueOf(singleton.getDate()));
            preparedStatement.executeUpdate();
            return "success";
        }
        catch (SQLException e)
        {
            return "errorOccured";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}