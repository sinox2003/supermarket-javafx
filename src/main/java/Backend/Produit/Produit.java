package Backend.Produit;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class Produit {

    private int id;
    private int idCategory;
    private String product_name;
    private int quantity;
    private double retail_price;
    private double selling_price;

    private LocalDate arrival_date;
    private LocalDate expiration_date;

    public Produit(int id, int idCategory, String product_name, int quantity, double retail_price,double selling_price, LocalDate arrival_date, LocalDate expiration_date) {
        this.id = id;
        this.idCategory=idCategory;
        this.product_name = product_name;
        this.quantity = quantity;
        this.retail_price = retail_price;
        this.selling_price=selling_price;
        this.arrival_date = arrival_date;
        this.expiration_date=expiration_date;
    }
   public Produit(int idCategory, String product_name, int quantity, double retail_price,double selling_price, LocalDate arrival_date, LocalDate expiration_date) {
        this.id = -1;
        this.idCategory=idCategory;
        this.product_name = product_name;
        this.quantity = quantity;
        this.retail_price = retail_price;
       this.selling_price=selling_price;
        this.arrival_date = arrival_date;
        this.expiration_date=expiration_date;
    }


}
