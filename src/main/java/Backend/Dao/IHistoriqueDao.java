package Backend.Dao;

import Backend.Historique.Historique;

import java.util.List;

public interface IHistoriqueDao {
    public List<Historique> getAll();
    public void add(Historique h);
    public void delete(int id);
}
