package main.controller;

public class Employee {
    private String firstName;
    private String surname;
    private int  employeeID;
    private String username;
    private String password;
    private String secretQ;
    private String secretQAnswer;
    private String role;
    private int accountTypeID;

    public Employee(Integer employeeID, String role, String firstName, String surname, String username, String password, String secretQ, String secretQAnswer, int accountTypeID) {
        this.firstName = firstName;
        this.surname = surname;
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.secretQ = secretQ;
        this.secretQAnswer = secretQAnswer;
        this.role = role;
        this.accountTypeID = accountTypeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretQ() {
        return secretQ;
    }

    public String getSecretQAnswer() {
        return secretQAnswer;
    }

    public String getRole() {
        return role;
    }

    public int getAccountTypeID() {
        return accountTypeID;
    }
}
