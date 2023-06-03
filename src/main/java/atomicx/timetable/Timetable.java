package atomicx.timetable;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Timetable extends Application {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final float BUTTON_WIDTH = 120F;
    private static final float BUTTON_HEIGHT = 50F;
    private static final int PADDING = 10;
    private static final int SPACING = 8;
    private static final int LABEL_FONT_SIZE = 20;
    private static final String LABEL_COLOR = "#FFFFFF";
    private final String[][] slots = {
            {"A1/SA1", "G1", "E1", "TD1", "A2/SA2", "TB2/SB2", "E2", "C2/SC2", "-", "-"},
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
        populateGrid();
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #424242;");
        root.getChildren().add(grid);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void populateGrid() {
        grid.getChildren().clear();
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
                    setSlotButtonStyleAndAction(button1, BUTTON_WIDTH / 2);
                    setSlotButtonStyleAndAction(button2, BUTTON_WIDTH / 2);

                    HBox hbox = new HBox(button1, button2);
                    grid.add(hbox, j + 1, i + 1);
                } else {
                    MFXButton button = new MFXButton(slots[i][j]);
                    setSlotButtonStyleAndAction(button);
                    grid.add(button, j + 1, i + 1);
                }
            }
        }

        MFXButton updateButton = new MFXButton("Update");
        setUtilityButtonStyle(updateButton);
        updateButton.setOnAction(e -> showUpdateScreen());
        grid.add(updateButton, COLS + 1, 0);
    }

    public void showUpdateScreen() {
        Stage updateStage = new Stage();
        updateStage.setTitle("Update Timetable");

        GridPane updateGrid = new GridPane();
        updateGrid.setAlignment(Pos.CENTER);
        updateGrid.setPadding(new Insets(PADDING));
        updateGrid.setHgap(SPACING);
        updateGrid.setVgap(SPACING);

        TextArea textArea = new TextArea();
        textArea.setPrefSize(400, 300);

        CheckBox checkbox = new CheckBox("✔ VTOP format | ⬜ Annexure format");
        checkbox.setSelected(true);

        MFXButton submitButton = new MFXButton("Submit");
        setUtilityButtonStyle(submitButton);

        submitButton.setOnAction(e -> {
            if (checkbox.isSelected()) parseVtopData(textArea.getText());
            else parseAnnexureData(textArea.getText());
            populateGrid();
            updateStage.close();
        });

        HBox submitAndCheck = new HBox(submitButton, checkbox);
        submitAndCheck.setSpacing(10);
        submitAndCheck.setAlignment(Pos.BOTTOM_LEFT);

        updateGrid.add(textArea, 0, 0);
        updateGrid.add(submitAndCheck, 0, 1);

        Scene updateScene = new Scene(updateGrid);

        updateStage.setScene(updateScene);

        updateStage.show();
    }

    private void parseAnnexureData(String tt) {
        Scanner text = new Scanner(tt);

        int row = 0;
        while (text.hasNextLine()) {
            ArrayList<String> currentLine = new ArrayList<>(Arrays.asList(text.nextLine().split(" ")));
            currentLine.removeIf(elem -> (!elem.matches("([A-z]+\\d+)(/[A-z]+\\d+)?")));
            if (currentLine.isEmpty()) continue;
            while (currentLine.size() < 10) currentLine.add("-");
            slots[row] = currentLine.toArray(new String[0]);
            row++;
        }
    }

    private void parseVtopData(String tt) {
        Scanner text = new Scanner(tt);

        //skipping time data
        text.nextLine();
        text.nextLine();
        text.nextLine();
        text.nextLine();

        int row = 0;
        while (text.hasNext()) {
            ArrayList<String> currentLine = new ArrayList<>(Arrays.asList(text.nextLine().split("\t")));
            ArrayList<String> newList = new ArrayList<>();
            for (String curElem : currentLine) {
                if (curElem.contains("-") && curElem.length() > 3) {
                    newList.add(curElem.split("-")[0]);
                } else if (curElem.matches("[A-z]+\\d+") || curElem.equals("-")) {
                    newList.add(curElem);
                }
            }
            currentLine = new ArrayList<>(newList);

            newList.clear();
            for (int i = 0; i < currentLine.size(); i += 2) {
                if (currentLine.get(i).contains("L")) {
                    newList.add(currentLine.get(i));
                } else if (currentLine.get(i + 1).equals("-")) {
                    newList.add(currentLine.get(i));
                } else {
                    newList.add(currentLine.get(i) + "/" + currentLine.get(i + 1));
                }
            }

            slots[row] = newList.toArray(new String[0]);
            row++;
        }
    }

    private void setSlotButtonStyleAndAction(MFXButton button, float buttonWidth) {
        button.setPrefSize(buttonWidth, BUTTON_HEIGHT);
        button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: #FFFFFF;");

        button.setOnAction(e -> {
            MFXButton b = (MFXButton) e.getSource();
            String slot = b.getText();
            if (!slot.equals("-")) {
                toggleSlot(slot);
            }
        });
    }

    private void setSlotButtonStyleAndAction(MFXButton button) {
        this.setSlotButtonStyleAndAction(button, BUTTON_WIDTH);
    }

    private void setUtilityButtonStyle(MFXButton button) {
        button.setPrefSize(BUTTON_WIDTH / 2, BUTTON_HEIGHT / 2);
        button.setStyle("-fx-background-color: #bc91f1; -fx-text-fill: #000000;");
    }

    private void toggleSlot(String slot) {
        slot = slot.replace("T", "");
        for (int i = 1; i < ROWS + 1; i++) {
            for (int j = 1; j < COLS + 1; j++) {
                Node node = getNodeByCoordinate(i, j);

                if (node instanceof HBox hbox) {
                    for (Node child : hbox.getChildren()) {
                        toggleButton(child, slot);
                    }
                } else {
                    if (slot.matches("L\\d+")) {
                        int slotNumber = Integer.parseInt(slot.substring(1));
                        if (slotNumber % 2 != 0) {
                            toggleButton(getNodeByCoordinate(i, j + 1), "L" + (slotNumber + 1));
                        } else {
                            toggleButton(getNodeByCoordinate(i, j - 1), "L" + (slotNumber - 1));
                        }
                    }
                    toggleButton(node, slot);
                }
            }
        }
    }

    private void toggleButton(Node node, String slot) {
        if (node instanceof MFXButton button) {
            String slotToCheck = button.getText().replace("T", "");
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
