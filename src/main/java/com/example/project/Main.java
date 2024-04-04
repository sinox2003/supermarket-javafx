package com.example.project;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {

    public static Stage primaryStage;
    MFXGenericDialog dialogContent;
    MFXStageDialog dialog;
    GridPane grid;

    public static void main(String[] args) {launch(args);}

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage=stage;
        Parent root= FXMLLoader.load(getClass().getResource("/com/example/project/FXML/login.fxml"));
        Scene scene=new Scene(root);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("Supermarket");
        stage.show();
        stage.setOnCloseRequest(event ->{
            event.consume();
            close(stage);

        });

    }
    public void DialogsController(Stage stage) {


        this.dialogContent = MFXGenericDialogBuilder.build()
                .makeScrollable(true)
                .get();
        this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setTitle("Dialogs Preview")
                .setOwnerNode(grid)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.addActions(
                Map.entry(new MFXButton("Confirm"), Event -> {
                    stage.close();

                    if(AdminPage.getInstance()!=null){
                        AdminPage.getInstance().UserGoOffline();
                    }

                    System.exit(1);
                }),
                Map.entry(new MFXButton("Cancel"), Event -> dialog.close())
        );
    }
    @FXML
    private void close(Stage stage) {
        DialogsController(stage);
        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.getStyleClass().add("mfx-warn-dialog");
        Label dialoglabel=new Label("By clicking the confirm button, the application will close.\nAre you sure you wish to proceed with exiting the application?");
        dialogContent.setCenter(dialoglabel);
        dialogContent.setHeaderText("This is a warning ");

        dialog.showDialog();
    }


}
