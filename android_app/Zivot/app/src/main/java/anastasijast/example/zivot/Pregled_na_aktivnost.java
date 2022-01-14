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

public class Pregled_na_aktivnost extends AppCompatActivity {
    TextView ime,pod,opis,vreme,lok,povt;
    String kluc;
    Button prifati,odbij,izvestaj;
    float pr,brojac;
    static Float rez;
    private DatabaseReference databaseReference, dbUser, dbRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregled_na_aktivnost);
        kluc = getIntent().getStringExtra("aktivnost");
        rez=(float)0.0;
        pr=(float)0.0;
        brojac=(float)0.0;
        ime = findViewById(R.id.imeAktivnost);
        pod = findViewById(R.id.podatoci);
        opis = findViewById(R.id.opisAktivnost);
        vreme = findViewById(R.id.vremeAktivnost);
        lok = findViewById(R.id.lokacijaAktivnost);
        povt = findViewById(R.id.povtorlivostAktivnost);
        LinearLayout layout = findViewById(R.id.baranja_L);
        LinearLayout prif=findViewById(R.id.prifakjanje);
        prifati=findViewById(R.id.prifati);
        odbij=findViewById(R.id.odbij);
        izvestaj=findViewById(R.id.izvestaj);
        dbRating= FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("activities/" + kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baranja value = snapshot.getValue(Baranja.class);
                dbUser = FirebaseDatabase.getInstance().getReference("users/" + value.getVolonter());
                if(value.getStatus().equals("Активна")==false){
                    dbUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
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
                                    String source = "<b> Име: </b><br>" + user.getName() + "<br><b>Телефонски број: </b><br>" + user.getBroj()
                                            + "<br><b>Емаил адреса: </b><br>" + user.getEmail()+"<br><b>Рејтинг: </b>"+rez.toString();
                                    pod.setVisibility(View.VISIBLE);
                                    pod.setText(Html.fromHtml(source));

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Pregled_na_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Pregled_na_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                ime.setText("Име на активност: "+value.getIme());
                opis.setText("Опис: \n"+value.getOpis());
                vreme.setText("Датум и време за извршување: \n"+value.getDatum_izvrsuvanje()+" "+value.getVreme());
                lok.setText("Локација: \n"+value.getLokacija());
                povt.setText(value.getPovtorlivost());
                if(value.getStatus().equals("Активна")){
                    izvestaj.setVisibility(View.VISIBLE);
                    izvestaj.setText("Избриши Активност");
                    izvestaj.setBackgroundColor(Color.parseColor("#ff4500"));
                    DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("activities/" + kluc);
                    izvestaj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();
                                    startActivity(new Intent(getApplicationContext(),Osnovno_povozrasen.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });
                        }
                    });
                }
                if(value.getStatus().equals("Пријавен Волонтер")){
                    prif.setVisibility(View.VISIBLE);
                    prifati.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Pregled_na_aktivnost.this,"Волонтерот е прифатен!", Toast.LENGTH_SHORT).show();
                            databaseReference.child("status").setValue("Закажана");
                            startActivity(new Intent(getApplicationContext(),Osnovno_povozrasen.class));
                        }
                    });
                    odbij.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Pregled_na_aktivnost.this,"Волонтерот не е прифатен!", Toast.LENGTH_SHORT).show();
                            databaseReference.child("status").setValue("Активна");
                            startActivity(new Intent(getApplicationContext(),Osnovno_povozrasen.class));
                        }
                    });
                }
                if(value.getStatus().equals("Завршена") && value.getIzvestaj()!=3  && value.getIzvestaj()!=1){
                    izvestaj.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    izvestaj.setVisibility(View.VISIBLE);
                    izvestaj.setText("Поднеси Извештај");
                    izvestaj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getApplicationContext(),Izvestaj_aktivnost.class);
                            String k=kluc+"/"+"user";
                            intent.putExtra("zaAktivnost",k);
                            startActivity(intent);
                        }
                    });
                }
                if(value.getStatus().equals("Завршена") && value.getIzvestaj()==3){
                    izvestaj.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pregled_na_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}