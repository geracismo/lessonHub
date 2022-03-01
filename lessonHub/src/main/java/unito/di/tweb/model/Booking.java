package unito.di.tweb.model;

public class Booking{
    private String username;
    private Teaching teaching;
    private int slot;
    private int status;

    public Booking(String username, Teaching teaching, int slot, int status){
        this.username = username;
        this.teaching = teaching;
        this.slot = slot;
        this.status = status;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public Teaching getTeaching(){
        return teaching;
    }

    public void setTeaching(Teaching teaching){
        this.teaching = teaching;
    }

    public int getSlot(){
        return slot;
    }

    public void setSlot(int slot){
        this.slot = slot;
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }
}
