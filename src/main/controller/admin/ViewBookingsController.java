package main.controller.admin;

import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.admin.ViewBookingsModel;

import java.io.IOException;
import java.sql.SQLException;

public class ViewBookingsController {
    private Singleton singleton = Singleton.getInstance();
    private ViewBookingsModel viewBookingsModel = new ViewBookingsModel();
    private BookingTableRow selectedRow = null;

    @FXML
    private Text subHeader;

    @FXML
    private ImageView backButton;

    @FXML
    private Button acceptButton;

    @FXML
    private Text errorMessage;

    @FXML
    private Button rejectButton;

    @FXML
    private Button removeButton;

    @FXML
    private TableView<BookingTableRow> bookingTable;

    @FXML
    private TableColumn<BookingTableRow,String> employeeIDColumn;

    @FXML
    private TableColumn<BookingTableRow, String> nameColumn;

    @FXML
    private TableColumn<BookingTableRow, String> deskIDColumn;

    @FXML
    private TableColumn<BookingTableRow, String> dateColumn;


    @FXML
    private void initialize() throws SQLException {
        BookingTableRow bookingTableRows[];
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        deskIDColumn.setCellValueFactory(new PropertyValueFactory<>("deskID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookingTable.selectionModelProperty().addListener((Observable observable) -> {
            int index = bookingTable.getSelectionModel().getSelectedIndex();
            BookingTableRow row = bookingTable.getItems().get(index);
        });
        if (singleton.getViewBookingsType().equals("existingBookings")) {
            subHeader.setText("View existing bookings");
            bookingTableRows = viewBookingsModel.getBookings("active");
        }
        else {
            subHeader.setText("View booking requests");
           bookingTableRows = viewBookingsModel.getBookings("review");
        }
        if (bookingTableRows != null) {
            for (int i = 0; i < bookingTableRows.length; i++) {
                bookingTable.getItems().add(bookingTableRows[i]);
            }
        }
        else {
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void acceptButton(MouseEvent event) throws SQLException {
        if (selectedRow != null) {
            viewBookingsModel.acceptBooking();
            bookingTable.getItems().remove(selectedRow);
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/bookingManagement.fxml");
    }

    @FXML
    private void removeEntry(MouseEvent event) throws SQLException {
        if (selectedRow != null) {
            viewBookingsModel.removeBooking();
            bookingTable.getItems().remove(selectedRow);
        }
    }

    @FXML
    private void selectRow(MouseEvent event) {
        try {
        String testForNull = bookingTable.getSelectionModel().getSelectedItem().getDate();
        }
        catch (NullPointerException error) {
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            selectedRow = null;
            return;
        }
        if (singleton.getViewBookingsType().equals("existingBookings")) {
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            removeButton.setVisible(true);
        }
        else {
            acceptButton.setVisible(true);
            rejectButton.setVisible(true);
        }
        selectedRow = bookingTable.getSelectionModel().getSelectedItem();
    }
}
