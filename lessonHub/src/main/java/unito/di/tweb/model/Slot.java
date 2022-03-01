package unito.di.tweb.model;

public class Slot{

    public static int generateSlotNumber(int day, int hour){
        int ret = 0;
        int d = 0;
        int h = 0;

        switch(day){
            case 1:
                d = 4;
                break;
            case 2:
                d = 8;
                break;
            case 3:
                d = 12;
                break;
            case 4:
                d = 16;
                break;
            case 5:
                d = 20;
                break;
        }

        switch(hour){
            case 15:
                h = 3;
                break;
            case 16:
                h = 2;
                break;
            case 17:
                h = 1;
                break;
            case 18:
                h = 0;
                break;
        }

        ret = d - h;

        return ret;
    }

    public static String fromSlotNumber(int slot){
        int d = slot / 4;
        int h = slot % 4;

        d += h == 0 ? 0 : 1;

        String ret = "";

        switch(d){
            case 1:
                ret += "Lunedì 01;";
                break;
            case 2:
                ret += "Martedì 02;";
                break;
            case 3:
                ret += "Mercoledì 03;";
                break;
            case 4:
                ret += "Giovedì 04;";
                break;
            case 5:
                ret += "Venerdì 05;";
        }

        switch(h){
            case 0:
                ret += "Ore 18:00";
                break;
            case 1:
                ret += "Ore 15:00";
                break;
            case 2:
                ret += "Ore 16:00";
                break;
            case 3:
                ret += "Ore 17:00";
        }

        return ret;
    }
}
