package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.admin.DownloadReportModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class DownloadReportController {
    private Singleton singleton = Singleton.getInstance();
    private DownloadReportModel downloadReportModel = new DownloadReportModel();

    @FXML
    private ImageView backButton;

    @FXML
    private Text subHeader;

    @FXML
    private Text errorMessage;

    @FXML
    private Text successMessage;

    @FXML
    private void initialize() {
        if (singleton.getAdminDateType().equals("generateReports"))
            subHeader.setText("Generate reports for bookings on " + singleton.getDate());
    }

    @FXML
    private void download(MouseEvent event) throws SQLException, IOException {
        String reportData[];
        successMessage.setVisible(false);
        FileWriter writer = new FileWriter("temp.csv");
        // This means a date was chosen, and a date is only chosen if the user is generating reports for bookings
        if (singleton.getAdminDateType().equals("generateReports")) {
            reportData = downloadReportModel.getBookingData(singleton.getDate());
            if (reportData == null) {
                errorMessage.setText("An unexpected error has occurred");
                errorMessage.setVisible(true);
                return;
            }
            writer.write("Desk ID, Employee ID, Date, State" + System.getProperty("line.separator"));
            for (int i = 0; i < reportData.length; i++) {
                writer.write(reportData[i] + System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();

        }
        else {
            reportData = downloadReportModel.getEmployeeData();
            if (reportData == null) {
                errorMessage.setText("An unexpected error has occurred");
                errorMessage.setVisible(true);
                return;
            }
            writer.write("First name, Surname, Password Hash, Employee ID, Secret question, Answer, Username, Employee role, Account type" + System.getProperty("line.separator"));
            for (int i = 0; i < reportData.length; i++) {
                writer.write(reportData[i] + System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
        }
        saveFile();

    }


    private void saveFile() {
        JFileChooser pathChooser = new JFileChooser();
        pathChooser.setDialogTitle("Specify a file to save");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Please choose the directory and name for your file", "csv");
        pathChooser.setFileFilter(filter);
        int returnVal = pathChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String chosenFilePath = pathChooser.getSelectedFile() + ".csv";
            File checkFile = new File(chosenFilePath);
            if (checkFile.exists()) {
                errorMessage.setText("Error: You must choose a non-existing file");
                errorMessage.setVisible(true);
                successMessage.setVisible(false);
                return;
            }
            File tempFile = new File("temp.csv");
            tempFile.renameTo(new File(chosenFilePath));
        }
        else {
            errorMessage.setText("Error: An unexpected error occurred, please try a different directory");
            successMessage.setVisible(false);
            errorMessage.setVisible(true);
            return;
        }
        errorMessage.setVisible(false);
        successMessage.setVisible(true);


    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.setAdminDateType("");
        singleton.changeScene("main/ui/admin/generateReports.fxml");
    }

}