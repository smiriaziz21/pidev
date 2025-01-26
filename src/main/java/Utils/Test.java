package Utils;

import Entites.Personne;
import Services.ServicePersonne;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Personne p1=new Personne("test 1","test1",12);

        ServicePersonne ser=new ServicePersonne();

        try {
            ser.ajouter(p1);
            System.out.println("personne ajoutée");
        } catch (SQLException e) {
            System.out.println(p1);
        }

try {
    List<Personne> l1=ser.getAll();
    l1.forEach(e-> System.out.println(e));
}catch(SQLException e)
{
    System.out.println(e);
}

    }
}
