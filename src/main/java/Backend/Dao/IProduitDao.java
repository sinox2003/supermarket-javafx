package Backend.Dao;

import Backend.Historique.Historique;
import Backend.Produit.Produit;

import java.util.List;

public interface IProduitDao extends IDao<Produit>{
    public List<Produit> getProduitByKeyword(String designation, int idCategorie);
    public void update(Produit obj, Historique.HistoriqueAction action, int qte , float prix, int type);
    public List<Produit> getProduitByCategorie(int idCategorie);
}
