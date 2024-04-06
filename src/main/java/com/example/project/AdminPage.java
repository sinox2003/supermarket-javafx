package com.example.project;

import Backend.Produit.ProduitDaoImpl;
import Backend.User.User;
import Backend.User.UserDaoImpl;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class AdminPage implements Initializable {

     MFXGenericDialog dialogContent;
     MFXStageDialog dialog;
     GridPane grid;
    double x,y;
    @FXML
    Pane notification_pane;
    @FXML
    VBox sideBar_vbox;
    @FXML
    AnchorPane anchorpane,sideBar;
    @FXML
    ToggleButton sidebar_btn;
    @FXML
    MFXButton signout_Btn;
    @FXML
    ToggleButton dashboard,activities,users,products,categories;
    @FXML
    Circle notification_nbr;
    @FXML
    ToggleGroup sidebar_buttons;
    @FXML
    StackPane main_pane;
    ToggleButton[] sidebar_btns=new ToggleButton[4];
    Parent root;
    static Stage   notification_stage;
    NotificationsController notificationsController;
    Scene scene;
    ProduitDaoImpl produitsDao = new ProduitDaoImpl();
    User current_user;

    private UserDaoImpl userDao = new UserDaoImpl();
    private  static AdminPage instance;



    public AdminPage(){
        instance=this;
    }
    public  static AdminPage getInstance(){
        return instance;
    }


/*//////////////////////////////////////////////// INITIALIZE \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        anchorpane.setOnMousePressed(Event->{
            x=Event.getSceneX();
            y=Event.getSceneY();

        });
        anchorpane.setOnMouseDragged(event ->{
            Main.primaryStage.setX(event.getScreenX()-x);
            Main.primaryStage.setY(event.getScreenY()-y );

        });
        sidebar_btns= new ToggleButton[]{dashboard,activities, users, products,categories};
        dashboard.fire();
        change_scene();


        main_pane.setOnMousePressed(mouseEvent ->{
            if(!sidebar_btn.isSelected()){
                sidebar_btn.fire();}

        });


        sidebar_buttons.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });




    }
/*////////////////////////////////////  minimize window \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
    public void minimize_window(ActionEvent event){
        ((Stage)((Button)event.getSource()).getScene().getWindow()).setIconified(true);
    }

/*////////////////////////////////////  close msg \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
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
                    UserGoOffline();
                    System.exit(1);

                }),
                Map.entry(new MFXButton("Cancel"), Event -> dialog.close())
        );

        dialogContent.setMaxSize(400, 200);

    }
    @FXML
    private void close(ActionEvent event) {
        DialogsController(event);
        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.getStyleClass().add("mfx-warn-dialog");

        Label dialoglabel=new Label("By clicking the confirm button, the application will close.\nAre you sure you wish to proceed with exiting the application?");

        dialogContent.setCenter(dialoglabel);
        dialogContent.setHeaderText("This is a warning info dialog");

        dialog.showDialog();
    }

/*////////////////////////////////////////////// changing user status   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/

    public void UserGoOffline(){
        current_user.setStatus(User.Status.offline);
        userDao.updateStatus(current_user);
    }

/*////////////////////////////////////////////// sign out function   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/

    public void SignOut(ActionEvent event) throws IOException {

        UserGoOffline();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/project/FXML/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

    }

 /*//////////////////////////////////// sidebar button  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
   public void sidebar_btn(ActionEvent event) throws InterruptedException {
       TranslateTransition tt = new TranslateTransition(Duration.millis(20), sideBar);

        if (sidebar_btn.isSelected()){

            tt.setByX(-168);
            tt.play();
//
            Platform.runLater(()->{
            for(ToggleButton toggleButton:sidebar_btns){
                toggleButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                toggleButton.setPrefWidth(55);
                toggleButton.setAlignment(Pos.CENTER);
                toggleButton.setPadding(new Insets(0));

            }
            notification_pane.setTranslateX(160);
            notification_pane.setTranslateY(60);
            signout_Btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            signout_Btn.setAlignment(Pos.CENTER_RIGHT);
            sideBar_vbox.setAlignment(Pos.CENTER_RIGHT);
            sideBar_vbox.setPadding(new Insets(0,2,0,0));
            });
        }else {
            tt.setDuration(Duration.millis(150));
            tt.setByX(168);
            tt.play();
            Platform.runLater(()->{
                sideBar_vbox.setAlignment(Pos.CENTER);
            for(ToggleButton toggleButton:sidebar_btns){
                toggleButton.setContentDisplay(ContentDisplay.LEFT);
                toggleButton.setPrefWidth(210);
                toggleButton.setAlignment(Pos.BASELINE_LEFT);
                toggleButton.setPadding(new Insets(0,0,0,35));

            }
            notification_pane.setTranslateX(0);
            notification_pane.setTranslateY(0);
            signout_Btn.setContentDisplay(ContentDisplay.LEFT);
            signout_Btn.setAlignment(Pos.CENTER_LEFT);

            });

        }
   }

    /*//////////////////////////////////// Notification  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*/
    public void show_notifications() {

        if(notification_stage.isShowing()){
            notification_stage.toFront();

        }else {
            notification_stage.show();
        }



    }
    public  void refresh_notifications(NotificationsController notificationsController ){
        int count=(int)produitsDao.getAll().stream().filter(produit ->produit.getExpiration_date()!=null && produit.getExpiration_date().isBefore(LocalDate.now())).count();
        if(count>0){
            notification_nbr.setVisible(true);
        }else {
            notification_nbr.setVisible(false);
        }

        notificationsController.Initialize_notifications();
    }
    public void change_scene(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/FXML/notifications.fxml"));
            root = loader.load();
            notificationsController = loader.getController();
            refresh_notifications( notificationsController );
            scene = new Scene(root);
            notification_stage=new Stage();
            notification_stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            notification_stage.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

   public void initializeLoader(ActionEvent event) {
           ToggleButton button=(ToggleButton)event.getTarget();
           String file_name=button.getId();
           changePage("/com/example/project/FXML/"+file_name+"Admin.fxml");
   }
//    public void initializeUsersAdmin(ActionEvent event) {
//
//        changePage("/com/example/project/FXML/usersAdmin.fxml");
//
//    }
   public void changePage(String path){
        try {
            main_pane.getChildren().clear();
             root=FXMLLoader.load(getClass().getResource(path));
            main_pane.getChildren().setAll(root);
        } catch (Exception e) {
                throw new RuntimeException(e);
        }
   }
    public void changePage(Parent root){
        try {
            main_pane.getChildren().clear();
            main_pane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}
