package atomicx.timetable;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Timetable extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 50;
    private static final int PADDING = 10;
    private static final int SPACING = 8;
    private static final int LABEL_FONT_SIZE = 20;
    private static final String LABEL_COLOR = "#FFFFFF";
    private final MFXButton[][] buttons = new MFXButton[ROWS][COLS];
    private final String[][] slots = {
            {"A1/SA2", "G1", "E1", "TD1", "A2/SA2", "TB2/SB2", "E2", "C2/SC2", "-", "-"},
            {"L1", "L2", "L3", "L4", "L21", "L22", "L23", "L24", "L25", "L26"},
            {"B1/SB1", "A1/SA1", "G1", "C1/SC1", "B2/SB2", "A2/SA2", "G2/STC2", "TD2", "-", "-"},
            {"L5", "L6", "L7", "L8", "L27", "L28", "L29", "L30", "L31", "L32"},
            {"TC1/SC1", "F1/STB1", "TA1/SA1", "E1", "TC2/SC2", "TG2/STB2", "TA2/SA2", "E2", "-", "-"},
            {"L9", "L10", "L11", "L12", "L33", "L34", "L35", "L36", "L37", "L38"},
            {"D1", "C1/SC1", "B1/SB1", "TF1/STA1", "D2", "C2/SC2", "B2/SB2", "F2/STA2", "-", "-"},
            {"L13", "L14", "L15", "L16", "L39", "L40", "L41", "L42", "L43", "L44"},
            {"TE1", "D1", "TG1/STC1", "TB1/SB1", "TE2", "D2", "TF2", "G2", "-", "-"},
            {"L17", "L18", "L19", "L20", "L45", "L46", "L49", "L50", "L47", "L48"}
    };
    private final GridPane grid = new GridPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Timetable");
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(PADDING));
        grid.setHgap(SPACING);
        grid.setVgap(SPACING);
        Label[] time = {new Label("09:00 - 09:50"), new Label("10:00 - 10:50"), new Label("11:00 - 11:50"), new Label("12:00 - 12:50"), new Label("14:00 - 14:50"), new Label("15:00 - 15:50"), new Label("16:00 - 16:50"), new Label("17:00 - 17:50"), new Label("18:00 - 18:50"), new Label("19:00 - 19:50")};
        Label[] days = {new Label("Tuesday"), new Label("Tuesday Lab"), new Label("Wednesday"), new Label("Wednesday Lab"), new Label("Thursday"), new Label("Thursday Lab"), new Label("Friday"), new Label("Friday Lab"), new Label("Saturday"), new Label("Saturday Lab")};
        for (int i = 0; i < time.length; i++) {
            time[i].setStyle("-fx-font-size: " + LABEL_FONT_SIZE + "; -fx-text-fill: " + LABEL_COLOR + ";");
            grid.add(time[i], i + 1, 0);
        }
        for (int i = 0; i < days.length; i++) {
            days[i].setStyle("-fx-font-size: " + LABEL_FONT_SIZE + "; -fx-text-fill: " + LABEL_COLOR + ";");
            grid.add(days[i], 0, i + 1);
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                if (slots[i][j].contains("/")) {
                    String[] parts = slots[i][j].split("/");
                    MFXButton button1 = new MFXButton(parts[0]);
                    MFXButton button2 = new MFXButton(parts[1]);
                    setButtonStyle(button1, BUTTON_WIDTH / 2);
                    setButtonStyle(button2, BUTTON_WIDTH / 2);

                    HBox hbox = new HBox(button1, button2);
                    grid.add(hbox, j + 1, i + 1);
                } else {
                    buttons[i][j] = new MFXButton(slots[i][j]);
                    setButtonStyle(buttons[i][j]);
                    grid.add(buttons[i][j], j + 1, i + 1);
                }
            }
        }
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #424242;");
        root.getChildren().add(grid);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setButtonStyle(MFXButton button, int buttonWidth) {
        button.setPrefSize(buttonWidth, BUTTON_HEIGHT);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF;");

        button.setOnAction(e -> {
            MFXButton b = (MFXButton) e.getSource();
            String slot = b.getText();
            if (!slot.equals("-")){
                toggleSlot(slot);
            }
        });
    }

    private void setButtonStyle(MFXButton button) {
        this.setButtonStyle(button, BUTTON_WIDTH);
    }

    private void toggleSlot(String slot) {
        if (slot.startsWith("T")) {
            slot = slot.substring(1);
        }
        for (int i = 1; i < ROWS + 1; i++) {
            for (int j = 1; j < COLS + 1; j++) {
                Node node = getNodeByCoordinate(i, j);

                if (node instanceof HBox hbox) {
                    for (Node child : hbox.getChildren()) {
                        toggleButton(child, slot);
                    }
                } else {
                    toggleButton(node, slot);
                }
            }
        }
    }

    private void toggleButton(Node node, String slot) {
        if (node instanceof MFXButton button) {
            String slotToCheck = button.getText().startsWith("T") ? button.getText().substring(1) : button.getText();
            if (slotToCheck.equals(slot)) {
                if (button.getStyle().equals("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF;")) {
                    button.setStyle("-fx-background-color: #FFC107; -fx-text-fill: #000000;");
                } else {
                    button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF;");
                }
            }
        }
    }

    private Node getNodeByCoordinate(Integer row, Integer column) {
        for (Node node : grid.getChildren()) {
            if (Objects.equals(GridPane.getRowIndex(node), row) && Objects.equals(GridPane.getColumnIndex(node), column)) {
                return node;
            }
        }
        return null;
    }

}
