package main.controller.user;

import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Singleton;
import main.model.user.ManageBookingsModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ManageBookingsController {
    private Singleton singleton = Singleton.getInstance();
    private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();
    private ManageBookingTableRow selectedRow = null;

    @FXML
    private Text subHeader;

    @FXML
    private ImageView backButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button updateButton;

    @FXML
    private Text errorMessage;

    @FXML
    private TableView<ManageBookingTableRow> bookingTable;

    @FXML
    private TableColumn<ManageBookingTableRow, String> deskIDColumn;

    @FXML
    private TableColumn<ManageBookingTableRow, String> stateColumn;

    @FXML
    private TableColumn<ManageBookingTableRow, String> dateColumn;

    @FXML
    private void updateBooking(MouseEvent event) throws IOException, SQLException {
        singleton.setBookingDate(LocalDate.parse(selectedRow.getDate()));
        singleton.setUpdateBooking(true);
        singleton.changeScene("main/ui/user/chooseDate.fxml");

    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/user/userHome.fxml");
    }

    @FXML
    private void deleteBooking(MouseEvent event) throws SQLException {
        Node source = (Node) event.getSource();
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(source.getScene().getWindow());
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/user/manageBookingsPopup.fxml"));
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
    }

    @FXML
    private void selectRow(MouseEvent event) {
        try {
            String testForNull = bookingTable.getSelectionModel().getSelectedItem().getDate();
        }
        catch (NullPointerException error) {
            removeButton.setVisible(false);
            updateButton.setVisible(false);
            selectedRow = null;
            singleton.setDate(null);
            return;
        }
        LocalDate currentDate = LocalDate.now();
        removeButton.setVisible(true);
        selectedRow = bookingTable.getSelectionModel().getSelectedItem();
        singleton.setDate(LocalDate.parse(selectedRow.getDate()));
        if (ChronoUnit.DAYS.between(currentDate, LocalDate.parse(selectedRow.getDate())) >= 2)
            updateButton.setVisible(true);
        else
            updateButton.setVisible(false);
    }



    @FXML
    private void initialize() throws SQLException {
        if (!singleton.getHasClearedBookings()) {
            manageBookingsModel.deleteOldEntries();
            singleton.setHasClearedBookings(true);
        }
        ManageBookingTableRow userBookings[] = manageBookingsModel.getUserBookings();
        deskIDColumn.setCellValueFactory(new PropertyValueFactory<>("deskID"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookingTable.selectionModelProperty().addListener((Observable observable) -> {
            int index = bookingTable.getSelectionModel().getSelectedIndex();
            ManageBookingTableRow row = bookingTable.getItems().get(index);
        });
        if (userBookings != null) {
            for (int i = 0; i < userBookings.length; i++) {
                bookingTable.getItems().add(userBookings[i]);
            }
        }
    }

}
