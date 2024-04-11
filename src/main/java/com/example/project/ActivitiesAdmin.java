package com.example.project;

import Backend.Categorie.CategorieDaoImpl;
import Backend.Historique.Historique;
import Backend.Historique.HistoriqueDaoImpl;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ActivitiesAdmin implements Initializable {
    @FXML
    private FlowPane actions_pane;
    @FXML
    private TextField search_bar;
    @FXML
    private MFXComboBox<String> sort;

    HistoriqueDaoImpl historiqueDao=new HistoriqueDaoImpl();
    CategorieDaoImpl categorieDao=new CategorieDaoImpl();
    List<Historique> historyList=historiqueDao.getAll();;
    private Parent root;
    private Scene filterScene;
    private boolean filterActive=false;
    public static Stage filter_stage;


        public static ActivitiesAdmin instance;

        public static ActivitiesAdmin getInstance() {
        return instance;
    }

        public ActivitiesAdmin(){
            instance=this;
        }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refreshActionsTable();


        search_bar.textProperty().addListener((observable, oldValue, newValue) -> {

            createTablee(historyList.stream().filter(historique -> historique.getDesignation().contains(newValue)).collect(Collectors.toList()));

        });

        ObservableList<String> sortList = FXCollections.observableList(
                new ArrayList<>(Arrays.asList( "OldDate", "NewDate","Oldest", "Newest"))
        );
//
        sort.setItems(sortList);

    }

    public void refreshActionsTable(){

        createTablee(historyList);
    }

    public void sortActionsTable(){

        switch (sort.getSelectedItem()){
            case "Oldest":  historyList=historyList.stream().sorted(Comparator.comparing(Historique::getDate)).collect(Collectors.toList());break;
            case "Newest" : historyList=historyList.stream().sorted(Comparator.comparing(Historique::getDate).reversed()).collect(Collectors.toList());break;
            case "OldAction" : historyList=historyList.stream().sorted(Comparator.comparing(Historique::getId)).collect(Collectors.toList());break;
            case "NewAction" : historyList=historyList.stream().sorted(Comparator.comparing(Historique::getId).reversed()).collect(Collectors.toList());
        }

        createTablee(historyList);
    }


    public FlowPane create_row(int id1, int category1, String name1, int quantity1, double price1, Historique.HistoriqueAction type1, LocalDate date1) {

        Label id = new Label(Integer.toString(id1));
        id.setMinWidth(90);

        Label category = new Label(categorieDao.getNomCategorieFromId(category1));
        category.setMinWidth(190);

        Hyperlink product_name=new Hyperlink(name1);
        product_name.setMinWidth(190);
        product_name.getStyleClass().add("link-button");
        product_name.setOnAction(event -> System.out.println("sdds"));

        Label quantity = new Label(Integer.toString(quantity1));
        quantity.setMinWidth(104);


        Label price_unit = new Label(Double.toString(price1));
        price_unit.setMinWidth(104);

        String type = "-";
        String color="4CAF50";
        if (type1 == Historique.HistoriqueAction.STOCK_OUT) {
            type = "+";
            color="ef6c00";
        } else if (type1 == Historique.HistoriqueAction.EXPIRED) {
            color="9C27B0";

        }

        Label total = new Label(type+ Double.toString(price1 * quantity1));
        total.setMinWidth(104);

        Label action = new Label();
        action.setMinWidth(80);
        action.setPadding(new Insets(0 ,0,0,18));
        Circle icon=new Circle(8, Color.web(color));

        action.setGraphic(icon);
        String text = switch (color) {
            case "4CAF50" -> "incoming products";
            case "ef6c00" -> "outgoing products";
            case "9C27B0" -> "expired products";
            default -> null;
        };
        action.setTooltip(new Tooltip(text));

        Label date = new Label(date1.toString());
        date.setMinWidth(95);

        FlowPane flowPane=new FlowPane(id,category,product_name,quantity,price_unit,total,action,date);
        flowPane.setHgap(20);

        flowPane.setAlignment(Pos.CENTER);
        flowPane.getStyleClass().add("row-container");
        return flowPane;

    }



    public void createTablee(List<Historique> historyList){
        actions_pane.getChildren().clear();

        for(Historique history:historyList){
            actions_pane.getChildren().add(create_row(history.getId(),history.getIdCategorie(),history.getDesignation(),history.getQte(),history.getPrix(),history.getType(),history.getDate()));



        }

    }









    public  void prepareFilter(){
        System.out.println("bhbhj");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/FXML/FilterPane.fxml"));
            root = loader.load();
            FilterPane filterPane= loader.getController();
            filterPane.setHistoryList(historyList);
            filterScene = new Scene(root);
            filter_stage=new Stage();
            filter_stage.initStyle(StageStyle.TRANSPARENT);
            filterScene.setFill(Color.TRANSPARENT);
            filter_stage.setScene(filterScene);
            filterActive=true;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void filter(){
        if (!filterActive){
            prepareFilter();
        }
        if(filter_stage.isShowing()){
            filter_stage.toFront();
        }else {
            filter_stage.show();
        }
    }

    public void clearFilter(){
        filter_stage.close();
        createTablee(historyList);
    }




}
