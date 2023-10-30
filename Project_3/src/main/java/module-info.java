module me.jackson.project_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens me.jackson.project_3 to javafx.fxml;
    exports me.jackson.project_3;
}