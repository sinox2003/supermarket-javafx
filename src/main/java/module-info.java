module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires MaterialFX;
    requires AnimateFX;
    requires fontawesomefx;


    opens com.example.project to javafx.fxml;
    exports com.example.project;

}