package Backend.Produit;

import Backend.Dao.AbstractDao;
import Backend.Dao.IProduitDao;
import Backend.Historique.Historique;
import Backend.Historique.HistoriqueDaoImpl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduitDaoImpl extends AbstractDao implements IProduitDao {
    @Override
    public void add(Produit obj) {
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO produit (idCategorie, designation, quantite, prix,selling_price, date, peremption) VALUES (?, ?, ?, ?, ?, ?,?)";
        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, obj.getIdCategory());
            pst.setString(2, obj.getProduct_name());
            pst.setInt(3, obj.getQuantity());
            pst.setDouble(4, obj.getRetail_price());
            pst.setDouble(5, obj.getSelling_price());
            pst.setDate(6, Date.valueOf(obj.getArrival_date()));
            if (obj.getExpiration_date() != null) {
                pst.setDate(7, Date.valueOf(obj.getExpiration_date()));
            } else {
                pst.setNull(7, Types.DATE);
            }
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys
                generatedKeys = pst.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Set the generated ID to your object
                    obj.setId(generatedKeys.getInt(1)); // Assuming the ID column is the first column
                } else {
                    System.out.println("Failed to retrieve the generated ID.");
                }
            }

            System.out.println("Backend.Produit ajouté avec ID: " + obj.getId());
            Historique h = new Historique(obj.getId(),obj.getIdCategory(), obj.getProduct_name(), obj.getQuantity(),
                    obj.getRetail_price(), Historique.HistoriqueAction.STOCK_IN, LocalDate.now());
            HistoriqueDaoImpl hdao = new HistoriqueDaoImpl();
            hdao.add(h);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Close the ResultSet and PreparedStatement in the finally block
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }




    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM produit WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public Produit getById(int id) {
        PreparedStatement pst = null;
        String sql = "SELECT * FROM produit WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Date dateSql = rs.getDate("date");
                Date peremptionSql = rs.getDate("peremption");

                return new Produit(
                        rs.getInt("id"),
                        rs.getInt("idCategorie"),
                        rs.getString("designation"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getDouble("selling_price"),
                        dateSql != null ? dateSql.toLocalDate() : null,
                        peremptionSql != null ? peremptionSql.toLocalDate() : null);
            } else {
                return null; // Aucun produit trouvé avec l'ID spécifié
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Fermer le PreparedStatement dans le bloc finally
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @Override
    public List<Produit> getAll() {
        List<Produit> listeProduits = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM produit");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Date dateSql = rs.getDate("date");
                Date peremptionSql = rs.getDate("peremption");

                listeProduits.add(new Produit(
                        rs.getInt("id"),
                        rs.getInt("idCategorie"),
                        rs.getString("designation"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getDouble("selling_price"),

                        dateSql != null ? dateSql.toLocalDate() : null,
                        peremptionSql != null ? peremptionSql.toLocalDate() : null));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les produits");
            e.printStackTrace();
        }
        return listeProduits;
    }

    @Override
    public List<Produit> getProduitByCategorie(int idCategorie){
        List<Produit> listeProduits = new ArrayList<Produit>();
        String sql = "SELECT * FROM produit WHERE idCategorie=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idCategorie);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Date dateSql = rs.getDate("date");
                    Date peremptionSql = rs.getDate("peremption");

                    listeProduits.add(new Produit(
                            rs.getInt("id"),
                            rs.getInt("idCategorie"),
                            rs.getString("designation"),
                            rs.getInt("quantite"),
                            rs.getDouble("prix"),
                            rs.getDouble("selling_price"),

                            dateSql != null ? dateSql.toLocalDate() : null,
                            peremptionSql != null ? peremptionSql.toLocalDate() : null));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par mot-clé");
            e.printStackTrace();
        }
        return listeProduits;
    }
    @Override
    public List<Produit> getProduitByKeyword(String designation, int idCategorie){
        if (designation.isEmpty()) return getProduitByCategorie(idCategorie);
        List<Produit> listeProduits = new ArrayList<Produit>();
        String sql = "SELECT * FROM produit WHERE idCategorie=? and designation LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idCategorie);
            pst.setString(2, '%'+designation+'%');
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Date dateSql = rs.getDate("date");
                    Date peremptionSql = rs.getDate("peremption");

                    listeProduits.add(new Produit(
                            rs.getInt("id"),
                            rs.getInt("idCategorie"),
                            rs.getString("designation"),

                            rs.getInt("quantite"),
                            rs.getDouble("prix"),
                            rs.getDouble("selling_price"),

                            dateSql != null ? dateSql.toLocalDate() : null,
                            peremptionSql != null ? peremptionSql.toLocalDate() : null));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par mot-clé");
            e.printStackTrace();
        }
        return listeProduits;
    }
    @Override
    public void update(Produit obj, Historique.HistoriqueAction action, int qte , float price, int type) {
        //action: 0 rien/1 ajout/ 2 retrait
        PreparedStatement pst = null;
        String sql = "UPDATE produit SET designation=?, quantite=?, prix=?,selling_price=?, date=?, peremption=? WHERE id=?";
        try {
            pst = connection.prepareStatement(sql);
            //pst.setInt(1, obj.getIdCategorie());
            pst.setString(1, obj.getProduct_name());
            pst.setInt(2, obj.getQuantity());
            pst.setDouble(3, obj.getRetail_price());
            pst.setDouble(4, obj.getSelling_price());
            pst.setDate(5, Date.valueOf(obj.getArrival_date()));
            if (obj.getExpiration_date() != null) {
                pst.setDate(6, Date.valueOf(obj.getExpiration_date()));
            } else {
                pst.setNull(6, Types.DATE);
            }
            pst.setInt(7, obj.getId());
            pst.executeUpdate();


//            int rowsUpdated = pst.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("Product updated successfully!");
//            } else {
//                System.out.println("Product with ID " + obj.getId() + " not found.");
//            }

            HistoriqueDaoImpl hdao = new HistoriqueDaoImpl();
            if(action== Historique.HistoriqueAction.STOCK_IN || action== Historique.HistoriqueAction.STOCK_OUT){
                Historique h = new Historique(obj.getId(),obj.getIdCategory(), obj.getProduct_name(), qte, price, action, LocalDate.now());
                hdao.add(h);
            }
            else {
                switch (type) {
                    case 2:
                        Historique h1 = new Historique(obj.getId(), obj.getIdCategory(), obj.getProduct_name(), qte, obj.getRetail_price(), Historique.HistoriqueAction.STOCK_IN, LocalDate.now());
                        Historique h2 = new Historique(obj.getId(), obj.getIdCategory(), obj.getProduct_name(), qte, obj.getSelling_price(), Historique.HistoriqueAction.STOCK_OUT, LocalDate.now());
                        hdao.updatePrice(h1);
                        hdao.updatePrice(h2);
                        break;
                    case 1:
                        Historique h3 = new Historique(obj.getId(), obj.getIdCategory(), obj.getProduct_name(), qte, price, Historique.HistoriqueAction.STOCK_IN, LocalDate.now());
                        hdao.updatePrice(h3);
                        break;
                    case -1:
                        Historique h4 = new Historique(obj.getId(), obj.getIdCategory(), obj.getProduct_name(), qte, price, Historique.HistoriqueAction.STOCK_OUT, LocalDate.now());
                        hdao.updatePrice(h4);
                        break;
                }
            }
            hdao.updateName(obj.getProduct_name(),obj.getId());
            hdao.updateDate(obj.getArrival_date(),obj.getId());


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
