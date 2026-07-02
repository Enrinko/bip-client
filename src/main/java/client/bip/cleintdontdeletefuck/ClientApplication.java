package client.bip.cleintdontdeletefuck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    public static void showPersonEditDialog(String type, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource(type));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            if ("main.fxml".equals(type)) dialogStage.initModality(Modality.NONE);
            else dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));
            if ("main.fxml".equals(type)) dialogStage.show();
            else dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void infoMessage(String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        info.initModality(Modality.WINDOW_MODAL);
        info.show();
    }

    public static boolean applyMessage(String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.YES, ButtonType.NO);
        info.initModality(Modality.WINDOW_MODAL);
        info.showAndWait();
        return info.getResult().equals(ButtonType.YES);
    }

    public static void warningMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }

    public static void errorMessage(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        error.initModality(Modality.WINDOW_MODAL);
        error.show();
    }

    public static String dialogMessage(String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        return dialog.getEditor().getText();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}