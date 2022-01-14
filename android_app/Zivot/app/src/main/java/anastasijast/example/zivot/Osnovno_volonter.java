package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Osnovno_volonter extends AppCompatActivity {
    Button btn,siteA,svoiA,aktivniA,itniA,izvestai;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dbRating;
    TextView text;
    Float pr,brojac;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osnovno_volonter);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/"+user.getUid());
        dbRating=FirebaseDatabase.getInstance().getReference();
        itniA=findViewById(R.id.itni);
        izvestai=findViewById(R.id.izvestai);
        btn=(Button) findViewById(R.id.odjavi_se);
        text=findViewById(R.id.volonter);
        siteA=findViewById(R.id.site);
        svoiA=findViewById(R.id.svoi);
        aktivniA=findViewById(R.id.aktivni);
        itniA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Itni_baranja.class));
            }
        });
        izvestai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Izvestai_volonter.class));
            }
        });
        siteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Site_baranja.class));
            }
        });
        svoiA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Svoi_baranja.class));
            }
        });
        aktivniA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Aktivni_baranja.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        pr= (float)0.0;
        brojac=(float)0.0;
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/"+user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value = snapshot.getValue(User.class);
                dbRating.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        DataSnapshot snapshot2 = snapshot1.child("rating");
                        Iterable<DataSnapshot> children = snapshot2.getChildren();
                        for (DataSnapshot ds : children) {
                            if (ds.child("doKogo").getValue(String.class).equals("Волонтер") && userID.equals(ds.child("volonterID").getValue(String.class))) {
                                pr = pr + ds.child("rejting").getValue(Float.class);
                                brojac++;
                            }
                        }
                        if(pr==0 && brojac==0){
                            pr=(float)0.0;
                        }
                        else{
                            pr = pr / brojac;
                        }
                        String source = "<b> Име: </b>" + value.getName() + "<br><b>Категорија:</b> " + value.getKategorija() + "<br><b>Телефонски број: </b>" + value.getBroj() + "<br><b>Рејтинг: </b>" + pr.toString();
                        text.setText(Html.fromHtml(source));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Osnovno_volonter.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Osnovno_volonter.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}