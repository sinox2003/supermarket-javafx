package Backend.User;

import Backend.Dao.AbstractDao;
import Backend.Dao.IUserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements IUserDao {

    public void add(User obj) {
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO user(username, password, type, status) VALUES (?, ?, ?, ?)";

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, obj.getUsername());
            pst.setString(2, obj.getPassword());
            pst.setString(3, String.valueOf(obj.getType()));
            pst.setString(4, String.valueOf(User.Status.offline));
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

            System.out.println("User ajouté avec ID: " + obj.getId());
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

        String sql = "DELETE FROM user WHERE id=?";
        PreparedStatement pst=null;

        try {
             pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Succes d'exec de la requete!!");
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }finally {
            try {
                pst.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public User getById(int id) {

        User user = null;
        String sql = "SELECT * FROM user WHERE id=?";
        PreparedStatement pst=null;

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            System.out.println("Succes d'exec de la requete!!");
            if (rs.next()) {

                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("type"), rs.getString("status"));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }finally {
            try {
                pst.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user";

        try {
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
            System.out.println("Succes d'exec de la requete!!");
            while (rs.next()) {
                //System.out.println(rs.getLong("id")+" "+rs.getString("email")+" "+rs.getString("password")+" "+rs.getString("type"));
                list.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("type"), rs.getString("status")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }finally {
            try {
                pst.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


        return list;


    }

    @Override
    public User getUsersByInfo(String providedUsername, String providedPassword) {
        User user = null;
        ResultSet rs=null;
        PreparedStatement pst=null;
        String sql = "SELECT * FROM user WHERE username = ? AND BINARY password = ?";

        try {
             pst = connection.prepareStatement(sql);
            pst.setString(1, providedUsername);
            pst.setString(2, providedPassword);

            rs = pst.executeQuery();

            while (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("type"), rs.getString("status"));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }finally {
            try {
                pst.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }

        return user;

    }


    @Override
    public void update(User user) {
        PreparedStatement pst=null;
        try {
            String sql = "UPDATE user SET username=?, password=?, type=?  WHERE id=?";
             pst = connection.prepareStatement(sql);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, String.valueOf(user.getType()));
            pst.setInt(4, user.getId());
            pst.executeUpdate();
            System.out.println("User updated successfully!");
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
    public void updateStatus(User user) {
        PreparedStatement pst=null;
        try {
            String sql = "UPDATE user SET status=?  WHERE id=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, String.valueOf(user.getStatus()));
            pst.setInt(2, user.getId());

            pst.executeUpdate();
            System.out.println("status changed!");
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


}
