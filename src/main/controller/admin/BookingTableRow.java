package main.controller.admin;

public class BookingTableRow {
    private final String employeeID;
    private final String fullName;
    private final String deskID;
    private final String date;

    public BookingTableRow(String employeeID, String fullName, String deskID, String date) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.deskID = deskID;
        this.date = date;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDeskID() {
        return deskID;
    }

    public String getDate() {
        return date;
    }
}
