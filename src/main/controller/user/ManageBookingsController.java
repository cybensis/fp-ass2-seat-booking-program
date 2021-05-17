package main.controller.user;

import javafx.fxml.FXML;
import main.Singleton;
import main.model.user.ManageBookingsModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ManageBookingsController {
    private Singleton singleton = Singleton.getInstance();
    private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();
    private LocalDate chosenBooking;


    @FXML
    private void initialize(String args[]) throws SQLException {
        ArrayList<ArrayList<String>> str = manageBookingsModel.getUserBookings(singleton.getUser());
        for (int i = 0; i < str.size(); i++) {
            //Add entries to table with date and seatid, then provide it with onClick functions that select it, maybe add it
            // to singleton as selectedEntry, then
            System.out.println(str.get(i).get(0));
            System.out.println(str.get(i).get(1));
        }
    }

    private void updateBooking() {
        //if date is 2 days in the future then change view to createBooking, and modify createBookingModel to not return
        //bookings by that user, since the user will never be able to view a booking  page on a day they already have
        // a booking unless through this exact means.
        // Maybe I'll have to set a variable in singleton cos of the popup, either the booking will need to be accepted
        // instantlly which will require modification of the popup, or make an admin review it, which will require a
        // change to that booking which will require a different sql statement hence the singleton variable
        singleton.setUpdateBooking(true);
    }

    private void cancelBooking() {
        //If booking is selected

        //else set error message
    }
}
