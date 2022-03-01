package unito.di.tweb.model;

public class User{
    private String username;
    private String name;
    private String surname;
    private int role;
    private char mfx;

    public User(String username, String name, String surname, int role, char mfx){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.mfx = mfx;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
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

    public int getRole(){
        return role;
    }

    public void setRole(int role){
        this.role = role;
    }

    public char getMfx(){
        return mfx;
    }

    public void setMfx(int mfx){
        char c = mfx == 0 ? 'M' : mfx == 1 ? 'F' : 'X';
        this.mfx = c;
    }
}
