package DAO;

import Modele.Student;

import java.util.List;

public interface StudentDAO {
    List<Student> selectAll();
    void saveStudent(Student student);
    boolean deleteStudent(int id);
    public void update(Student student);
}
