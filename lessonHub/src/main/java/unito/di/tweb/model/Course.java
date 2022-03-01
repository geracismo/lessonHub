package unito.di.tweb.model;

public class Course{
    private int id;
    private String name;
    private boolean active;

    public Course(int id, String name){
        this.id = id;
        this.name = name;
        this.active = true;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
