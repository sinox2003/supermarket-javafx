package com.example.project;

import Backend.Historique.HistoriqueDaoImpl;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;
import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProduct implements Initializable{



        @FXML
        private Label label,designation_rule, quantite_rule, prix_rule, date_rule,selling_price_rule;


        private ProduitDaoImpl produitsDao = new ProduitDaoImpl();

        private HistoriqueDaoImpl hdao = new HistoriqueDaoImpl();


        public  int categorieNumber;
        //panes


        @FXML
        TextField designation_field, prix_field,quantite_field ,selling_price_field;
        @FXML
        MFXDatePicker peremption_field,date_field;


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            date_field.setYearsRange(NumberRange.of(LocalDate.now().getYear()-1,LocalDate.now().getYear()));
            peremption_field.setYearsRange(NumberRange.of(LocalDate.now().getYear(),LocalDate.now().getYear()+10));




        }

        private Boolean rules(){


            int flag=0;

            if (designation_field.getText().isEmpty()) {
                designation_rule.setText("Vous devez fournir la designation du produit!");
                flag+=1;
            }
            else {
                designation_rule.setText("");

            }
            // Validate Quantite
            if (quantite_field.getText().isEmpty()) {
                quantite_rule.setText("Vous devez fournir la quantité du produit!");

                flag+=1;
            }else{

                quantite_rule.setText("");

                int qte;
                try {
                    qte = Integer.parseInt(quantite_field.getText());
                    if (qte <= 0) {
                        quantite_rule.setText("le nombre doit être positif!");
                        flag+=1;

                    }

                } catch (NumberFormatException e) {
                    // Handle error: Quantity should be a valid integer
                    quantite_rule.setText("Vous devez taper un entier!");
                    flag+=1;
                    System.out.println("entier error");
                }
            }

            // Validate Prix
            if (prix_field.getText().isEmpty()) {
                prix_rule.setText("Vous devez fournir le prix du produit!");
                flag+=1;
            }else{
                prix_rule.setText("");
                float prix;
                try {
                    prix = Float.parseFloat(prix_field.getText());
                    if (prix <= 0) {

                        prix_rule.setText("le nombre doit être positif!");
                        flag+=1;
                    }
                } catch (NumberFormatException e) {
                    prix_rule.setText("Vous devez taper un nombre!");
                    flag+=1;
                }
            }
            if (selling_price_field.getText().isEmpty()) {
                selling_price_rule.setText("Vous devez fournir le prix du produit!");
                flag+=1;
            }else{
                selling_price_rule.setText("");
                float prix;
                try {
                    prix = Float.parseFloat(selling_price_field.getText());
                    if (prix <= 0) {

                        selling_price_rule.setText("le nombre doit être positif!");
                        flag+=1;
                    }
                } catch (NumberFormatException e) {
                    selling_price_rule.setText("Vous devez taper un nombre!");
                    flag+=1;
                }
            }

            // Validate Date and Peremption
            if (date_field.getValue() == null ){
                date_rule.setText("Vous devez fournir la date d'entrée!");
                flag+=1;
            }
            else {
                date_rule.setText("");
            }

            return flag == 0;

        }


        public void ValiderProduit() throws InterruptedException {

            if( rules()){


                String designation = designation_field.getText();
                int qte = Integer.parseInt(quantite_field.getText());
                Float prix = Float.parseFloat(prix_field.getText());
                Float selling_price = Float.parseFloat(selling_price_field.getText());
                LocalDate date = date_field.getValue();
                LocalDate peremption = peremption_field.getValue();
                //TODO :: add the conditions for peremption = null
                Produit p = new Produit(categorieNumber, designation, qte, prix,selling_price, date, peremption);

                produitsDao.add(p);



                    designation_field.setText("");
                    quantite_field.setText("");
                    prix_field.setText("");
                    date_field.setText("");
                    peremption_field.setText("");
                    selling_price_field.setText("");

                    label.setText("Product added successfully");


                    AdminPage.getInstance().refresh_notifications(AdminPage.getInstance().notificationsController);

            }



        }
        public void cancelProduct(){
            CategoriesAdmin.getInstance().openProductsPane(categorieNumber);
        }



}


