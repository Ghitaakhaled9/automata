module com.example.automatefini {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.automatefini to javafx.fxml;
    exports com.example.automatefini;
}
