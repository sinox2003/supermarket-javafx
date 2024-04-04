package com.example.project;

import Backend.User.User;
import Backend.User.UserDaoImpl;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;

    private GridPane grid;


    public double x,y;
    @FXML
    private AnchorPane topBar;
    @FXML
    private Parent root;
    @FXML
    private Label label;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private UserDaoImpl userDao = new UserDaoImpl();


        // ... (other declarations)

        public void login(ActionEvent event) throws SQLException, IOException {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(password.isEmpty() || username.isEmpty()){
                label.setText("Username or password empty");
            }
            else{
                User user = userDao.getUsersByInfo(username, password);
                if (user!=null && user.getType()== User.TypeUser.admin) {
                    // Successful login
                    user.setStatus(User.Status.online);
                    userDao.updateStatus(user);
                    label.setText("");
                    changeScene(event, "/com/example/project/FXML/adminPage.fxml");
                    AdminPage.getInstance().current_user=user;

                } else {
                    // Incorrect username or password
                    label.setText("Username or password incorrect");
                }
            }
        }

        public void changeScene(ActionEvent event, String file) throws IOException {
            root = FXMLLoader.load(getClass().getResource(file));
            scene = new Scene(root);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            scene.setFill(Color.TRANSPARENT);


            stage.setScene(scene);
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        }
    public void DialogsController(ActionEvent event) {
        Stage stage=(Stage)((Node)event.getTarget()).getScene().getWindow();

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
                }),
                Map.entry(new MFXButton("Cancel"), Event -> dialog.close())
        );


    }
    @FXML
    private void close(ActionEvent event) {
        DialogsController(event);
        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.getStyleClass().add("mfx-warn-dialog");

        Label dialoglabel=new Label("By clicking the confirm button, the application will close.\nAre you sure you wish to proceed with exiting the application?");

        dialogContent.setCenter(dialoglabel);
        dialogContent.setHeaderText("This is a warning ");

        dialog.showDialog();
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topBar.setOnMousePressed(Event->{
            x=Event.getSceneX();
            y=Event.getSceneY();

        });
        topBar.setOnMouseDragged(event ->{
            Main.primaryStage.setX(event.getScreenX()-x);
            Main.primaryStage.setY(event.getScreenY()-y );

        });

    }
    public void minimize_window(ActionEvent event){
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }
}



