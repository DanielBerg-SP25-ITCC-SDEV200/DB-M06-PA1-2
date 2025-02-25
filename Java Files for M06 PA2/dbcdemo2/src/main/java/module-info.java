module com.example.dbcdemo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;



    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.dbcdemo2 to javafx.fxml;
    exports com.example.dbcdemo2;
}