package com.example.project;

import Backend.User.User;
import Backend.User.UserDaoImpl;
import animatefx.animation.RotateIn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.EnumFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersAdmin implements  Initializable{
    


        @FXML
        private MFXButton refresh_btn;
        @FXML
        private Text refresh_icon;


        @FXML
        MFXTableView<User> usersTable;



        private UserDaoImpl userDao = new UserDaoImpl();
         int  userId_modifier;

         static String current_user=null;




        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {


            refresh_btn.setOnMousePressed(event ->{
                RotateIn rotateIn=new RotateIn(refresh_icon);
                rotateIn.setSpeed(0.7);
                rotateIn.play();
            });


            setup_table_users();

            initializeUsers();

        }



        public void setup_table_users(){
            MFXTableColumn<User>  idColumn = new MFXTableColumn<>("Id", true);
            MFXTableColumn<User> usernameColumn = new MFXTableColumn<>("Username", true);
            MFXTableColumn<User> passwordColumn = new MFXTableColumn<>("Password", true);
            MFXTableColumn<User> typeColumn = new MFXTableColumn<>("Type", true);
            MFXTableColumn<User> statusClumn = new MFXTableColumn<>("Status", true);
            MFXTableColumn<User> currentUserColums = new MFXTableColumn<>("", true);

            MFXTableColumn<User> actionsColumn = new MFXTableColumn<>("Actions", true);
//            MFXTableColumn<User> modifierColumn = new MFXTableColumn<>("Modifier", true);

            idColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getId));
            usernameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUsername));
            passwordColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getPassword));
            typeColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getType));
            statusClumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getStatus));
            currentUserColums.setRowCellFactory(user -> new MFXTableRowCell<>(data -> data.getId(), data -> {
                if(AdminPage.getInstance().current_user.equals(user))
                    return "current user";
                return null;
            }));

            actionsColumn.setRowCellFactory(user -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
                {

                        setAlignment(Pos.TOP_LEFT);
                        setMinWidth(180);
                        MFXButton deleteButton = new MFXButton();
                        Button editButton = new Button();
                        deleteButton.getStyleClass().add("delete-button-tableView");
                        editButton.getStyleClass().add("edit-button-tableView");
                        editButton.setOnAction(event -> {
                            userId_modifier = user.getId();
                            edit(user);


                        });
                        deleteButton.setOnAction(event -> {
                            supprimerUser(user);
                        });
                        HBox hBox = new HBox(editButton, deleteButton);
                        hBox.setSpacing(25);
                        setGraphic(hBox);
                    }


            });


            idColumn.setMinWidth(100);

            usernameColumn.setMinWidth(210);

            passwordColumn.setMinWidth(210);

            typeColumn.setMinWidth(150);
            statusClumn.setMinWidth(150);
            currentUserColums.setMinWidth(150);

            usersTable.getTableColumns().addAll(idColumn, usernameColumn,passwordColumn,typeColumn,statusClumn,currentUserColums,actionsColumn);
            usersTable.getFilters().addAll(
                    new IntegerFilter<>("Id", User::getId),
                    new StringFilter<>("Username", User::getUsername),
                    new StringFilter<>("Prix", User::getPassword),
                    new EnumFilter<>("Type",User::getType, User.TypeUser.class),
                    new EnumFilter<>("Status",User::getStatus, User.Status.class)

            );
        }
        public void initializeUsers() {

            usersTable.autosizeColumnsOnInitialization();

            ObservableList<User> usersList = FXCollections.observableList(userDao.getAll());

            usersTable.setItems(usersList);

        }
//
//
//
//
//
        public void addUser(){
            AdminPage.getInstance().changePage("/com/example/project/FXML/AddUser.fxml");

        }


    public  void edit(User user){
        try {

            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/project/FXML/EditUser.fxml"));
            Parent root=loader.load();
            EditUser editUser=loader.getController();
            editUser.user=user;
            editUser.setUserValues(user);
            AdminPage.getInstance().changePage(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


        public void supprimerUser(User user)  {
            userDao.delete(user.getId());
            initializeUsers();
        }






    }

