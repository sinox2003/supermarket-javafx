package Backend.Dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {
    public void add(T obj) throws SQLException;
    public void delete(int id) throws SQLException;
    public T getById(int id) throws SQLException;
    public List<T> getAll() throws SQLException;

}
