package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Svoja_aktivnost extends AppCompatActivity {
    TextView ime,tekst,pod,opis,vreme,lok,povt,rejting;
    String kluc;
    Button zavrsi;
    float pr,brojac;
    static Float rez;
    private DatabaseReference databaseReference,dbUser,dbRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svoja_aktivnost);
        rez=(float) 0.0;
        rejting=findViewById(R.id.rejting);
        tekst=findViewById(R.id.tekst);
        kluc=getIntent().getStringExtra("svojaAkt");
        zavrsi=findViewById(R.id.zavrsiAktivnost);
        ime=findViewById(R.id.imeAktivnost);
        pod=findViewById(R.id.podatoci);
        opis=findViewById(R.id.opisAktivnost);
        vreme=findViewById(R.id.vremeAktivnost);
        lok=findViewById(R.id.lokacijaAktivnost);
        povt=findViewById(R.id.povtorlivostAktivnost);
        LinearLayout layout=findViewById(R.id.baranja_L);
        pr= 0.0F;
        brojac= 0.0F;
        dbRating=FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("activities/"+kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baranja value = snapshot.getValue(Baranja.class);

                dbUser = FirebaseDatabase.getInstance().getReference("users/" + value.getUser());
                dbUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String source = "<b> Име: </b><br>" + user.getName() + "<br><b>Телефонски број: </b><br>" + user.getBroj() +
                                "<br><b>Емаил адреса: </b><br>" + user.getEmail();
                        pod.setText(Html.fromHtml(source));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Svoja_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

                    }
                });
                dbRating.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        DataSnapshot snapshot2 = snapshot1.child("rating");
                        Iterable<DataSnapshot> children = snapshot2.getChildren();
                        for (DataSnapshot ds : children) {
                            if (ds.child("doKogo").getValue(String.class).equals("Постар") && value.getUser().equals(ds.child("userID").getValue(String.class))) {
                                pr = pr + ds.child("rejting").getValue(Float.class);
                                brojac++;
                            }
                        }
                        rez = pr / brojac;
                        rejting.setText(rez.toString());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Svoja_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

                    }
                });
                ime.setText("Име на активност: "+value.getIme());
                opis.setText("Опис: \n"+value.getOpis());
                vreme.setText("Датум и време за извршување: \n"+value.getDatum_izvrsuvanje()+" "+value.getVreme());
                lok.setText("Локација: \n"+value.getLokacija());
                povt.setText(value.getPovtorlivost());
                if (value.getStatus().equals("Закажана")){
                    zavrsi.setVisibility(View.VISIBLE);
                    zavrsi.setText("Завршена Активност");
                    zavrsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Svoja_aktivnost.this,"Активноста е завршена!", Toast.LENGTH_SHORT).show();
                            databaseReference.child("status").setValue("Завршена");
                            zavrsi.setVisibility(View.INVISIBLE);
                            //startActivity(new Intent(getApplicationContext(),Osnovno_volonter.class));
                        }
                    });
                }
                if(value.getStatus().equals("Завршена") && value.getIzvestaj()!=3 && value.getIzvestaj()!=2){
                    zavrsi.setVisibility(View.VISIBLE);
                    zavrsi.setText("Поднеси извештај");
                    tekst.setVisibility(View.VISIBLE);
                    zavrsi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),Izvestaj_aktivnost.class);
                            String k=kluc+"/"+"volonter";
                            intent.putExtra("zaAktivnost",k);
                            startActivity(intent);
                        }
                    });
                }
                if(value.getStatus().equals("Завршена") && value.getIzvestaj()==3) {
                    zavrsi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Svoja_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}