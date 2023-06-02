module atomicx.timetable {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens atomicx.timetable to javafx.fxml;
    exports atomicx.timetable;
}