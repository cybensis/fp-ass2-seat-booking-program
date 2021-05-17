package main.model.user;

import main.SQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ManageBookingsModel {

    Connection connection;

    public ManageBookingsModel(){

        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);

    }

    public ArrayList<ArrayList<String>> getUserBookings(String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        ArrayList<ArrayList<String>> tablesBooked = new ArrayList<>();

        try {

            String insertQuery = "SELECT seatID, date FROM userBookings WHERE username = ? AND date >= ?;";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, String.valueOf(username));
            preparedStatement.setString(2, String.valueOf(LocalDate.now()));
            resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                tablesBooked.add(new ArrayList());
                tablesBooked.get(i).add(resultSet.getString("seatID"));
                tablesBooked.get(i).add(resultSet.getString("date"));
                i++;
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


    public void removeBooking(String username, LocalDate date) throws SQLException {
        PreparedStatement preparedStatement = null;
        String insertQuery = "DELETE FROM userBookings WHERE username = ? AND date = ?;";
        preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, String.valueOf(date));
        int response = preparedStatement.executeUpdate();
        //return "Success";
    }
}
