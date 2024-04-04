package com.example.project;
import Backend.Categorie.CategorieDaoImpl;
import Backend.Historique.Historique;
import Backend.Historique.HistoriqueDaoImpl;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;
import animatefx.animation.RotateIn;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.DoubleFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
public class ProductsByCategory implements Initializable {
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    @FXML
    private GridPane grid;
    @FXML
    private Button refreshproduits_btn ;
    @FXML
    private Text refresh_icon;
    @FXML
    MFXTableView<Produit> ProduitsTable;
    private ProduitDaoImpl produitsDao = new ProduitDaoImpl();
    private CategorieDaoImpl categorieDao = new CategorieDaoImpl();
    private HistoriqueDaoImpl hdao = new HistoriqueDaoImpl();
    int categorieNumber;
    //panes
    @FXML
    Pane products_pane;

    private  static ProductsByCategory instance;



    public ProductsByCategory(){
        instance=this;
    }
    public  static ProductsByCategory getInstance(){
        return instance;
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

        dialogContent.addActions(
                Map.entry(new MFXButton("Confirm"), Event -> {
                    categorieDao.delete(categorieNumber);

                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), Event -> dialog.close())
        );

        dialogContent.setMaxSize(400, 200);

    }
    @FXML
    private void openWarning(ActionEvent event) {
        DialogsController(event);
        MFXFontIcon warnIcon = new MFXFontIcon("mfx-do-not-enter-circle", 18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.getStyleClass().add("mfx-warn-dialog");

        Label label=new Label("En supprimant la catégorie , tous les produits associés à celle-ci seront\négalement supprimés.\nÊtes-vous sûr(e) de vouloir procéder ?");

        dialogContent.setCenter(label);
        dialogContent.setHeaderText("This is a warning info dialog");

        dialog.showDialog();
    }
    /* ////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\   */






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refreshproduits_btn.setOnMousePressed(event ->{
            RotateIn rotateIn=new RotateIn(refresh_icon);
            rotateIn.setSpeed(0.7);
            rotateIn.play();
        });


    }


    /*   ////////////////////////  PRODUIT METHODS POURCHAQUE CATEGORIE \\\\\\\\\\\\\\\\\\\\\\\*/
    public void setup_table_produit(){
        MFXTableColumn<Produit> idColumnProduit = new MFXTableColumn<>("Id", true, Comparator.comparing(Produit::getId));
        MFXTableColumn<Produit> designationColumn = new MFXTableColumn<>("Product name", true, Comparator.comparing(Produit::getProduct_name));
        MFXTableColumn<Produit> quantiteColumn = new MFXTableColumn<>("Quantity", true, Comparator.comparing(Produit::getQuantity));
        MFXTableColumn<Produit> retail_priceColumn = new MFXTableColumn<>("Product price ", true, Comparator.comparing(Produit::getRetail_price));
        MFXTableColumn<Produit> selling_priceColumn = new MFXTableColumn<>("selling price", true, Comparator.comparing(Produit::getRetail_price));
        MFXTableColumn<Produit> dateColumn = new MFXTableColumn<>("Arrival date", true, Comparator.comparing(Produit::getArrival_date));
        MFXTableColumn<Produit> peremptionColumn = new MFXTableColumn<>("Expiration date", true, Comparator.comparing(Produit::getExpiration_date));
        MFXTableColumn<Produit> actionColumn = new MFXTableColumn<>("Action", true);

        idColumnProduit.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getId));
        designationColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getProduct_name));
        quantiteColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getQuantity));
        retail_priceColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getRetail_price));
        selling_priceColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getSelling_price));
        dateColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getArrival_date));
        peremptionColumn.setRowCellFactory(Produit -> new MFXTableRowCell<>(Backend.Produit.Produit::getExpiration_date));
        actionColumn.setRowCellFactory(produit -> new MFXTableRowCell<>(data -> data.getId(), data -> {return null;}) {
            {
                setAlignment(Pos.TOP_LEFT);
                setMinWidth(180);
                MFXButton deleteButton = new MFXButton();
                Button editButton = new Button();
                Button stock_outButton = new Button();
                stock_outButton.getStyleClass().add("stockOut-button-tableView");
                deleteButton.getStyleClass().add("delete-button-tableView");
                editButton.getStyleClass().add("edit-button-tableView");

                stock_outButton.setTooltip(new Tooltip("remove from stock"));
                deleteButton.setTooltip(new Tooltip("delete product"));

                editButton.setOnAction(event -> edit(produit));
                deleteButton.setOnAction(event -> {
                    deleteProduct(produit);
                });
                stock_outButton.setOnAction(event -> removeProduct(produit));

                HBox hBox=new HBox(editButton,stock_outButton,deleteButton);
                hBox.setSpacing(25);
                setGraphic(hBox);
            }
        });

        idColumnProduit.setMinWidth(95);
        designationColumn.setMinWidth(195);
        quantiteColumn.setMinWidth(145);
        retail_priceColumn.setMinWidth(113);
        selling_priceColumn.setMinWidth(113);
        dateColumn.setMinWidth(165);
        peremptionColumn.setMinWidth(165);

        ProduitsTable.getTableColumns().addAll(idColumnProduit, designationColumn,quantiteColumn,retail_priceColumn,selling_priceColumn,dateColumn,peremptionColumn,actionColumn);
        ProduitsTable.getFilters().addAll(
                new IntegerFilter<>("Id", Produit::getId),
                new StringFilter<>("Name", Produit::getProduct_name),
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
    };

    @FXML
    public void initializeProduits() {
        ObservableList<Produit> produitsList = FXCollections.observableArrayList(produitsDao.getProduitByCategorie(categorieNumber));
        ProduitsTable.setItems(produitsList);

    }




    public void deleteProduct(Produit produit) {
        produitsDao.delete(produit.getId());
        hdao.delete(produit.getId());
        initializeProduits();
    }
    public void removeProduct(Produit produit) {
        produitsDao.delete(produit.getId());
        Historique h = new Historique(produit.getId(),produit.getIdCategory(), produit.getProduct_name(), produit.getQuantity(),
                produit.getSelling_price(), Historique.HistoriqueAction.STOCK_OUT, LocalDate.now());

        hdao.add(h);
        initializeProduits();
    }

    public void addProduct(){
        try {

            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/project/FXML/AddProduct.fxml"));
            Parent root=loader.load();
            AddProduct addProduct=loader.getController();
            addProduct.categorieNumber=categorieNumber;
            AdminPage.getInstance().changePage(root);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  void goBack(ActionEvent event) {
        AdminPage.getInstance().changePage("/com/example/project/FXML/CategoriesAdmin.fxml");

    }

    public  void edit(Produit produit){
        try {

            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/project/FXML/EditProduct.fxml"));
            Parent root=loader.load();
            EditProduct editProduct=loader.getController();
            editProduct.product=produit;
            editProduct.setValues(produit);
            AdminPage.getInstance().changePage(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
