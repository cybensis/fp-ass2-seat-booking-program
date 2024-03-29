package main.controller.user;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Singleton;
import main.model.user.CreateBookingModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateBookingController {
    private static final int NEIGHBOR_DESK_OFFSET = 1;

    private Singleton singleton = Singleton.getInstance();
    private CreateBookingModel createBookingModel = new CreateBookingModel();
    private Rectangle[] rectangleContainer;
    private ArrayList<Integer> tablesBooked;

    @FXML
    private Button accept;

    @FXML
    private Button cancel;

    @FXML
    private ImageView backButton;

    @FXML
    private Text sceneHeader;

    @FXML
    private Rectangle seat_0, seat_1, seat_2, seat_3, seat_4, seat_5, seat_6, seat_7, seat_8, seat_9, seat_10, seat_11, seat_12, seat_13, seat_14, seat_15;

    @FXML
    private void initialize() throws SQLException, IOException {
        rectangleContainer = new Rectangle[]{seat_0, seat_1, seat_2, seat_3, seat_4, seat_5, seat_6, seat_7, seat_8, seat_9, seat_10, seat_11, seat_12, seat_13, seat_14, seat_15};
        LocalDate chosenDate = singleton.getDate();
        String sceneHeaderText = (singleton.getUpdateBooking()) ? "Update booking - " + chosenDate : "Create a new booking - " + chosenDate;
        sceneHeader.setText(sceneHeaderText);

        // These 6 lines get the seating status and blocked seats for the current user
        String seatingStatus = createBookingModel.getSeatingStatus();
        int pastAndFutureBookedDesks[] = createBookingModel.getUsersBlockedDesks();
        int blockedNeighborDesks[] = new int[0];
        if (pastAndFutureBookedDesks != null)
            blockedNeighborDesks = createBookingModel.getBlockedNeighborDesks(rectangleContainer.length, pastAndFutureBookedDesks[0]);
        tablesBooked = createBookingModel.blacklistedDesks();

        if (seatingStatus.equals("Error") || pastAndFutureBookedDesks == null || tablesBooked == null || blockedNeighborDesks == null) {
            sceneHeader.setText("An unexpected error has occurred, please try again");
            return;
        }
        createDeskClickEvent();
        int deskID;
        if (seatingStatus.equals("COVID Conditions")) {
            for (int i = 0; i < tablesBooked.size(); i++) {
                deskID = tablesBooked.get(i);
                disableSeat(deskID, "booked");
                // The below code block changes the neighboring seats of already booked seats to orange, to represent
                // the COVID conditions that disallow people to sit right next to each other by applying -1 or +1 to
                // the current deskID. The reason for this if statement is to make sure that no illegal array index
                // is accessed, e.g., if deskID is 0, and - 1 is applied to retrieve the corresponding
                // rectangleContainer for its neighbor desk, then it tries to access index -1 which is illegal, and
                // the same goes for trying to access the max value + 1.
                if (deskID > 0 && deskID < rectangleContainer.length) {
                    disableSeat(deskID - NEIGHBOR_DESK_OFFSET, "covid");
                    disableSeat(deskID + NEIGHBOR_DESK_OFFSET, "covid");
                }
            }
        } else if (seatingStatus.equals("Lockdown")) {
            for (int i = 0; i < rectangleContainer.length; i++) {
                disableSeat(i, "covid");
            }
        } else if (seatingStatus.equals("Normal"))
            for (int i = 0; i < tablesBooked.size(); i++) {
                disableSeat(tablesBooked.get(i), "booked");

                // This for each block, blocks the two neighboring seats, of employees who the current user last sat
                // next to, but this only applies when normal conditions are active, since under COVID conditions, there
                // can be no sitting next to one another.
                for (int desk : blockedNeighborDesks) {
                    if (desk >= 0)
                        disableSeat(desk, "booked");
                }
            }
        // This blocks the most recent seat the user booked, both in the past and future, if the user has no past or
        // future bookings then the array will keep the default initialized values of -1, hence the if (desk >= 0)
        for (int desk : pastAndFutureBookedDesks) {
            if (desk >= 0)
                disableSeat(desk, "booked");
        }

    }

    // This creates an event that allows a user to click on a rectangle which will initiate a booking confirmation.
    private void createDeskClickEvent() {
        for (int i = 0; i < rectangleContainer.length; i++) {
            int deskID = i;
            rectangleContainer[i].setOnMouseClicked(event -> {
                singleton.setChosenDesk(deskID);
                Node source = (Node) event.getSource();
                Stage popup = new Stage();
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.initOwner(source.getScene().getWindow());
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/user/createBookingPopup.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene popupScene = new Scene(root, 600, 300);
                popup.setScene(popupScene);
                popup.show();
                popup.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Singleton singleton = Singleton.getInstance();
                        try {
                            singleton.changeScene("main/ui/user/userHome.fxml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
        }
    }

    // This will block a seat to prevent it from being clicked and booked, and can be blocked under 2 conditions
    private void disableSeat(int deskID, String condition) {
        if (!rectangleContainer[deskID].isDisabled() && condition.equals("covid"))
            rectangleContainer[deskID].setFill(Paint.valueOf("ORANGE"));
        else if (!rectangleContainer[deskID].isDisabled() && condition.equals("booked"))
            rectangleContainer[deskID].setFill(Paint.valueOf("RED"));
        rectangleContainer[deskID].setDisable(true);
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.setUpdateBooking(false);
        singleton.changeScene("main/ui/user/userHome.fxml");

    }


}
