package Backend.Categorie;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Categorie {
    private int id;
    private String nom;
    public Categorie(int id, String nom){
        this.id = id;
        this.nom = nom;
    }
    public Categorie(String nom){
        this.nom = nom;
    }

}
