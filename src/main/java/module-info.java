module org.example.inpexamfinal {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.inpexamfinal to javafx.fxml;
    exports org.example.inpexamfinal;
}