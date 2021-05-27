package main.controller.user;

public class ManageBookingTableRow {
    private String state;
    private String deskID;
    private String date;

    public ManageBookingTableRow(String state, String deskID, String date) {
        this.deskID = deskID;
        this.date = date;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getDeskID() {
        return deskID;
    }

    public String getDate() {
        return date;
    }
}
