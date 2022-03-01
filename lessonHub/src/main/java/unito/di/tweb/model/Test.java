package unito.di.tweb.model;

import com.google.gson.*;
import unito.di.tweb.dao.MyDAO;

import java.util.ArrayList;

public class Test{
    public static void main(String args[]){
        Gson g = new Gson();
        User u = new User("andrebro", "Andrea", "Brovia", 0, 'M');
        Course c = new Course(1, "Tecnologie Web");
        Professor p = new Professor(1, "Viviana", "Patti", 'F');
        Teaching t = new Teaching(1, p, c, true);
        Booking b = new Booking(u.getUsername(), t, 1, 0);

        System.out.println(g.toJson(u));
        System.out.println(g.toJson(c));
        System.out.println(g.toJson(p));
        System.out.println(g.toJson(t));
        System.out.println(g.toJson(b));
        System.out.println(g.toJson(null));

        MyDAO.registerDriver();
        String string = MyDAO.check_login("andrebro", "pwd");
        System.out.println(string);

        JsonObject jo = new JsonObject();
        jo = g.fromJson(string, JsonObject.class);
        User u2 = g.fromJson(jo.get("response"), User.class);
        System.out.println(g.toJson(u2));

        System.out.println(MyDAO.showAvailableBookings());

        Gson gson = new Gson();

        ArrayList<Course> courses = new ArrayList<>();
        JsonObject jo2 = new JsonObject();
        jo2.addProperty("error", false);
        jo2.add("message", null);
        jo2.addProperty("type", "get_courses");
        courses = MyDAO.getCourses();
        JsonArray array2 = new JsonArray();
        JsonObject data2 = new JsonObject();
        for(Course row : courses){
            array2.add(gson.toJsonTree(row));
        }
        data2.add("data", array2);
        jo2.add("response", data2);
        System.out.println(gson.toJson(jo2));
    }
}
