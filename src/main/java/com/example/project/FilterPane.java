package com.example.project;

import Backend.Categorie.CategorieDaoImpl;
import Backend.Historique.Historique;
import io.github.palexdev.materialfx.controls.MFXFilterPane;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.EnumFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterPane implements Initializable {


    @FXML
    private MFXFilterPane<Historique> filterPane = new MFXFilterPane<>();
    @FXML
    private Pane pane;
    private double x;
    private double y;

    CategorieDaoImpl categorieDao=new CategorieDaoImpl();
    List<Historique> historyList=new ArrayList<>();

    public void setHistoryList(List<Historique> historyList) {
        this.historyList = historyList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterPane.getFilters().addAll(

                new IntegerFilter<>("Id", Historique::getId),
                new StringFilter<>("Category", historique -> {return categorieDao.getNomCategorieFromId(historique.getIdCategorie());} ),
                new StringFilter<>("Product Name", Historique::getDesignation),

                new IntegerFilter<>("Quantity", Historique::getQte),
                new DoubleFilter<>("Price", Historique::getPrix),
                new DoubleFilter<>("Total", historique -> {
                    return historique.getPrix()*historique.getQte();
                }),
                new EnumFilter<>("Action", Historique::getType, Historique.HistoriqueAction.class),
                new StringFilter<>("Date",historique -> {return historique.getDate().toString();} )


        );



        pane.setOnMousePressed(Event->{
            x=Event.getSceneX();
            y=Event.getSceneY();
        });

        pane.setOnMouseDragged(event ->{
            ActivitiesAdmin.filter_stage.setX(event.getScreenX()-x);
            ActivitiesAdmin.filter_stage.setY(event.getScreenY()-y );
        });




    }


    public void setOnFilter(MouseEvent mouseEvent) {
        Predicate<Historique> filter = filterPane.filter();
        List<Historique> filteredData = historyList.stream().filter(filter).collect(Collectors.toList());
        ActivitiesAdmin.getInstance().historyList=filteredData;
        ActivitiesAdmin.getInstance().createTablee(filteredData);
    }

    public void setOnReset(MouseEvent mouseEvent) {
        ActivitiesAdmin.getInstance().createTablee(historyList);
    }

    public void close_filterPane(ActionEvent event) {
        Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();


    }
}
