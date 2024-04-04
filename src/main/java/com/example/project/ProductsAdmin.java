package com.example.project;

import Backend.Categorie.CategorieDaoImpl;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;
import animatefx.animation.RotateIn;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ProductsAdmin implements Initializable {


    @FXML
    private MFXFontIcon refresh_icon;
    @FXML
    private MFXTableView<Produit> ProduitsTable;
    private final ProduitDaoImpl produitsDao = new ProduitDaoImpl();
    private final CategorieDaoImpl categorieDao = new CategorieDaoImpl();


    private  static ProductsAdmin instance;



    public ProductsAdmin(){
        instance=this;
    }
    public  static ProductsAdmin getInstance(){
        return instance;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh_icon.setOnMousePressed(event ->{
            RotateIn rotateIn=new RotateIn(refresh_icon);
            rotateIn.setSpeed(0.7);
            rotateIn.play();
        });

        setup_table_ALLProducts();
        initialize_AllProducts();
    }








@FXML
public void setup_table_ALLProducts(){
    MFXTableColumn<Produit> idColumnProduit = new MFXTableColumn<>("Product Id", true, Comparator.comparing(Produit::getId));
    MFXTableColumn<Produit> idCategorie_ColumnProduit = new MFXTableColumn<>("Category",true);
    MFXTableColumn<Produit> designationColumn = new MFXTableColumn<>("Product name", true, Comparator.comparing(Produit::getProduct_name));
    MFXTableColumn<Produit> quantiteColumn = new MFXTableColumn<>("Quantity", true, Comparator.comparing(Produit::getQuantity));
    MFXTableColumn<Produit> retail_priceColumn = new MFXTableColumn<>("Retail price", true, Comparator.comparing(Produit::getRetail_price));
    MFXTableColumn<Produit> selling_priceColumn = new MFXTableColumn<>("selling price", true, Comparator.comparing(Produit::getRetail_price));
    MFXTableColumn<Produit> dateColumn = new MFXTableColumn<>("Arrival date", true, Comparator.comparing(Produit::getArrival_date));
    MFXTableColumn<Produit> peremptionColumn = new MFXTableColumn<>("Expiration date", true, Comparator.comparing(Produit::getExpiration_date));

    idColumnProduit.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getId));
    idCategorie_ColumnProduit.setRowCellFactory(produit -> new MFXTableRowCell<>(data -> data.getId(), data -> {
        return  categorieDao.getNomCategorieFromId(produit.getIdCategory());

    }));
    designationColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getProduct_name));
    quantiteColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getQuantity));
    retail_priceColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getRetail_price));
    selling_priceColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getSelling_price));
    dateColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getArrival_date));
    peremptionColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getExpiration_date));


    idColumnProduit.setMinWidth(100);
    idCategorie_ColumnProduit.setMinWidth(190);
    designationColumn.setMinWidth(190);
    quantiteColumn.setMinWidth(120);
    retail_priceColumn.setMinWidth(110);
    selling_priceColumn.setMinWidth(110);

    dateColumn.setMinWidth(145);

    peremptionColumn.setMinWidth(145);

    retail_priceColumn.setMinWidth(120);
    ProduitsTable.getTableColumns().addAll(idColumnProduit,idCategorie_ColumnProduit, designationColumn,quantiteColumn,retail_priceColumn,selling_priceColumn,dateColumn,peremptionColumn);
    ProduitsTable.getFilters().addAll(
            new IntegerFilter<>("Product Id", Produit::getId),
            new StringFilter<>("Category", produit ->   {
                return  categorieDao.getNomCategorieFromId(produit.getIdCategory());}
            ),

    new StringFilter<>("Product name", Produit::getProduct_name),
            new IntegerFilter<>("Quantity", Produit::getQuantity),
            new DoubleFilter<>("Retail price", Produit::getRetail_price),
            new DoubleFilter<>("Selling price", Produit::getSelling_price),

            new StringFilter<>("Arrival date",produit -> {
                return  produit.getArrival_date().toString();
            }),
            new StringFilter<>("Expiration date",produit -> {
                return  produit.getExpiration_date().toString();
            })
            
    );
}
@FXML
public void initialize_AllProducts() {
    ObservableList<Produit> produitsList = FXCollections.observableArrayList(produitsDao.getAll());
    ProduitsTable.setItems(produitsList);
}












}
