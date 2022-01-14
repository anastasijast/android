package anastasijast.example.zivot;

public class Baranja {
    public String user;
    public String ime;
    public String opis;
    public String vreme;
    public String povtorlivost;
    public String itnost;
    public String lokacija;
    public String status;
    public String volonter;
    public String data;
    public String datum_izvrsuvanje;
    public int izvestaj;
    //0,ako nema,1 ako podnel postar,2 volonter,3 dvajcata

    public Baranja(){}

    public Baranja(String user,String ime, String opis, String vreme,String povtorlivost, String itnost, String lokacija,String status,String volonter,String data,String datum_izvrsuvanje,int izvestaj){
        this.user=user;
        this.ime=ime;
        this.opis=opis;
        this.vreme=vreme;
        this.povtorlivost=povtorlivost;
        this.itnost=itnost;
        this.lokacija=lokacija;
        this.status=status;
        this.volonter=volonter;
        this.data=data;
        this.datum_izvrsuvanje=datum_izvrsuvanje;
        this.izvestaj=izvestaj;
    }

    public String getDatum_izvrsuvanje() {
        return datum_izvrsuvanje;
    }

    public String getUser(){ return user; }
    public String getIme(){ return ime; }
    public String getOpis(){ return opis; }
    public String getVreme(){ return vreme; }
    public String getPovtorlivost(){ return povtorlivost; }
    public String getLokacija(){ return lokacija; }
    public String getStatus() { return status; }
    public String getVolonter() { return volonter; }

    public int getIzvestaj() {
        return izvestaj;
    }

    public String getData() {
        return data;
    }
}
