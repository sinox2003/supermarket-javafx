package Backend.Historique;

import Backend.Dao.AbstractDao;
import Backend.Dao.IHistoriqueDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueDaoImpl extends AbstractDao implements IHistoriqueDao {
    @Override
    public List<Historique> getAll() {
        List<Historique> listeHistoriques = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM historique");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Date dateSql = rs.getDate("date");

                listeHistoriques.add(new Historique(
                        rs.getInt("id"),
                        rs.getInt("idCategorie"),
                        rs.getString("designation"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getString("type"),
                        dateSql != null ? dateSql.toLocalDate() : null));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les historiques");
            e.printStackTrace();
        }
        return listeHistoriques;
    }
    @Override
    public void add(Historique h){
        PreparedStatement pstAction = null;
        String sqlAction = "INSERT INTO historique  VALUES (?,?,?,?,?,?,?)";
        try {
            pstAction = connection.prepareStatement(sqlAction);
            pstAction.setInt(1, h.getId());
            pstAction.setInt(2, h.getIdCategorie());
            pstAction.setString(3, h.getDesignation());
            pstAction.setInt(4, h.getQte());
            pstAction.setDouble(5, h.getPrix());
            pstAction.setString(6, String.valueOf(h.getType()));
            pstAction.setDate(7, Date.valueOf(h.getDate()));
            pstAction.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM historique WHERE id = ?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Historique deleted successfully!");
            } else {
                System.out.println("Historique with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void updateName(String name,int id){
        PreparedStatement pstAction = null;

        try {
                String sqlAction = "UPDATE historique set  designation=? where id=? ";
                pstAction = connection.prepareStatement(sqlAction);
                pstAction.setString(1, name);
                pstAction.setInt(2, id);

            pstAction.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


    public void updatePrice(Historique h){
        PreparedStatement pstAction = null;

        try {
            String sqlAction = "UPDATE historique set   prix=?  where id=? and  type=?";

                pstAction = connection.prepareStatement(sqlAction);



                pstAction.setDouble(1, h.getPrix());

                pstAction.setInt(2, h.getId());
                pstAction.setString(3, String.valueOf(h.getType()));



            pstAction.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateDate(LocalDate date , int id){
        PreparedStatement pstAction = null;

        try {
            String sqlAction = "UPDATE historique set   date=?  where id=? and  type='STOCK_IN' ";

            pstAction = connection.prepareStatement(sqlAction);




            pstAction.setDate(1, Date.valueOf(date));
            pstAction.setInt(2, id);




            pstAction.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM historique WHERE idCategorie = ?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, categoryId);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Historique deleted successfully!");
            } else {
                System.out.println("Historique with category ID " + categoryId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }



}
