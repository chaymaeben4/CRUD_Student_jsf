package DAO;

import Modele.Student;
import util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO_Imp implements StudentDAO {
    private final DatabaseConfig db = new DatabaseConfig();

    @Override
    public List<Student> selectAll() {
        List<Student> list = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM student");
            while (rs.next()) {
                Student s = new Student(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"));
                list.add(s);
            }
        } catch (SQLException E) {
            E.printStackTrace();
        }
        return list;
    }
    public void saveStudent(Student student){
        try (Connection connection = db.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( "INSERT INTO student" + "  (nom, prenom, email) VALUES "
                     + " (?, ?, ?);")) {
            preparedStatement.setString(1, student.getNom());
            preparedStatement.setString(2, student.getPrenom());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean deleteStudent(int id){
        boolean statut=false;
        try(Connection connection = db.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement("delete from student where id = ?;")){
            preparedStatement.setInt(1,id);
            statut=preparedStatement.executeUpdate() >0;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  statut;
    }


    public void update(Student student){
        try(Connection connection=db.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement("update student set nom = ?,prenom= ?, email =? where id = ?;")){
            preparedStatement.setString(1,student.getNom());
            preparedStatement.setString(2,student.getPrenom());
            preparedStatement.setString(3,student.getEmail());
            preparedStatement.setInt(4,student.getId());
            preparedStatement.executeUpdate() ;
        }
        catch (SQLException E){
            E.printStackTrace();
        }
    }

}
