package anastasijast.example.zivot;

public class User {
    public String name;
    public String email;
    public String password;
    public String broj;
    public String kategorija;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String password, String broj,String kategorija) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.broj = broj;
        this.kategorija=kategorija;
    }
    public String getName(){
        return name;
    }
    public String getKategorija(){
        return kategorija;
    }
    public String getBroj(){
        return broj;
    }
    public String getEmail(){ return email; }
}