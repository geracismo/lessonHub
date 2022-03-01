package unito.di.tweb.model;

public class Professor{
    private int id;
    private String name;
    private String surname;
    private char mfx;
    private boolean active;

    public Professor(int id, String name, String surname, char mfx){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mfx = mfx;
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

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public char getMfx(){
        return mfx;
    }

    public void setMfx(int mfx){
        char c = mfx == 0 ? 'M' : mfx == 1 ? 'F' : 'X';
        this.mfx = c;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
