module com.prashantaryal.onlinebankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires servlet.api;
    requires javafx.baseEmpty;
    requires java.sql;
    requires org.json;

    opens com.prashantaryal.onlinebankingsystem to javafx.fxml;
    exports com.prashantaryal.onlinebankingsystem;

    opens com.prashantaryal.onlinebankingsystem.classes to javafx.baseEmpty;
    exports com.prashantaryal.onlinebankingsystem.classes;
}