package com.example.project;

import Backend.Categorie.Categorie;
import Backend.Categorie.CategorieDaoImpl;
import Backend.Historique.Historique;
import Backend.Historique.HistoriqueDaoImpl;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardAdmin implements Initializable {





    @FXML
    private BarChart<String,Double> barchart2;

    @FXML
    private BarChart<String,Double> barchart1;
    @FXML
    private NumberAxis incomeYaxis,outcomeYaxis;
    @FXML
    private Label maxBar1,maxBar2;
    @FXML
    Pane pieChartPane;
    @FXML
    Arc arc1,arc2,arc3,arc4,arc5,arc6;
    List<Arc> arcList=new ArrayList<>();


    HistoriqueDaoImpl historiqueDao=new HistoriqueDaoImpl();
    CategorieDaoImpl categorieDao=new CategorieDaoImpl();
    List<Historique> historyList=new ArrayList<>();
    List<Categorie> categoryList =new ArrayList<>();


    public static DashboardAdmin instance;




    public static DashboardAdmin getInstance() {
        return instance;
    }

    public DashboardAdmin(){
        instance=this;
    }


    public static void setInstance(DashboardAdmin instance) {
        DashboardAdmin.instance = instance;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                refreshActionsTable();
            }
        }));
        timeline.play();


        arcList.addAll(List.of( arc1,arc2,arc3,arc4,arc5,arc6));

    }

    public void refreshActionsTable(){

        categoryList=categorieDao.getAll();
        historyList=historiqueDao.getAll();

        setBarchart(historyList);
        createDonutChart(setDonutData(historyList,categoryList));

    }


    public void setBarchart(List<Historique> historyList){

        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        for (int i = 1; i <= 12; i++) {
            String month = getMonthName(i);
            series1.getData().add(new XYChart.Data<>(month, incomeOfMonth(i,historyList)));

            series2.getData().add(new XYChart.Data<>(month, outcomeOfMonth(i,historyList)));
        }





        barchart2.getData().clear();
        barchart1.getData().clear();
        barchart2.getData().add(series2);
        barchart1.getData().add(series1);


        barchart2.setBarGap(10);

        barchart1.setBarGap(10);


        double max1 = series1.getData().stream().mapToDouble(XYChart.Data::getYValue).max().orElse(0);
        double max2 = series2.getData().stream().mapToDouble(XYChart.Data::getYValue).max().orElse(0);
        double maxOverall = Math.max(max1, max2);

        double upperBound = maxOverall*1.25 ; // Adding some padding for better visualizatio
        maxBar1.setText(String.valueOf(Math.round(upperBound/2)));
        maxBar2.setText(String.valueOf(Math.round(-upperBound/2)));
        incomeYaxis.setTickUnit(0);
        outcomeYaxis.setTickUnit(0);
        incomeYaxis.setUpperBound(upperBound);
        outcomeYaxis.setUpperBound(upperBound);


    }


    public void createDonutChart(List<DonutData> dataList){

        for (int i=0;i<dataList.size();i++){
            int angle=348;
            if(dataList.get(i).getValue()>=0.99)
                angle=360;

            arcList.get(i).setLength(dataList.get(i).getValue()*angle);
            MFXTooltip tooltip=new MFXTooltip(arcList.get(i),dataList.get(i).getData() +" ("+dataList.get(i).getPercentedValue()+")");

            int finalI = i;
            arcList.get(i).setOnMouseEntered(event -> tooltip.show(arcList.get(finalI), event.getScreenX(), event.getScreenY()));
            arcList.get(i).setOnMouseExited(event -> tooltip.hide());

        }

    }



    public List<DonutData> setDonutData(List<Historique> historyList,List<Categorie> categoryList){
        List<DonutData> dataList=new ArrayList<>();
        for (int i=0 ; i<categoryList.size() ; i++){
            dataList.add(new DonutData(categoryList.get(i).getNom(), gainByCategory(historyList,categoryList.get(i).getId())));

        }

        return DonutData.pourcentage(dataList);
    }


    public double gainByCategory(List<Historique> historyList,int id){

        double income=historyList.stream().filter(historique ->historique.getIdCategorie()==id && historique.getType() == Historique.HistoriqueAction.STOCK_OUT ).mapToDouble(historique -> { return historique.getPrix()*historique.getQte();}).sum();
        double outcome=historyList.stream().filter(historique ->historique.getIdCategorie()==id && (historique.getType() == Historique.HistoriqueAction.STOCK_IN || historique.getType()== Historique.HistoriqueAction.EXPIRED) ).mapToDouble(historique -> { return historique.getPrix()*historique.getQte();}).sum();

        return income < outcome ? 0 : income - outcome;

    }



    public double outcomeOfMonth(int month,List<Historique> historyList){
        int days =daysOfMonth(month);

        return  historyList.stream().filter(historique -> historique.getDate().isBefore(LocalDate.of(LocalDate.now().getYear(), month, days)) && historique.getDate().isAfter(LocalDate.of(LocalDate.now().getYear(), month, 1)) && (historique.getType() == Historique.HistoriqueAction.STOCK_IN || historique.getType()== Historique.HistoriqueAction.EXPIRED)).
                mapToDouble(historique -> { return historique.getPrix()*historique.getQte();}).sum();

    }

    public double incomeOfMonth(int month,List<Historique> historyList){
        int days = daysOfMonth(month);

        return  historyList.stream().filter(historique -> historique.getDate().isBefore(LocalDate.of(LocalDate.now().getYear(), month, days)) && historique.getDate().isAfter(LocalDate.of(LocalDate.now().getYear(), month, 1)) && historique.getType() == Historique.HistoriqueAction.STOCK_OUT ).
                mapToDouble(historique -> { return historique.getPrix()*historique.getQte();}).sum();

    }

    public int daysOfMonth(int month){
        return switch (month) {
            case 4, 6, 9, 11 -> 30;
            case 2 -> 28; // Assuming non-leap year by default
            default -> 31;
        };
    }


    public String getMonthName(int month) {
        // Replace with your implementation
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }




}
