package anastasijast.example.zivot;

public class Pomosna {
    public String userID;
    public float distance;

    public Pomosna(){}

    public Pomosna(String userID,float distance){
        this.userID=userID;
        this.distance=distance;
    }

    public String getUserID() {
        return userID;
    }

    public float getDistance() {
        return distance;
    }
}
