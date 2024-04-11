package com.example.project;

import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.Comparator;
import java.util.List;

public class DonutData {

    private  String data;
    private  double value;
    private  Color color;
    private String percentedValue;
    static double total;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPercentedValue() {
        return percentedValue;
    }

    public void setPercentedValue(String percentedValue) {
        this.percentedValue = percentedValue;
    }

    public DonutData(String data, double value) {
        this.data = data;
        this.value = value;
    }

    public static List<DonutData> pourcentage(List<DonutData> dataList){
        double percentage=0;

        dataList.sort(Comparator.comparing(DonutData::getValue).reversed());
        double others=dataList.stream().skip(5).mapToDouble(DonutData::getValue).sum();
        dataList=dataList.subList(0,5);
        dataList.addLast(new DonutData("Others",others));
        total = dataList.stream().mapToDouble(DonutData::getValue).sum();
        for(DonutData donutData:dataList){
            donutData.percentedValue=String.format("%.1f",(donutData.getValue() / total)*100)+"%";
            percentage += (donutData.getValue() / total) ;
            donutData.setValue(percentage);

        }

        return  dataList.reversed();

    }


    public Arc createArc(double radius, double stroke){
        int angle=348;
        if(value>=0.99)
            angle=360;
        Arc arc=new Arc(0,0,radius,radius,0,value*angle);
        arc.setStrokeWidth(stroke);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(color);
        arc.setCacheHint(CacheHint.QUALITY);
        arc.strokeLineJoinProperty().set(StrokeLineJoin.ROUND);
        arc.strokeLineCapProperty().set(StrokeLineCap.ROUND);

        return arc;

    }




    @Override
    public String toString() {
        return "DonutData{" +
                "data='" + data + '\'' +
                ", value=" + value +
                ", color=" + color +
                '}';
    }
}
