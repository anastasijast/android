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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detali_aktivnost extends AppCompatActivity {
    TextView ime,opis,vreme,lok,rej,povt;
    String kluc;
    float pr,brojac;
    static Float rez;
    private DatabaseReference databaseReference,dbRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_detali_aktivnost);
        rej=findViewById(R.id.rejting);
        ime=findViewById(R.id.imeAktivnost);
        rez=(float)0.0;
        pr=(float)0.0;
        brojac=(float)0.0;
        opis=findViewById(R.id.opisAktivnost);
        vreme=findViewById(R.id.vremeAktivnost);
        lok=findViewById(R.id.lokacijaAktivnost);
        povt=findViewById(R.id.povtorlivostAktivnost);
        LinearLayout layout=findViewById(R.id.baranja_L);
        kluc=getIntent().getStringExtra("baranje");
        dbRating=FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("activities/"+kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baranja value = snapshot.getValue(Baranja.class);
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
                        ime.setText("Име на активност: "+value.getIme());
                        opis.setText("Опис: \n"+value.getOpis());
                        vreme.setText("Датум и време за извршување: \n"+value.getDatum_izvrsuvanje()+" "+value.getVreme());
                        lok.setText("Локација: \n"+value.getLokacija());
                        povt.setText(value.getPovtorlivost());
                        rej.setText(rez.toString());
                        if(value.getStatus().equals("Активна")){
                            Button btn=findViewById(R.id.prifatiAktivnost);
                            btn.setVisibility(View.VISIBLE);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(Detali_aktivnost.this,"Активноста е прифатена!", Toast.LENGTH_SHORT).show();
                                    btn.setVisibility(View.INVISIBLE);
                                    databaseReference.child("volonter").setValue(user.getUid());
                                    databaseReference.child("status").setValue("Пријавен Волонтер");
                                    startActivity(new Intent(getApplicationContext(),Osnovno_volonter.class));
                                }
                            });
                        }
                        else{
                            TextView zak=new TextView(getApplicationContext());
                            zak.setTextColor(Color.parseColor("#ff4500"));
                            zak.setTextSize(18);
                            zak.setText("Активноста не е достапна за закажување.");
                            layout.addView(zak);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Detali_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Detali_aktivnost.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}