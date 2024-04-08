package com.example.project;


import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
import Backend.Historique.HistoriqueDaoImpl;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CategoriesAdmin implements Initializable {

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    String[] colors={
            "linear-gradient(from 0.0% 100.0% to 100.0% 0.0%, rgba(44,219,160,1) 12%, rgba(51,165,194,1) 53%, rgba(77,133,226,1) 77%);",
            "linear-gradient(from 0.0% 100.0% to 100.0% 0.0%, #eaa31e 0%, #f3527d 66%, #f834a8 100%);",
            "linear-gradient(from 0.0% 100.0% to 100.0% 0.0%, rgba(195,107,214,1) 12%, rgba(130,75,196,1) 52%, rgba(92,52,185,1) 82%);",
            "linear-gradient(from 0.0% 100.0% to 100.0% 0.0%,rgba(50,43,35,1) 0%, rgba(142,14,0,1) 100%);"};

    @FXML
    private GridPane grid;


    @FXML
    private Button addCategory_btn ;


    @FXML
    private FlowPane DBCategories_container;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    private MFXComboBox<String> sort;



    String buttonText;

    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();
    private HistoriqueDaoImpl historiqueDao = new HistoriqueDaoImpl();

    public int categorieNumber;

    //panes

    @FXML
    MFXScrollPane  scroll_pane_categories;
    List<Categorie> categorieList=categorieDao.getAll();

    public  static CategoriesAdmin instance;


    public CategoriesAdmin(){
        instance=this;
    }
    public static CategoriesAdmin getInstance(){
        return instance;
    }

    //home METHODS
    public void showProductsByCategory(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
         buttonText = clickedButton.getText();


            categorieNumber=Integer.parseInt(clickedButton.getId().split("C")[1]);

        openProductsPane(categorieNumber);
    }

    public void openProductsPane(int categorieNumber) {




            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/project/FXML/ProductsByCategory.fxml"));
        Parent root= null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ProductsByCategory productsByCategory=loader.getController();
            productsByCategory.categorieNumber=categorieNumber;
            productsByCategory.setup_table_produit();
            productsByCategory.initializeProduits();
            AdminPage.getInstance().changePage(root);

    }


    /* //////////////////////////////       warning msg lors de la tenntation de supprimer la categorie \\\\\\\\\\\\\\\\\\\\\\   */
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



        dialogContent.setMaxSize(400, 200);

    }
    @FXML
    private void openWarning(String categorieNom,int categorieNumber) {

        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.getStyleClass().add("mfx-warn-dialog");
        dialogContent.addActions(
                Map.entry(new MFXButton("Confirm"), Event -> {
                    historiqueDao.deleteCategory(categorieNumber);
                    categorieDao.delete(categorieNumber);
                    refreshCategory();
                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), Event -> dialog.close())
        );
        Label label=new Label("En supprimant la catégorie "+categorieNom+", tous les produits\nassociés à celle-ci seront également supprimés.\nÊtes-vous sûr(e) de vouloir procéder ?");

        dialogContent.setCenter(label);
        dialogContent.setHeaderText("This is a warning info dialog");

        dialog.showDialog();
    }
    /* ////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\   */


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> sortList = FXCollections.observableList(
                new ArrayList<>(Arrays.asList( "Oldest", "Newest","A-Z", "Z-A"))
        );
//
        sort.setItems(sortList);
//


        refresh();


    }

    public List<Categorie> sort(){


        switch (sort.getSelectedItem()){
            case "Oldest":  return categorieList;
            case "Newest" : return categorieList.stream().sorted(Comparator.comparing(Categorie::getId).reversed()).collect(Collectors.toList());
            case "A-Z" : return categorieList.stream().sorted(Comparator.comparing(Categorie::getNom)).collect(Collectors.toList());
            case "Z-A" : return categorieList.stream().sorted(Comparator.comparing(Categorie::getNom).reversed()).collect(Collectors.toList());
        }
        return null;
    }

    public void addCategory() throws IOException {
        AdminPage.getInstance().changePage("/com/example/project/FXML/addCategory.fxml");

    }
    public  void returnToCategories(){
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(scroll_pane_categories);
        refreshCategory();
    }
    public void initializeCategories( List<Categorie> categories ) {

        int i=0,color_nbr = 0;
        for (Categorie category : categories) {


            MFXButton categoryButton = new MFXButton(category.getNom());
            categoryButton.setFocusTraversable(false);
            categoryButton.setId("C" + category.getId());
            categoryButton.getStyleClass().add("showCategoryProducts_btn");
            MFXButton edit = new MFXButton("EDIT");
            edit.getStyleClass().add("edit-button");
            MFXButton delete = new MFXButton("DELETE");
            delete.getStyleClass().add("deleteCategory_btn");

            Label label = new Label(category.getNom().toUpperCase());
            label.getStyleClass().add("label");
            Pane pane = new Pane();
            pane.getStyleClass().add("pane");
            pane.setClip(new Rectangle(246.4, 334.4));

            TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), edit);
            TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), delete);
            TranslateTransition tt3 = new TranslateTransition(Duration.millis(200), label);
            edit.setTranslateX(250);
            delete.setTranslateX(-224);
            label.setTranslateY(110);
            pane.setOnMouseEntered(mouseEvent -> {
                tt3.setToY(149);
                tt1.setToX(26);
                tt2.setToX(0);
                tt3.play();
                tt2.play();
                tt1.play();

            });
            pane.setOnMouseExited(mouseEvent -> {
                tt1.setToX(250);
                tt2.setToX(-224);
                tt3.setToY(110);
                tt1.play();
                tt2.play();
                tt3.play();
            });
            categoryButton.setOnAction(event -> {

                showProductsByCategory(event);


            });

            edit.setOnAction(event -> {

                editCategory(category);

            });


            delete.setOnAction(event -> {
                DialogsController(event);
                openWarning(category.getNom(), category.getId());

            });

            pane.getChildren().addAll(edit, delete, label, categoryButton);
            color_nbr = i % 4;
            pane.setStyle(" -fx-background-color:" + colors[color_nbr]);
            DBCategories_container.getChildren().addLast(pane);
            i++;
        }
        addCategory_btn.setStyle(" -fx-background-color:" + colors[(color_nbr+1)%4]);
    }

    private void editCategory(Categorie category)  {

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/project/FXML/EditCategory.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditCategory editCategory=loader.getController();
        editCategory.categorie=category;
        editCategory.setValues(category);
        AdminPage.getInstance().changePage(root);

    }

    public void refreshCategory(){

        DBCategories_container.getChildren().clear();

        initializeCategories(sort());
        DBCategories_container.getChildren().addLast(addCategory_btn);

    }
    public void refresh(){

        DBCategories_container.getChildren().clear();

        initializeCategories(categorieList);
        DBCategories_container.getChildren().addLast(addCategory_btn);

    }






}
