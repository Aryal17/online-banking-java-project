module com.prashantaryal.onlinebankingsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.prashantaryal.onlinebankingsystem to javafx.fxml;
    exports com.prashantaryal.onlinebankingsystem;
}