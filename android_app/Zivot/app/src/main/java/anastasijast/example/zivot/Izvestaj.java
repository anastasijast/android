package anastasijast.example.zivot;

public class Izvestaj {
    public String userID;
    public String volonterID;
    public String opis;
    public Float rejting;
    public String datum;
    public String doKogo;
//0 ako nikoj,1 ako postaro,2 ako volonter ispratil,3 ako dvajcata
    public Izvestaj(){}

    public Izvestaj(String userID,String volonterID,String opis,Float rejting,String datum,String doKogo){
        this.userID=userID;
        this.volonterID=volonterID;
        this.opis=opis;
        this.rejting=rejting;
        this.datum=datum;
        this.doKogo=doKogo;
    }

    public String getUserID() {
        return userID;
    }

    public String getVolonterID() {
        return volonterID;
    }

    public String getOpis() {
        return opis;
    }

    public Float getRejting() {
        return rejting;
    }
}
