package main.model.user;

import main.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreateBookingModel {

    Connection connection;

    public CreateBookingModel(){

        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);

    }

    public int getLastSatDesk(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        try {
            String query = "SELECT deskID FROM userBookings WHERE date = (SELECT MAX(date) FROM userBookings WHERE username = ?);";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("deskID");
            }
            return 0;
        }
        catch (Exception e)
        {
            return 0;
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    public ArrayList<Integer> blacklistedDesks(LocalDate date, String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        ArrayList<Integer> tablesBooked = new ArrayList<>();
        try {
            String query = "SELECT deskID FROM userBookings WHERE date = ? AND state = 'active';";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(date));
            resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                tablesBooked.add(resultSet.getInt("deskID"));
            }
            return tablesBooked;
        }
        catch (Exception e)
        {
            return tablesBooked;
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }

    }

    public String addBooking(String username, int deskid, LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "INSERT INTO userBookings (deskID, username, date, state) VALUES (?,?,?,'review')";
        preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, deskid);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3,  String.valueOf(date));
        int response = preparedStatement.executeUpdate();
        return "Success";
    }

    public String updateBooking(String username, int deskid, LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "UPDATE userBookings SET deskID = ?, username = ?, date = ?, state = 'review' WHERE username  = ? AND date = ?";
        preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setInt(1, deskid);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3,  String.valueOf(date));
        preparedStatement.setString(4, username);
        preparedStatement.setString(5,  String.valueOf(date));
        int response = preparedStatement.executeUpdate();
        return "Success";
    }

}
