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
import java.util.ArrayList;

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
        singleton.setDate(LocalDate.parse(selectedRow.getDate()));
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
            String testForNull = this.bookingTable.getSelectionModel().getSelectedItem().getDate();
        }
        catch (NullPointerException error) {
            this.removeButton.setVisible(false);
            this.updateButton.setVisible(false);
            this.selectedRow = null;
            this.singleton.setDate(null);
            return;
        }
        LocalDate currentDate = LocalDate.now();
        this.removeButton.setVisible(true);
        this.selectedRow = this.bookingTable.getSelectionModel().getSelectedItem();
        this.singleton.setDate(LocalDate.parse(this.selectedRow.getDate()));
        if (ChronoUnit.DAYS.between(currentDate, LocalDate.parse(this.selectedRow.getDate())) > 2)
            this.updateButton.setVisible(true);
        else
            this.updateButton.setVisible(false);
    }



    @FXML
    private void initialize() throws SQLException {
        ManageBookingTableRow userBookings[] = manageBookingsModel.getUserBookings(singleton.getUser());
        this.deskIDColumn.setCellValueFactory(new PropertyValueFactory<>("deskID"));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.bookingTable.selectionModelProperty().addListener((Observable observable) -> {
            int index = this.bookingTable.getSelectionModel().getSelectedIndex();
            ManageBookingTableRow row = bookingTable.getItems().get(index);
        });
        if (userBookings != null) {
            for (int i = 0; i < userBookings.length; i++) {
                this.bookingTable.getItems().add(userBookings[i]);
            }
        }
    }

}