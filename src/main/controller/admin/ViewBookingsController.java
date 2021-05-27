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
        // Maybe a good idea to retrieve all submissions, past and present, and anything less than 2 days from now
        // should be deleted.
        BookingTableRow bookingTableRows[];
        this.employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        this.deskIDColumn.setCellValueFactory(new PropertyValueFactory<>("deskID"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.bookingTable.selectionModelProperty().addListener((Observable observable) -> {
            int index = this.bookingTable.getSelectionModel().getSelectedIndex();
            BookingTableRow row = bookingTable.getItems().get(index);
            System.out.println(index);
        });
        if (this.singleton.getViewBookingsType().equals("existingBookings")) {
            this.subHeader.setText("View existing bookings");
            bookingTableRows = this.viewBookingsModel.getBookings("active");
        }
        else {
            this.subHeader.setText("View booking requests");
           bookingTableRows = this.viewBookingsModel.getBookings("review");
        }
        if (bookingTableRows != null) {
            for (int i = 0; i < bookingTableRows.length; i++) {
                this.bookingTable.getItems().add(bookingTableRows[i]);

            }
        }
    }

    @FXML
    private void acceptButton(MouseEvent event) throws SQLException {
        if (this.selectedRow != null) {
            this.viewBookingsModel.acceptBooking(String.valueOf(this.selectedRow.getEmployeeID()), this.selectedRow.getDate());
            this.bookingTable.getItems().remove(this.selectedRow);
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        this.singleton.changeScene("main/ui/admin/bookingManagement.fxml");
    }

    @FXML
    private void removeEntry(MouseEvent event) throws SQLException {
        if (this.selectedRow != null) {
            this.viewBookingsModel.removeBooking(String.valueOf(this.selectedRow.getEmployeeID()), this.selectedRow.getDate());
            this.bookingTable.getItems().remove(this.selectedRow);
        }
    }

    @FXML
    private void selectRow(MouseEvent event) {
        try {
        String testForNull = this.bookingTable.getSelectionModel().getSelectedItem().getDate();
        }
        catch (NullPointerException error) {
            this.acceptButton.setVisible(false);
            this.rejectButton.setVisible(false);
            this.selectedRow = null;
            return;
        }
        if (this.singleton.getViewBookingsType().equals("existingBookings")) {
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            removeButton.setVisible(true);
        }
        else {
            this.acceptButton.setVisible(true);
            this.rejectButton.setVisible(true);
        }
        this.selectedRow = this.bookingTable.getSelectionModel().getSelectedItem();
    }
}