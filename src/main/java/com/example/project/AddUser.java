package com.example.project;

import Backend.User.User;
import Backend.User.UserDaoImpl;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUser implements Initializable {




    @FXML
    Label addUser_label;
    @FXML
    Label  username_rule, password_rule, type_rule;

    private UserDaoImpl userDao = new UserDaoImpl();


    @FXML
    private MFXComboBox<String> type_field;
    @FXML
    TextField username_field, password_field;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            ObservableList<String> type_comboBox= FXCollections.observableArrayList("admin","cashier");
            type_field.setItems(type_comboBox);


    }





    private Boolean userRules(String username, String password, String type){


        int flag=0;



        if (username.isEmpty()) {
            username_rule.setText("Vous devez fournir le username!");
            System.out.println("designation error");
            flag+=1;
        }


        if (password.isEmpty()) {
            password_rule.setText("Vous devez fournir le password");
            flag+=1;
        }
        if (type.isEmpty()) {
            type_rule.setText("Vous devez fournir le type ");
            flag+=1;
        }
        return flag == 0;
    }


        public void addUser() {
            String username = username_field.getText();
            String password = password_field.getText();
            String type = type_field.getText();
            if(userRules( username , password,type)){

                User user = new User(username, password, type);
                userDao.add(user);
                addUser_label.setText(type+" added successfully");

            }
        }


    public void cancelUser() {
        AdminPage.getInstance().changePage("/com/example/project/FXML/usersAdmin.fxml");
    }


}
