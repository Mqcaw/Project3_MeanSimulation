package me.jackson.project_3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class Main extends Application {

    TableView<Game> table =new TableView<>();
    TableColumn<Game, String> namesColumn = new TableColumn<>("Names");
    TableColumn<Game, Integer> iterationsColumn = new TableColumn<>("Iterations");
    TableColumn<Game, Integer> maxValuesColumn = new TableColumn<>("Max Value");
    TableColumn<Game, Double> meansColumn = new TableColumn<>("Mean");
    TableColumn<Game, Double> accuracyColumn = new TableColumn<>("Accuracy");

    ObservableList<Game> tableList = FXCollections.observableArrayList();

    @Override
    public void init() throws Exception {
        try (Connection connect = DriverManager.getConnection("jdbc:derby:database; create = true");
             Statement state = connect.createStatement()) {

            DatabaseMetaData dbm = connect.getMetaData();
            ResultSet result = dbm.getTables(null, null, "DATABASE_TABLE", null);
            if (result.next()) {
            } else {
                state.execute("create table database_table(name varchar(100), iterations int, max_value int, mean double, accuracy double)");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        table.getItems().clear();

        table.getColumns().add(namesColumn);
        table.getColumns().add(iterationsColumn);
        table.getColumns().add(maxValuesColumn);
        table.getColumns().add(meansColumn);
        table.getColumns().add(accuracyColumn);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        updateTable();
    }

    @Override
    public void start(Stage stage) {

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField iterationsField = new TextField();
        iterationsField.setPromptText("Iterations");
        TextField maxValueField = new TextField();
        maxValueField.setPromptText("Max Value");

        VBox textFields = new VBox(nameField, iterationsField, maxValueField);
        textFields.setPadding(new Insets(5, 25, 0, 25));
        textFields.setSpacing(10);
        textFields.setAlignment(Pos.CENTER);


        Button newEntryButton = new Button("New Entry");
        newEntryButton.setMinWidth(100);
        Button resetButton = new Button("Reset");
        resetButton.setMinWidth(100);

        HBox buttons = new HBox(newEntryButton, resetButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        Button highestAccuracy = new Button("Highest Accuracy");



        VBox root = new VBox(table, textFields, buttons, highestAccuracy);
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(10);

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Project 3");
        stage.setScene(scene);
        stage.show();


        newEntryButton.setOnMouseClicked(mouseEvent -> {
            try (Connection connect = DriverManager.getConnection("jdbc:derby:database");
                 PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO database_table VALUES(?, ?, ?, ?, ?)")) {


                boolean error = false;
                if (nameField.getText().isEmpty()) {
                    errorEmptyField("Name Field");
                    error = true;
                }
                if (iterationsField.getText().isEmpty()) {
                    errorEmptyField("Iterations Field");
                    error = true;
                }
                if (maxValueField.getText().isEmpty()) {
                    errorEmptyField("Max Value Field");
                    maxValueField.setStyle("-fx-ext-fill: red;");
                    error = true;
                }
                if (error) {
                    System.out.println("Error");
                    return;
                }
                try {
                    int temp = Integer.parseInt(iterationsField.getText());
                } catch (NumberFormatException nfe) {
                    errorIntFormat("Iterations Field");
                    error= true;
                }
                try {
                    int temp = Integer.parseInt(maxValueField.getText());
                } catch (NumberFormatException nfe) {
                    errorIntFormat("Max Value Field");
                    error= true;
                }

                if (error) {
                    System.out.println("Error");
                    return;
                }

                String tempName = nameField.getText();
                int tempIterations = Integer.parseInt(iterationsField.getText());
                int tempMaxValue = Integer.parseInt(maxValueField.getText());
                Game game = new Game(tempName, tempIterations, tempMaxValue);
                double tempMean = game.getMean();
                double tempAccuracy = game.getAccuracy();


                preparedStatement.setString(1, tempName);
                preparedStatement.setInt(2, tempIterations);
                preparedStatement.setInt(3, tempMaxValue);
                preparedStatement.setDouble(4, tempMean);
                preparedStatement.setDouble(5, tempAccuracy);
                preparedStatement.executeUpdate();
                System.out.println("Execute update sent");

                updateTable();
            } catch (SQLException exception) {

            }

            nameField.setText("");
            iterationsField.setText("");
            maxValueField.setText("");
        });
        resetButton.setOnMouseClicked(mouseEvent -> {
            try (Connection connect = DriverManager.getConnection("jdbc:derby:database")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Reset Table");
                alert.setHeaderText("Are you sure you want to rest all data in the table?");
                alert.setContentText("Click OK to reset.");


                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {
                    return;
                } else if (option.get() == ButtonType.CANCEL) {
                    return;
                }
                if (option.get() == ButtonType.OK) {
                    PreparedStatement preparedStatement = connect.prepareStatement("TRUNCATE TABLE database_table");
                    preparedStatement.executeUpdate();
                    updateTable();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        });
        highestAccuracy.setOnMouseClicked(mouseEvent -> {
            try (Connection connect = DriverManager.getConnection("jdbc:derby:database");
                 Statement statement = connect.createStatement();
                 ResultSet result = statement.executeQuery("SELECT * FROM database_table ORDER BY accuracy DESC")) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Highest Accuracy");
                alert.setHeaderText("The top 10 game's with the highest accuracy are:");
                String temp = "";
                int index = 0;
                while (result.next()) {
                    if (index < 10) {
                        temp = temp + (index + 1) + ". " + result.getString("name") + ": " + result.getDouble("accuracy") + "%\n";
                        index++;
                    }
                }
                alert.setContentText(temp);
                alert.show();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void stop() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {

        }
    }

    public static void main(String[] args) {
        launch();
    }

    public void updateTable() {
        table.getItems().clear();
        tableList.clear();
        try (Connection connect = DriverManager.getConnection("jdbc:derby:database");
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM database_table");) {

            while (result.next()) {
                String name = result.getString("name");
                int iterations = result.getInt("iterations");
                int max_value = result.getInt("max_value");
                double mean = result.getDouble("mean");
                double accuracy = result.getDouble("accuracy");

                Game tempGameObject = new Game(name, iterations, max_value, mean, accuracy);

                tableList.add(tempGameObject);

                namesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                iterationsColumn.setCellValueFactory(new PropertyValueFactory<>("iterations"));
                maxValuesColumn.setCellValueFactory(new PropertyValueFactory<>("maxValue"));
                meansColumn.setCellValueFactory(new PropertyValueFactory<>("mean"));
                accuracyColumn.setCellValueFactory(new PropertyValueFactory<>("accuracy"));

                table.setItems(tableList);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    public void errorEmptyField(String field) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty Field");
        alert.setContentText("The text field \"" + field + "\" is empty.");
        alert.show();
    }
    public void errorIntFormat(String field) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Input Type");
        alert.setContentText("The text field \"" + field + "\" must only contain <0-9>.");
        alert.show();
    }
}