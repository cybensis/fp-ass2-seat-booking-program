package main.model.admin;

import main.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ChooseDateModel {
    private Singleton singleton = Singleton.getInstance();

    public String alreadyBooked(LocalDate date, String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //if (date is today or less cos must be the day after minimum for admin to accept)
        try {
            String checkAlreadyBooked = "SELECT seatID FROM userBookings WHERE date = ? AND username = ?;";
            preparedStatement = singleton.getConnection().prepareStatement(checkAlreadyBooked);
            preparedStatement.setString(1, String.valueOf(date));
            preparedStatement.setString(2, username);
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
        //int employeeID = getEmployeeID(username);
        //System.out.println(employeeID);
        System.out.println(oldDate);
        System.out.println(newDate);
        String insertQuery = "UPDATE userBookings SET date = ? WHERE employeeID = ? AND date = ?;";
        //String insertQuery = "SELECT * FROM userBookings WHERE employeeID = ? AND date = ?;";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, String.valueOf(newDate));
            preparedStatement.setInt(2, 2);
            preparedStatement.setString(2, String.valueOf(oldDate));
            preparedStatement.executeUpdate();
            //System.out.println(resultSet.getString("date"));
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
}
