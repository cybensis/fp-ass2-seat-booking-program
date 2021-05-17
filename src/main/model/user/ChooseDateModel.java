package main.model.user;

import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChooseDateModel {
    Connection connection;

    public ChooseDateModel() {

        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);

    }

    public String alreadyBooked(LocalDate date, String username) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //if (date is today or less cos must be the day after minimum for admin to accept)
        try {
            String checkAlreadyBooked = "SELECT seatID FROM userBookings WHERE date = ? AND username = ?;";
            preparedStatement = connection.prepareStatement(checkAlreadyBooked);
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
        return "nuffin";
    }
}
