package Backend.Categorie;

import Backend.Dao.AbstractDao;
import Backend.Dao.ICategorieDao;
import Backend.User.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategorieDaoImpl extends AbstractDao implements ICategorieDao {

    @Override
    public void add(Categorie obj) {
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO categorie(nom)  VALUES (?)";
        try {
            pst = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//            pst.setInt(1, obj.getId());
            pst.setString(1, obj.getNom());
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                // Récupérer les clés générées
                generatedKeys = pst.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Définir l'ID généré sur votre objet Categorie
                    obj.setId(generatedKeys.getInt(1));
                } else {
                    System.out.println("Impossible de récupérer l'ID généré.");
                }

                System.out.println("Catégorie ajoutée avec ID: " + obj.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Fermer le ResultSet et le PreparedStatement dans le bloc finally
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

    public void update(Categorie categorie) {
        PreparedStatement pst=null;
        try {
            String sql = "UPDATE categorie SET id=?, nom=? WHERE id=?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, categorie.getId());
            pst.setString(2, categorie.getNom());
            pst.setInt(3, categorie.getId());
            pst.executeUpdate();
            System.out.println("category updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                pst.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM categorie WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Category with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Categorie getById(int id) {
        PreparedStatement pst = null;
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("nom"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categorie> getAll() {
        List<Categorie> listeCategories = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM categorie");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listeCategories.add(new Categorie(
                        rs.getInt("id"),
                        rs.getString("nom")));
            }
            System.out.println("getall");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les catégories");
            e.printStackTrace();
        }
        return listeCategories;
    }
    public String getNomCategorieFromId(int id){
        PreparedStatement pst = null;
        ResultSet rst = null;
        String sql = "SELECT nom FROM categorie WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rst = pst.executeQuery();
            if(rst.next()){
                return rst.getString("nom");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }




}
