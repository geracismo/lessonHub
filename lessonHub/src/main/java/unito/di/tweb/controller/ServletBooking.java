package unito.di.tweb.controller;

import com.google.gson.*;

import unito.di.tweb.dao.*;
import unito.di.tweb.model.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



@WebServlet(name = "ServletBooking", value = "/ServletBooking")
public class ServletBooking extends HttpServlet {

    private int timeout = 10;

    public void init(ServletConfig conf){
        MyDAO.registerDriver();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestManager(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestManager(request, response);
    }

    private void requestManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = response.getWriter();

        HttpSession session;

        session = request.getSession(false);

        if(session == null){
            session = request.getSession();
            session.setMaxInactiveInterval(10 * 60);
            System.out.println("CREATING SESSION");
        }

        System.out.println("COOKIE ID: " + session.getId());

        boolean logged = isLogged(session);

        //System.out.println("LOGGED: "+logged+" COOKIE ID: "+session.getId());

        Gson gson = new Gson();

        BufferedReader reader = request.getReader();

        JsonObject obj = gson.fromJson(reader, JsonObject.class);

        System.out.println(obj.toString());

        String type = obj.get("type").isJsonNull() == false ? obj.get("type").getAsString() : "";

        JsonObject auth = obj.get("authorization").getAsJsonObject();

        boolean auth_status = auth.get("email").isJsonNull() || auth.get("password").isJsonNull() ? false : true;

        switch(type){
            case "add_delete_course_professor":
                if(isAdmin(session)){
                    int ic = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("course").getAsJsonObject().get("id").getAsInt();
                    int id = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("professor").getAsJsonObject().get("id").getAsInt();
                    boolean flag = obj.get("params").getAsJsonObject().get("mode").getAsInt() == 0 ? true : false;
                    out.println(MyDAO.add_delete_course_professor(id, ic, flag));
                }else if(auth_status){
                    out.println(gson.toJson(this.send_error("Mobile Not Allowed", "add_delete_course_professor")));
                }else{
                    out.println(gson.toJson(this.send_error("Authorization Error", "add_delete_course_professor")));
                }
                break;
            case "add_delete_course":
                if(isAdmin(session)){
                    int id = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("course").getAsJsonObject().get("id").getAsInt();
                    boolean flag = obj.get("params").getAsJsonObject().get("mode").getAsInt() == 0 ? true : false;
                    String name = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("course").getAsJsonObject().get("name").getAsString();
                    out.println(MyDAO.add_delete_course(name, id, flag));
                }else if(auth_status){
                    out.println(gson.toJson(this.send_error("Mobile Not Allowed", "add_delete_course")));
                }else{
                    out.println(gson.toJson(this.send_error("Authorization Error", "add_delete_course")));
                }
                break;
            case "add_delete_professor":
                if(isAdmin(session)){
                    int id = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("professor").getAsJsonObject().get("id").getAsInt();
                    boolean flag = obj.get("params").getAsJsonObject().get("mode").getAsInt() == 0 ? true : false;
                    String name = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("professor").getAsJsonObject().get("name").getAsString();
                    String surname = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("professor").getAsJsonObject().get("surname").getAsString();
                    char mfx = flag ? obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("professor").getAsJsonObject().get("mfx").getAsString().charAt(0) : ' ';
                    out.println(MyDAO.add_delete_professor(name, surname, mfx, id, flag));
                }else if(auth_status){
                    out.println(gson.toJson(this.send_error("Mobile Not Allowed", "add_delete_professor")));
                }else{
                    out.println(gson.toJson(this.send_error("Authorization Error", "add_delete_professor")));
                }
                break;
            case "book_reservation":
                if(logged || auth_status){
                    boolean check = false;
                    String username = null;
                    if(auth_status){
                        username = auth.get("email").getAsString();
                        String pwd = auth.get("password").getAsString();
                        String json = MyDAO.check_login(username, pwd);
                        if(gson.fromJson(json, JsonObject.class).get("error").getAsBoolean() == false){
                            check = true;
                        }
                    }
                    if(logged){
                        check = true;
                        username = (String) session.getAttribute("username");
                    }
                    if(check){
                        int id = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("teaching").getAsInt();
                        int day = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("slot").getAsJsonObject().get("day").getAsInt();
                        int hour = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("slot").getAsJsonObject().get("hour").getAsInt();
                        out.println(MyDAO.addBooking(username, id, Slot.generateSlotNumber(day, hour)));
                    }else{
                        out.println(this.send_error("Authorization Error", "book_reservation"));
                    }
                }else{
                    out.println(this.send_error("Authorization Error", "book_reservation"));
                }
                break;
            case "delete_done_reservation":
                if(logged || auth_status){
                    boolean check = false;
                    String username = null;
                    int role = -1;
                    if(auth_status){
                        username = auth.get("email").getAsString();
                        String pwd = auth.get("password").getAsString();
                        String json = MyDAO.check_login(username, pwd);
                        role = gson.fromJson(json, JsonObject.class).get("response").getAsJsonObject().get("role").getAsInt();
                        if(gson.fromJson(json, JsonObject.class).get("error").getAsBoolean() == false){
                            check = true;
                        }
                    }
                    if(logged){
                        check = true;
                        username = (String) session.getAttribute("username");
                        role = (int) session.getAttribute("role");
                        if(role == 1){
                            if(obj.get("authorization").getAsJsonObject().get("email").isJsonNull() == false){
                                username = obj.get("authorization").getAsJsonObject().get("email").getAsString();
                            }
                        }
                    }
                    if(check){
                        int id = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("teaching").getAsInt();
                        int day = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("slot").getAsJsonObject().get("day").getAsInt();
                        int hour = obj.get("data").getAsJsonObject().get("elem").getAsJsonObject().get("reservation").getAsJsonObject().get("slot").getAsJsonObject().get("hour").getAsInt();
                        boolean flag = obj.get("params").getAsJsonObject().get("mode").getAsInt() == 0;
                        if(role == 1){
                            check = flag ? false : true;
                        }
                        if(check){
                            if(role == 1){
                                if(obj.get("params").getAsJsonObject().get("username") != null){
                                    username = obj.get("params").getAsJsonObject().get("username").getAsString();
                                }
                            }
                            out.println(MyDAO.done_delete_booking(username, id, Slot.generateSlotNumber(day, hour), flag));
                        }else{
                            out.println(this.send_error("Authorization Error", "delete_done_reservation"));
                        }
                    }else{
                        out.println(this.send_error("Authorization Error", "delete_done_reservation"));
                    }
                }else{
                    out.println(this.send_error("Authorization Error", "delete_done_reservation"));
                }
                break;
            case "login":
                if(auth_status){
                    String username = auth.get("email").getAsString();
                    String pwd = auth.get("password").getAsString();
                    String json = MyDAO.check_login(username, pwd);
                    if(gson.fromJson(json, JsonObject.class).get("error").getAsBoolean() == false){
                        User u = gson.fromJson(gson.fromJson(json, JsonObject.class).get("response"), User.class);
                        session.setAttribute("username", u.getUsername());
                        session.setAttribute("password", pwd);
                        session.setAttribute("role", u.getRole());
                        System.out.println("LOGIN");
                    }
                    out.println(json);
                }else{
                    out.println(this.send_error("Incorrect Login Authorization", "login"));
                }
                break;
            case "check_session":
                if(logged){
                    JsonObject ret = new JsonObject();
                    ret.addProperty("error", false);
                    ret.add("message", null);
                    ret.addProperty("type", "check_session");
                    ret.addProperty("response", "Session valid");
                    out.println(gson.toJson(ret));
                }else{
                    out.println(this.send_error("Session Invalid", "check_session"));
                }
                break;
            case "logout":
                if(logged){
                    try {
                        session.invalidate();
                        JsonObject ret = new JsonObject();
                        ret.addProperty("error", false);
                        ret.add("message", null);
                        ret.addProperty("type", "logout");
                        ret.addProperty("response", "Logout OK");
                        out.println(gson.toJson(ret));
                    }catch(IllegalStateException e){
                        out.println(gson.toJson(this.send_error("User is logged out", "logout")));
                    }
                }else{
                    out.println(gson.toJson(this.send_error("User is logged out", "logout")));
                }
                break;
            case "user_show_history":
                if(logged || auth_status){
                    boolean check = false;
                    String username = null;
                    if(auth_status){
                        username = auth.get("email").getAsString();
                        String pwd = auth.get("password").getAsString();
                        String json = MyDAO.check_login(username, pwd);
                        if(gson.fromJson(json, JsonObject.class).get("error").getAsBoolean() == false){
                            check = true;
                        }
                    }
                    if(logged){
                        check = true;
                        username = (String) session.getAttribute("username");
                    }
                    if(check){
                        out.println(MyDAO.getHistory(username, true));
                    }else{
                        out.println(this.send_error("Authorization Error", "user_show_history"));
                    }
                }else{
                    out.println(this.send_error("Authorization Error", "user_show_history"));
                }
                break;
            case "show_my_reservations":
                if(logged || auth_status){
                    boolean check = false;
                    String username = null;
                    if(auth_status){
                        username = auth.get("email").getAsString();
                        String pwd = auth.get("password").getAsString();
                        String json = MyDAO.check_login(username, pwd);
                        if(gson.fromJson(json, JsonObject.class).get("error").getAsBoolean() == false){
                            check = true;
                        }
                    }
                    if(logged){
                        check = true;
                        username = (String) session.getAttribute("username");
                    }
                    if(check){
                        out.println(MyDAO.getHistory(username, false));
                    }else{
                        out.println(this.send_error("Authorization Error", "show_my_reservations"));
                    }
                }else{
                    out.println(this.send_error("Authorization Error", "show_my_reservations"));
                }
                break;
            case "admin_show_all_history":
                if(isAdmin(session)){
                    out.println(MyDAO.adminHistory());
                }else if(auth_status){
                    out.println(gson.toJson(this.send_error("Mobile Not Allowed", "admin_show_all_history")));
                }else{
                    out.println(gson.toJson(this.send_error("Authorization Error", "admin_show_all_history")));
                }
                break;
            case "show_available_reservations":
                out.println(MyDAO.showAvailableBookings());
                break;
            case "get_teachings":
                ArrayList<Teaching> teachings = new ArrayList<>();
                JsonObject jo = new JsonObject();
                jo.addProperty("error", false);
                jo.add("message", null);
                jo.addProperty("type", "get_teachings");
                teachings = MyDAO.getTeachings();
                JsonArray array = new JsonArray();
                JsonObject data = new JsonObject();
                for(Teaching t : teachings){
                    JsonObject row = new JsonObject();
                    row.addProperty("id", t.getId());
                    row.add("course", gson.toJsonTree(t.getCourse()));
                    row.add("professor", gson.toJsonTree(t.getProfessor()));
                    array.add(row);
                }
                data.add("data", array);
                jo.add("response", data);
                out.println(gson.toJson(jo));
                break;
            case "get_professors":
                JsonObject json = new JsonObject();
                json.addProperty("error", false);
                json.add("message", null);
                json.addProperty("type", "get_professors");
                ArrayList<Professor> professors = new ArrayList<>();
                if(obj.get("params").isJsonNull() == false){
                    int id = obj.get("params").getAsJsonObject().get("course").getAsJsonObject().get("id").getAsInt();
                    professors = MyDAO.getProfessorsByFilter(id);
                }else{
                    professors = MyDAO.getProfessors();
                }
                JsonArray array1 = new JsonArray();
                JsonObject data1 = new JsonObject();
                for(Professor p : professors){
                    array1.add(gson.toJsonTree(p));
                }
                data1.add("data", array1);
                json.add("response", data1);
                out.println(gson.toJson(json));
                break;
            case "get_courses":
                ArrayList<Course> courses = new ArrayList<>();
                JsonObject jo2 = new JsonObject();
                jo2.addProperty("error", false);
                jo2.add("message", null);
                jo2.addProperty("type", "get_courses");
                courses = MyDAO.getCourses();
                JsonArray array2 = new JsonArray();
                JsonObject data2 = new JsonObject();
                for(Course c : courses){
                    array2.add(gson.toJsonTree(c));
                }
                data2.add("data", array2);
                jo2.add("response", data2);
                out.println(gson.toJson(jo2));
                break;
            default:
        }
    }

    private boolean isLogged(HttpSession session){
        boolean ret;
        try{
            ret = session.getAttribute("username") == null ? false : true;
        }catch(IllegalStateException e){
            ret = false;
        }
        return ret;
    }

    private boolean isAdmin(HttpSession session){
        /*boolean ret = false;
        if(isLogged(session)){
            try{
                ret = session.getAttribute("role").toString().equals("1");
            }catch(IllegalStateException e){
                ret = false;
            }
        }
        return ret;*/
        return true;
    }

    private JsonObject send_error(String msg, String type){
        JsonObject ret = new JsonObject();
        ret.addProperty("error", true);
        ret.addProperty("message", msg);
        ret.addProperty("type", type);
        ret.add("response", null);

        return ret;
    }
}
