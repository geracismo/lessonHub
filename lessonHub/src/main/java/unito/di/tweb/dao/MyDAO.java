package unito.di.tweb.dao;

import com.google.gson.*;
import unito.di.tweb.model.*;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.*;

public class MyDAO{
    private static final String url = "jdbc:mysql://localhost:3306/lessonhub";
    private static final String user = "root";
    private static final String pwd = "";
    private static Connection conn;

    public static void registerDriver(){
        try{
            DriverManager.registerDriver(new Driver());
        }catch(SQLException e){
            System.out.println("Driver Error: "+e.getMessage());
        }
    }

    public static void openConn(){
        try{
            conn = DriverManager.getConnection(MyDAO.url, MyDAO.user, MyDAO.pwd);
        }catch(SQLException e){
            System.out.println("Connection Error: "+e.getMessage()+" "+e.getSQLState());
            conn = null;
        }
    }

    public static void closeConn(){
        if(conn != null){
            try{
                conn.close();
                conn = null;
            }catch(SQLException e){
                System.out.println("Connection Error: "+e.getMessage()+" "+e.getSQLState());
            }
        }
    }

    public static String check_login(String username, String pwd){
        openConn();
        User u = null;
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT * FROM utente u WHERE u.username = \""+username+"\" AND u.password = \""+pwd+"\"");
            if(r.first()){
                u = new User(r.getString("username"), r.getString("nome"), r.getString("cognome"), r.getInt("ruolo"), r.getInt("mfx") == 1 ? 'F' : r.getInt("mfx") == 0 ? 'M' : 'X');
                jo.addProperty("error", false);
                jo.add("message", null);
                jo.addProperty("type", "login_check");
                jo.add("response", g.toJsonTree(u));
            }else{
                jo.addProperty("error", true);
                jo.addProperty("message", "Wrong username or password");
                jo.addProperty("type", "login_check");
                jo.add("response", null);
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            jo.addProperty("error", true);
            jo.addProperty("message", "Wrong username or password");
            jo.addProperty("type", "login_check");
            jo.add("response", null);
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static String add_delete_course(String name, int id, boolean flag){
        openConn();
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        try{
            Statement stat = conn.createStatement();
            if(flag){
                stat.executeUpdate("INSERT INTO corso (nome) VALUES (\""+name+"\")");
            }else{
                stat.executeUpdate("UPDATE corso SET attivo = 0 WHERE id = "+id);
                stat.executeUpdate("UPDATE corso_docente SET attivo = 0 WHERE corso_id = "+id);
            }
            jo.addProperty("error", false);
            jo.add("message", null);
            jo.addProperty("type", "add_delete_course");
            jo.add("response", null);
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            jo.addProperty("error", true);
            jo.addProperty("message", "Error on query");
            jo.addProperty("type", "add_delete_course");
            jo.add("response", null);
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static String add_delete_professor(String name, String surname, char mfx, int id, boolean flag){
        openConn();
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        int i = mfx == 'M' ? 0 : mfx == 'F' ? 1 : 2;
        try{
            Statement stat = conn.createStatement();
            if(flag){
                stat.executeUpdate("INSERT INTO docente(nome, cognome, mfx) VALUES (\""+name+"\", \""+surname+"\", "+i+")");
            }else{
                stat.executeUpdate("UPDATE docente SET attivo = 0 WHERE id = "+id);
                stat.executeUpdate("UPDATE corso_docente SET attivo = 0 WHERE docente_id = "+id);
            }
            jo.addProperty("error", false);
            jo.add("message", null);
            jo.addProperty("type", "add_delete_professor");
            jo.add("response", null);
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            jo.addProperty("error", true);
            jo.addProperty("message", "Error on query");
            jo.addProperty("type", "add_delete_professor");
            jo.add("response", null);
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static String add_delete_course_professor(int id, int ic, boolean flag){
        openConn();
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        try{
            Statement stat = conn.createStatement();
            if(flag){
                stat.executeUpdate("INSERT INTO corso_docente(docente_id, corso_id) VALUES ("+id+", "+ic+")");
            }else{
                stat.executeUpdate("UPDATE corso_docente SET attivo = 0 WHERE docente_id = "+id+" AND corso_id = "+ic);
            }
            jo.addProperty("error", false);
            jo.add("message", null);
            jo.addProperty("type", "add_delete_course_professor");
            jo.add("response", null);
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            jo.addProperty("error", true);
            jo.addProperty("message", "Error on query");
            jo.addProperty("type", "add_delete_course_professor");
            jo.add("response", null);
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static int getProfessorID(String name, String surname){
        openConn();
        int ret = -1;
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT d.id FROM docente d WHERE d.nome = \""+name+"\" AND d.cognome = \""+surname+"\"");
            if(r.first()){
                ret = r.getInt("id");
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return ret;
    }

    public static int getCourseID(String name){
        openConn();
        int ret = -1;
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT c.id FROM corso c WHERE c.nome = \""+name+"\"");
            if(r.first()){
                ret = r.getInt("id");
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return ret;
    }

    public static ArrayList<String> getUsers(){
        ArrayList<String> users = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT username FROM utente WHERE 1");
            while(r.next()){
                users.add(r.getString("username"));
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return users;
    }

    public static ArrayList<Booking> getUserBookings(String username, int type){ //type fix (attuale 0,1,2), per prendere tutti i bookings senza richiamare la funzione per ogni type
        ArrayList<Booking> bookings = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT u.username, u.nome, u.cognome, p.insegnamento_id, c.nome, d.nome, d.cognome, d.mfx, p.slot, p.stato, cd.docente_id, cd.corso_id FROM prenotazione p, utente u, corso c, docente d, corso_docente cd WHERE p.insegnamento_id = cd.id AND p.username = u.username AND c.id = cd.corso_id AND d.id = cd.docente_id AND p.username = \""+username+"\"");
            while(r.next()){
                Booking booking = new Booking(username, new Teaching(r.getInt("insegnamento_id"), new Professor(r.getInt("docente_id"), r.getString(6), r.getString(7), r.getString("mfx").charAt(0)), new Course(r.getInt("corso_id"), r.getString(5)), true), r.getInt("slot"), r.getInt("stato"));
                if(booking.getStatus() == type || type == 3){
                    bookings.add(booking);
                }
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return bookings;
    }

    public static String getHistory(String username, boolean mode){
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings = MyDAO.getUserBookings(username, mode ? 3 : 0);
        JsonObject elem = new JsonObject();
        elem.addProperty("error", false);
        elem.add("message", null);
        elem.addProperty("type", mode ? "user_show_history" : "show_my_reservations");
        JsonObject data = new JsonObject();
        JsonArray array = new JsonArray();
        Gson gson = new Gson();
        for(Booking book : bookings){
            JsonObject row = new JsonObject();
            JsonObject slot = new JsonObject();
            String[] datetime = Slot.fromSlotNumber(book.getSlot()).split(";");
            String[] day = datetime[0].split(" ");
            String[] hour = datetime[1].split(" ");
            slot.addProperty("day", Integer.parseInt(day[1]));
            day = hour[1].split(":");
            slot.addProperty("hour", Integer.parseInt(day[0]));
            row.addProperty("teaching", book.getTeaching().getId());
            row.add("slot", slot);
            row.add("course", gson.toJsonTree(book.getTeaching().getCourse()));
            row.add("professor", gson.toJsonTree(book.getTeaching().getProfessor()));
            row.addProperty("status", book.getStatus());
            array.add(row);
        }
        data.add("data", array);
        elem.add("response", data);

        return gson.toJson(elem);
    }

    public static JsonArray getHistory(String username){
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings = MyDAO.getUserBookings(username, 3);
        JsonArray array = new JsonArray();
        Gson gson = new Gson();
        for(Booking book : bookings){
            JsonObject row = new JsonObject();
            JsonObject slot = new JsonObject();
            String[] datetime = Slot.fromSlotNumber(book.getSlot()).split(";");
            String[] day = datetime[0].split(" ");
            String[] hour = datetime[1].split(" ");
            slot.addProperty("day", Integer.parseInt(day[1]));
            day = hour[1].split(":");
            slot.addProperty("hour", Integer.parseInt(day[0]));
            row.addProperty("teaching", book.getTeaching().getId());
            row.add("slot", slot);
            row.add("course", gson.toJsonTree(book.getTeaching().getCourse()));
            row.add("professor", gson.toJsonTree(book.getTeaching().getProfessor()));
            row.addProperty("status", book.getStatus());
            array.add(row);
        }

        return array;
    }

    public static String adminHistory(){
        ArrayList<String> users = new ArrayList<>();
        Gson gson = new Gson();
        users = MyDAO.getUsers();
        JsonObject elem = new JsonObject();
        elem.addProperty("error", false);
        elem.add("message", null);
        elem.addProperty("type", "admin_show_all_history");
        JsonArray array = new JsonArray();
        JsonObject data = new JsonObject();
        for(String u : users){
            JsonObject row = new JsonObject();
            row.addProperty("username", u);
            row.add("data", MyDAO.getHistory(u));
            array.add(row);
        }
        data.add("users", array);
        elem.add("response", data);

        return gson.toJson(elem);
    }

    public static int getTeachingID(String name, String surname, String name_c){
        openConn();
        int ret = -1;
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT cd.id FROM corso_docente cd, docente d, corso c WHERE cd.docente_id = d.id AND cd.corso_id = c.id AND d.nome = \""+name+"\" AND d.cognome = \""+surname+"\" AND c.nome = \""+name_c+"\"");
            if(r.first()){
                ret = r.getInt("id");
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return ret;
    }

    public static String done_delete_booking(String username, int id, int slot, boolean flag){
        openConn();
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        try{
            Statement stat = conn.createStatement();
            if(flag){
                stat.executeUpdate("UPDATE prenotazione p SET stato = 1 WHERE p.username = \""+username+"\" AND p.insegnamento_id = "+id+" AND p.slot = "+slot+" AND p.stato = 0");
            }else{
                stat.executeUpdate("UPDATE prenotazione p SET stato = 2 WHERE p.username = \""+username+"\" AND p.insegnamento_id = "+id+" AND p.slot = "+slot+" AND p.stato = 0");
            }
            jo.addProperty("error", false);
            jo.add("message", null);
            jo.addProperty("type", "done_delete_booking");
            jo.add("response", null);
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            boolean duplicate = false;
            if(((e.getMessage().contains("Duplicate entry") && e.getMessage().contains("for key")) || Integer.parseInt(e.getSQLState()) == 23000) && !flag){
                try{
                    Statement stat = conn.createStatement();
                    stat.executeUpdate("DELETE FROM prenotazione WHERE username = \""+username+"\" AND insegnamento_id = "+id+" AND slot = "+slot+" AND stato = 0");
                    duplicate = true;
                }catch(SQLException e1){
                    System.out.println("Query Error: "+e1.getMessage()+" "+e1.getSQLState());
                }
            }
            if(duplicate){
                System.out.println("Duplicated row, ignore");
                jo.addProperty("error", false);
                jo.add("message", null);
                jo.addProperty("type", "done_delete_booking");
                jo.add("response", null);
            }else{
                jo.addProperty("error", true);
                jo.addProperty("message", "Error on query");
                jo.addProperty("type", "done_delete_booking");
                jo.add("response", null);
            }
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static String addBooking(String username, int id, int slot){
        openConn();
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        try{
            Statement stat = conn.createStatement();
            stat.executeUpdate("INSERT INTO prenotazione(username, insegnamento_id, slot) VALUES (\""+username+"\", "+id+", "+slot+")");
            jo.addProperty("error", false);
            jo.add("message", null);
            jo.addProperty("type", "add_booking");
            jo.add("response", null);
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
            jo.addProperty("error", true);
            jo.addProperty("message", "Error on query");
            jo.addProperty("type", "add_booking");
            jo.add("response", null);
        }finally{
            closeConn();
        }
        return g.toJson(jo);
    }

    public static String showAvailableBookings(){
        Gson g = new Gson();
        JsonObject jo = new JsonObject();
        JsonArray bookings = new JsonArray();
        ArrayList<Teaching> teachings = MyDAO.getTeachings();
        ArrayList<Professor> professors = MyDAO.getProfessors();
        for(Professor d : professors){
            for(int i = 1; i < 21; i++){
                boolean available = MyDAO.professorAvailable(d.getName(), d.getSurname(), i);
                if(available){
                    for(Teaching t : teachings){
                        if(t.getProfessor().getId() == d.getId()){
                            JsonObject row = new JsonObject();
                            row.addProperty("teaching_id", t.getId());
                            row.addProperty("professor_id", t.getProfessor().getId());
                            row.addProperty("course_id", t.getCourse().getId());
                            row.addProperty("name", t.getProfessor().getName());
                            row.addProperty("surname", t.getProfessor().getSurname());
                            row.addProperty("mfx", String.valueOf(t.getProfessor().getMfx()));
                            row.addProperty("course_name", t.getCourse().getName());
                            row.addProperty("slot", Slot.fromSlotNumber(i));
                            bookings.add(row);
                        }
                    }
                }
            }
        }
        jo.addProperty("error", false);
        jo.add("message", null);
        jo.addProperty("type", "show_available_reservations");
        if(bookings.isEmpty()){
            jo.add("response", null);
        }else{
            jo.add("response", bookings);
        }
        return g.toJson(jo);
    }

    private static boolean professorAvailable(String name, String surname, int slot){
        boolean ret = false;
        openConn();
        try {
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT * FROM prenotazione p, corso_docente cd WHERE p.slot = "+slot+" AND p.stato != 2 AND p.insegnamento_id = cd.id AND cd.attivo = 1 AND cd.docente_id IN (SELECT d.id FROM docente d WHERE d.nome = \""+name+"\" AND d.cognome = \""+surname+"\")");
            if(!r.first()){
                ret = true;
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return ret;
    }

    public static ArrayList<Course> getCourses(){
        ArrayList<Course> courses = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT c.id, c.nome FROM corso c WHERE c.attivo = 1");
            while(r.next()){
                Course row = new Course(r.getInt("id"), r.getString("nome"));
                courses.add(row);
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return courses;
    }

    public static ArrayList<Professor> getProfessorsByFilter(int id){
        ArrayList<Professor> professors = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT DISTINCT d.id, d.nome, d.cognome, d.mfx FROM docente d WHERE d.attivo = 1 AND d.id NOT IN (SELECT cd2.docente_id FROM corso_docente cd2 WHERE cd2.corso_id = "+id+")");
            while(r.next()){
                Professor row = new Professor(r.getInt("id"), r.getString("nome"), r.getString("cognome"), ' ');
                row.setMfx(r.getInt("mfx"));
                professors.add(row);
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return professors;
    }

    public static ArrayList<Professor> getProfessors(){
        ArrayList<Professor> professors = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT d.id, d.nome, d.cognome, d.mfx FROM docente d WHERE d.attivo = 1");
            while(r.next()){
                Professor row = new Professor(r.getInt("id"), r.getString("nome"), r.getString("cognome"), ' ');
                row.setMfx(r.getInt("mfx"));
                professors.add(row);
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return professors;
    }

    public static ArrayList<Teaching> getTeachings(){
        ArrayList<Teaching> teachings = new ArrayList<>();
        openConn();
        try{
            Statement stat = conn.createStatement();
            ResultSet r = stat.executeQuery("SELECT cd.id, cd.docente_id, cd.corso_id, d.nome, d.cognome, d.mfx, c.nome AS corso_nome FROM corso_docente cd, docente d, corso c WHERE cd.docente_id = d.id AND cd.corso_id = c.id AND cd.attivo = 1");
            while(r.next()){
                Teaching row = new Teaching(r.getInt("id"), new Professor(r.getInt("docente_id"), r.getString("nome"), r.getString("cognome"), ' '), new Course(r.getInt("corso_id"), r.getString("corso_nome")), true);
                row.getProfessor().setMfx(r.getInt("mfx"));
                teachings.add(row);
            }
        }catch(SQLException e){
            System.out.println("Query Error: "+e.getMessage()+" "+e.getSQLState());
        }finally{
            closeConn();
        }
        return teachings;
    }
}
