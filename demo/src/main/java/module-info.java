module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.esnecca to javafx.fxml;
    exports de.esnecca;
}
