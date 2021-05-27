package main.controller.admin;

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

public class GraphicVisualisationController {
    static final int NEIGHBOR_DESK_OFFSET = 1;

    private Singleton singleton = Singleton.getInstance();
    // Reusing CreateBookingModel from the basic user side.
    private CreateBookingModel createBookingModel = new CreateBookingModel();
    private Rectangle[] rectangleContainer;
    ArrayList<Integer> tablesBooked;

    @FXML
    private ImageView backButton;

    @FXML
    private Text subHeader;

    @FXML
    private Rectangle seat_0, seat_1, seat_2, seat_3, seat_4, seat_5, seat_6, seat_7, seat_8, seat_9, seat_10, seat_11, seat_12, seat_13, seat_14, seat_15;

    @FXML
    private void initialize() throws SQLException, IOException {
        rectangleContainer = new Rectangle[]{seat_0, seat_1, seat_2, seat_3, seat_4, seat_5, seat_6, seat_7, seat_8, seat_9, seat_10, seat_11, seat_12, seat_13, seat_14, seat_15};
        LocalDate chosenDate = singleton.getDate();
        subHeader.setText("Graphic visualisation - " + chosenDate);
        String seatingStatus = createBookingModel.getSeatingStatus(chosenDate);
        tablesBooked = createBookingModel.blacklistedDesks(chosenDate, singleton.getUser());
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
            }
    }


    private void disableSeat(int deskID, String condition) {
        if (!rectangleContainer[deskID].isDisabled() && condition.equals("covid"))
            rectangleContainer[deskID].setFill(Paint.valueOf("ORANGE"));
        else if (!rectangleContainer[deskID].isDisabled() && condition.equals("booked"))
            rectangleContainer[deskID].setFill(Paint.valueOf("RED"));
        rectangleContainer[deskID].setDisable(true);
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/user/chooseDate.fxml");

    }


}
