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

import org.w3c.dom.Text;

public class Osnovno_povozrasen extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dbRating;
    Button btn,izv,nova,site_ak;
    TextView text;
    Float pr,brojac;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osnovno_povozrasen);
        izv=findViewById(R.id.izvestai);
        nova=(Button)findViewById(R.id.nova);
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth =  FirebaseAuth.getInstance();
        FirebaseUser user_new = mAuth.getCurrentUser();
        site_ak=findViewById(R.id.site);
        userID=user_new.getUid();
        dbRating=firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference("users/"+user_new.getUid());
        btn=(Button) findViewById(R.id.odjavi_se);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        nova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Nova_aktivnost.class));
            }
        });
        izv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Izvestai_postaro.class));
            }
        });
        site_ak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Pregled_aktivnosti.class));
            }
        });
        text=findViewById(R.id.profil);
    }
    @Override
    public void onResume() {
        super.onResume();
        pr=(float) 0.0;
        brojac=(float) 0.0;
        FirebaseAuth mAuth =  FirebaseAuth.getInstance();
        FirebaseUser user_new = mAuth.getCurrentUser();
        userID=user_new.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users/"+user_new.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value=snapshot.getValue(User.class);
                dbRating.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        DataSnapshot snapshot2 = snapshot1.child("rating");
                        Iterable<DataSnapshot> children = snapshot2.getChildren();
                        for (DataSnapshot ds : children) {
                            if (ds.child("doKogo").getValue(String.class).equals("Постар") && userID.equals(ds.child("userID").getValue(String.class))) {
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
                        String source="<b> Име: </b>"+value.getName()+"<br><b>Категорија:</b> "+value.getKategorija()+
                                "<br><b>Телефонски број: </b>"+value.getBroj()+"<br><b>Рејтинг: </b>"+pr.toString();
                        text.setText(Html.fromHtml(source));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Osnovno_povozrasen.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Osnovno_povozrasen.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}