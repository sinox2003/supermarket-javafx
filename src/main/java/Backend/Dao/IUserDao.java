package Backend.Dao;

import Backend.User.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao extends IDao<User> {
//    public List<User> getByType(String type);
    public User getUsersByInfo(String providedUsername, String providedPassword) throws SQLException;
    public void update(User user) throws SQLException;
    public void updateStatus(User user);


}
