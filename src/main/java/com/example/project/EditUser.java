package com.example.project;


import Backend.User.User;
import Backend.User.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUser implements Initializable{



    public User user;

    @FXML
    Label  modifierUser_label;
    @FXML
    Label modifierUsername_rule, modifierPassword_rule;

    private UserDaoImpl userDao = new UserDaoImpl();



    @FXML
    TextField modifierUsername_field, modifierPassword_field, modifierType_field;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





    }







        private Boolean modifierUserRules(String username, String password,String type) {


            int flag = 0;

            if (username.isEmpty()) {
                modifierUsername_rule.setText("Vous devez fournir le username!");
                System.out.println("username error");
                flag += 1;
            }


            if (password.isEmpty()) {
                modifierPassword_rule.setText("Vous devez fournir le password");
                flag += 1;
            }

            return flag == 0;
        }



    public void setUserValues(User user){
        modifierUsername_field.setText(user.getUsername());
        modifierPassword_field.setText(user.getPassword());
        modifierType_field.setText(String.valueOf(user.getType()));

    }
    public void editUser()  {
        String username = modifierUsername_field.getText();
        String password = modifierPassword_field.getText();
        String type = modifierType_field.getText();

            if(modifierUserRules(username,password,type)){

                User editeUser = new User(user.getId(), username, password, type);
                userDao.update(editeUser);
                modifierUser_label.setText("User was modified successfully");

            }
    }

    public void cancelUser() {
        AdminPage.getInstance().changePage("/com/example/project/FXML/usersAdmin.fxml");
    }
//

}
