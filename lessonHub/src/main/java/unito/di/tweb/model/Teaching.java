package unito.di.tweb.model;

public class Teaching{
    private int id;
    private Professor professor;
    private Course course;
    private boolean active;

    public Teaching(int id, Professor professor, Course course, boolean active){
        this.id = id;
        this.professor = professor;
        this.course = course;
        this.active = active;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Professor getProfessor(){
        return professor;
    }

    public void setProfessor(Professor professor){
        this.professor = professor;
    }

    public Course getCourse(){
        return course;
    }

    public void setCourse(Course course){
        this.course = course;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
