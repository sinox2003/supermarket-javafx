package com.example.project;

import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategory  {
    @FXML
    Label categoryName_rule;

    @FXML
    Label label;
    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();


    @FXML
    TextField categoryName_field;

    public Categorie categorie;


    public Boolean categoryRules(String nom){

        int flag = 0;

        if (nom.isEmpty()) {
            categoryName_rule.setText("Field is empty!");
            categoryName_rule.setTextFill(Color.RED);
            flag += 1;
        }
        return flag == 0;
    }

    public void setValues(Categorie categorie){
        categoryName_field.setText(categorie.getNom());

    }

    public void validerCategory()  {
        String nom = categoryName_field.getText().toUpperCase();
        int id=categorie.getId();
        if(categoryRules(nom)){

            Categorie categorie = new Categorie(id,nom);
            categorieDao.update(categorie);

            label.setText("Category was edited successfully ");

        }
    }

    public  void goBack(ActionEvent event) {
        AdminPage.getInstance().changePage("/com/example/project/FXML/CategoriesAdmin.fxml");

    }

}
