package Backend.Historique;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Data
@Getter
@Setter
public class Historique {

    public Historique(int id, int idCategorie, String designation, int qte, double prix, String type, LocalDate date) {
        this.id = id;
        this.idCategorie=idCategorie;
        this.designation = designation;
        this.qte = qte;
        this.prix = prix;
        switch (type){
            case "STOCK_IN":this.type =HistoriqueAction.STOCK_IN;
            break;
            case "STOCK_OUT" :this.type =HistoriqueAction.STOCK_OUT;
            break;
            case "EXPIRED":this.type =HistoriqueAction.EXPIRED;

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        this.date = date;

    }

    public enum HistoriqueAction {
        STOCK_IN,STOCK_OUT,EXPIRED
    }

    private int id;
    private int idCategorie;
    private String designation;
    private int qte;
    private double prix;
    private HistoriqueAction type;//1:entrant | -1:sortant
    private LocalDate date;

    public Historique(int id, int idCategorie, String designation, int qte, double prix, HistoriqueAction type, LocalDate date) {
        this.id = id;
        this.idCategorie=idCategorie;
        this.designation = designation;
        this.qte = qte;
        this.prix = prix;
        this.type = type;
        this.date = date;
    }
    public Historique(int idCategorie, String designation, int qte, double prix, HistoriqueAction type, LocalDate date) {
        this.id = -1;
        this.idCategorie=idCategorie;
        this.designation = designation;
        this.qte = qte;
        this.prix = prix;
        this.type = type;
        this.date = date;

    }



//    public HistoriqueAction getAction(){
//        if(type==1){
//            return HistoriqueAction.STOCK_IN;
//        }else
//            return HistoriqueAction.STOCK_OUT;
//    }


}
