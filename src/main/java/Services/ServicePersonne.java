package Services;

import Entites.Personne;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import Utils.DataSource;

public class ServicePersonne implements IService<Personne> {

    private Connection con = DataSource.getInstance().getConn();
    private Statement st;

    public ServicePersonne() {
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Personne personne) throws SQLException {
        String req = "INSERT INTO `personne` (`id`, `nom`, `prenom`, `age`) VALUES (NULL, '" + personne.getNom() + "', '" + personne.getPrenom() + "', '" + personne.getAge() + "');";

        st.executeUpdate(req);
    }

    public void ajouterPSTM(Personne personne) throws SQLException {
        PreparedStatement pre = con.prepareStatement("INSERT INTO `personne` ( `nom`, `prenom`, `age`) VALUES (?,?,?);");
        pre.setString(1, personne.getNom());
        pre.setString(2, personne.getPrenom());
        pre.setInt(3, personne.getAge());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Personne personne) throws SQLException {

    }

    @Override
    public void update(Personne personne) throws SQLException {

    }

    @Override
    public Personne getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Personne> getAll() throws SQLException {
        List<Personne>  list=new ArrayList<>();
        ResultSet rs=st.executeQuery("SELECT * FROM personne;");
        while (rs.next()) {

            int id=rs.getInt(1);
            String nom=rs.getString("nom");
            String prenom=rs.getString("prenom");
            int age=rs.getInt("age");
            Personne p=new Personne(id,nom,prenom,age);
            list.add(p);
        }


        return list;
    }
}
