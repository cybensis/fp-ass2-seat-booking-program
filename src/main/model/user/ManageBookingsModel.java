package main.model.user;


import main.Singleton;
import main.controller.user.ManageBookingTableRow;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class ManageBookingsModel {

    private Singleton singleton = Singleton.getInstance();

    public ManageBookingTableRow[] getUserBookings(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String currentDate = String.valueOf(LocalDate.now());
        int employeeID = getEmployeeID(username);
        String query = "SELECT count(*) FROM userBookings WHERE date >= '" + currentDate + "' AND employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, employeeID);
            resultSet = preparedStatement.executeQuery();
            int returnedRows = 0;
            if (resultSet.next())
                returnedRows = resultSet.getInt("count(*)");
            query = "SELECT deskID, date, state FROM userBookings WHERE date >= '" + currentDate + "' AND employeeID = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, employeeID);
            resultSet = preparedStatement.executeQuery();
            if (returnedRows > 0) {
                ManageBookingTableRow bookingTableRow[] = new ManageBookingTableRow[returnedRows];
                int i = 0;
                while (resultSet.next()) {
                    bookingTableRow[i] = new ManageBookingTableRow(resultSet.getString("state"),resultSet.getString("deskID"), resultSet.getString("date"));
                    i++;
                }
                if (i == 0)
                    return null;
                else
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


    public void removeBooking(String username, LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        int employeeID = getEmployeeID(username);
        String insertQuery = "DELETE FROM userBookings WHERE employeeID = ? AND date = ?;";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setInt(1, employeeID);
            preparedStatement.setString(2, String.valueOf(date));
            preparedStatement.executeUpdate();
        }
        catch (SQLException error) {
            //Nothing
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
        preparedStatement.close();
    }


    public int getEmployeeID(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT employeeID FROM user WHERE username = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            int employeeID = resultSet.getInt("employeeID");
            return employeeID;

        }
        catch (SQLException error) {
            return -1;
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public String alreadyBooked(LocalDate date, String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int employeeID = getEmployeeID(username);
        try {
            String checkAlreadyBooked = "SELECT deskID FROM userBookings WHERE date = ? AND employeeID = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(date));
            preparedStatement.setInt(2, employeeID);
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


    public void updateBooking(String username, LocalDate oldDate, LocalDate newDate) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int employeeID = getEmployeeID(username);
        String insertQuery = "UPDATE userBookings SET date = ? WHERE employeeID = ? AND date = ?;";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, String.valueOf(newDate));
            preparedStatement.setInt(2, employeeID);
            preparedStatement.setString(3, String.valueOf(oldDate));
            preparedStatement.executeUpdate();
        }
        catch (SQLException error) {
            System.out.println(error);
            //Nothing
        }
        finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

}
