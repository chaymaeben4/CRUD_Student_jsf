package service;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

import DAO.StudentDAO;
import DAO.StudentDAO_Imp;
import Modele.Student;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.bean.ViewScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlCommandButton;
import jakarta.faces.component.html.HtmlDataTable;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;

@ManagedBean
@ViewScoped
public class StudentBean {
    private boolean showAddStudentForm;
    private int studentIdToDelete;
    private HtmlDataTable htmlDataTable;
    private HtmlInputText inputId;
    private HtmlInputText inputNom;
    private HtmlInputText inputPrenom;
    private HtmlInputText inputEmail;
    private List<Student> students;
    private int pageNumber = 1; //numero de page actielle
    private int pageSize = 10;  //nombre de lignes a afficher dans chaque page
    private Student studentEdit;

    public Student getStudentEdit() {
        return studentEdit;
    }

    public void setStudentEdit(Student studentEdit) {
        this.studentEdit = studentEdit;
    }

    public StudentBean() {
        this.showAddStudentForm=false;
        this.studentDAO = new StudentDAO_Imp();
        this.htmlDataTable=new HtmlDataTable();
        this.studentAdd=new Student();
        this.inputId=new HtmlInputText();
        this.inputNom=new HtmlInputText();
        this.inputPrenom=new HtmlInputText();
        this.inputEmail=new HtmlInputText();
        this.studentEdit=new Student();
        this.students = studentDAO.selectAll();
    }





    public int getStudentIdToDelete() {
        return studentIdToDelete;
    }

    public void setStudentIdToDelete(int studentIdToDelete) {
        this.studentIdToDelete = studentIdToDelete;
    }

    public HtmlInputText getInputNom() {
        return inputNom;
    }

    public void setInputNom(HtmlInputText inputNom) {
        this.inputNom = inputNom;
    }

    public HtmlInputText getInputPrenom() {
        return inputPrenom;
    }

    public void setInputPrenom(HtmlInputText inputPrenom) {
        this.inputPrenom = inputPrenom;
    }

    public HtmlInputText getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(HtmlInputText inputEmail) {
        this.inputEmail = inputEmail;
    }





    public HtmlInputText getInputId() {
        return inputId;
    }

    public void setInputId(HtmlInputText inputId) {
        this.inputId = inputId;
    }

    private StudentDAO studentDAO;
    private Student studentAdd;

    public Student getStudentAdd() {
        return studentAdd;
    }

    public boolean isShowAddStudentForm() {
        return showAddStudentForm;
    }

    public void setShowAddStudentForm(boolean showAddStudentForm) {
        this.showAddStudentForm = showAddStudentForm;
    }

    public void setStudentAdd(Student studentAdd) {
        this.studentAdd = studentAdd;
    }



    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void saveStudent() {
        this.showAddStudentForm=false;
        studentDAO.saveStudent(studentAdd);
        System.out.println("student saved");
        students=studentDAO.selectAll();
    }

    public void addStudent(){
        System.out.println("row added");
        this.showAddStudentForm=true;

    }

    public void deleteStudent(){
        if(studentDAO.deleteStudent(studentIdToDelete)){
            System.out.println("deleted");
            students=studentDAO.selectAll();
        }
        else{
            System.out.println("not deleted");
        }

    }

    // Méthode pour récupérer les étudiants visibles sur la page actuelle
    public List<Student> getVisibleStudents() {
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, students.size());
        return students.subList(start, end);
    }
    // Méthode pour aller à la page précédente
    public void previousPage() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }

    // Méthode pour aller à la page suivante
    public void nextPage() {
        if (isHasMoreStudents()) {
            pageNumber++;
        }
    }

    // Méthode pour vérifier s'il y a plus d'étudiants à afficher sur la page suivante
    public boolean isHasMoreStudents() {
        int start = pageNumber * pageSize;
        return start < getStudents().size();
    }

    @Named
    public void toggleEditMode() {
        System.out.println(studentEdit);
        int index = students.indexOf(studentEdit);
        System.out.println(index);
        if (index != -1) {
            Student studentToUpdate = students.get(index);
            studentToUpdate.setEditMode(!studentToUpdate.getEditMode());
            students.set(index, studentToUpdate);
            System.out.println(students.get(index).getEditMode());
        }
    }
    public void saveChanges() {
        try {
            System.out.println("Saving changes for student: " + studentEdit.getId());
            System.out.println("Selected Student before update: " + studentEdit);

            studentDAO.update(studentEdit);
            toggleEditMode();
            students = studentDAO.selectAll();

            System.out.println("Update successful for student: " +studentEdit.getPrenom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = (String) value;
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email address.", null);
            throw new ValidatorException(message);
        }
    }
}
