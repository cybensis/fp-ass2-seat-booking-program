package main.model.admin;

import main.Singleton;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadReportModel {
    private Singleton singleton = Singleton.getInstance();

    public String[] getBookingData() throws SQLException {
        PreparedStatement preparedStatement = null;
        // Naming this rs to make the array assignment shorter and more readable.
        ResultSet rs=null;
        String bookingData[];
        String query = "SELECT count(*) FROM userBookings WHERE date >= ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            rs = preparedStatement.executeQuery();
            bookingData = new String[rs.getInt("count(*)")];
            query = "SELECT * FROM userBookings INNER JOIN states ON userBookings.state = states.stateID WHERE date >= ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(singleton.getDate()));
            rs = preparedStatement.executeQuery();
            int i = 0;
            while (rs.next()) {
                bookingData[i] = rs.getString("deskID") + "," + rs.getString("employeeID") + "," + rs.getString("date") + "," + rs.getString("stateName");
                i++;
            }
            return bookingData;
        }
        catch (SQLException error)
        {
            bookingData = null;
            return bookingData;
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (rs != null)
                rs.close();
        }
    }


    public String[] getEmployeeData() throws SQLException {
        PreparedStatement preparedStatement = null;
        // Naming this rs to make the array assignment shorter and more readable.
        ResultSet rs=null;
        String employeeData[];
        String query = "SELECT count(*) FROM user;";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            rs = preparedStatement.executeQuery();
            employeeData = new String[rs.getInt("count(*)")];
            query = "SELECT * FROM user INNER JOIN states ON user.accountState = states.stateID;";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            rs = preparedStatement.executeQuery();
            int i = 0;
            while (rs.next()) {
                // I skip 10 and 11, because this selects all from both tables, I only want the name of the accounts state, which columns 10 and 11 only holds the id
                employeeData[i] = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getString(6) + "," + rs.getString(7) + "," + rs.getString(8) + "," + rs.getString(9) + "," + rs.getString(12);
                i++;
            }
            return employeeData;
        }
        catch (SQLException error)
        {
            employeeData = null;
            return employeeData;
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (rs != null)
                rs.close();
        }
    }
}

