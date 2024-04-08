package com.example.project;

import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AddCategory  {

    @FXML
    Label  categoryName_rule;


    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();


    @FXML
    TextField categoryName_field;




    public Boolean categoryRules(){
        String nom = categoryName_field.getText();
        int flag = 0;

        if (nom.isEmpty()) {
            categoryName_rule.setText("Field is empty!");
            categoryName_rule.setTextFill(Color.RED);
            flag += 1;
        }
        return flag == 0;
    }
    public void validerCategory(ActionEvent event) throws IOException, InterruptedException {
        if(categoryRules()){
            int id = (int)categorieDao.getAll().stream().count()+1;

            Categorie categorie = new Categorie(id,categoryName_field.getText().toUpperCase());
            categorieDao.add(categorie);

            categoryName_rule.setText("Category was added succesfuly ");
            categoryName_rule.setTextFill(Color.GREEN);
        }
    }

    public  void goBack(ActionEvent event) {
        AdminPage.getInstance().changePage("/com/example/project/FXML/CategoriesAdmin.fxml");

    }


}
