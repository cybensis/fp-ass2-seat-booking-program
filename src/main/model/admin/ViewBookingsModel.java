package main.model.admin;

import main.SQLConnection;
import main.Singleton;
import main.controller.admin.BookingTableRow;

import java.sql.*;
import java.time.LocalDate;

public class ViewBookingsModel {
    private Singleton singleton = Singleton.getInstance();
    private final static int TABLE_ELEMENTS = 4;

    public BookingTableRow[] getBookings(String state) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String currentDate = String.valueOf(LocalDate.now());
        String query = "SELECT count(deskID) FROM userBookings WHERE date >= '" + currentDate + "' AND state = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, state);
            resultSet = preparedStatement.executeQuery();
            int returnedRows = 0;
            if (resultSet.next())
                    returnedRows = resultSet.getInt("count(deskID)");
            query = "SELECT user.employeeID, user.firstName, user.lastName, userBookings.deskID, userBookings.date FROM userBookings INNER JOIN user ON userBookings.employeeID = user.employeeID WHERE date >= '" + currentDate + "' AND state = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, state);
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
                return null;
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

    public String acceptBooking(String employeeID, String date) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;

        String query = "UPDATE userBookings SET state = 'active' WHERE date = ? AND employeeID = ? AND state = 'review'";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, employeeID);
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

    public String removeBooking(String employeeID, String date) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "DELETE FROM userBookings WHERE employeeID = ? AND date = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            preparedStatement.setString(2, date);
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