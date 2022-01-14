package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Izvestaj_aktivnost extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    String date,kluc,postaro,volonter,doKoj,userID,volonterID;
    TextView odKogo,doKogo;
    String[] niza;
    Button kopce;
    EditText opis;
    RatingBar ratingBar;
    Integer izv;
    private DatabaseReference databaseReference,dbUser,dbVol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izvestaj_aktivnost);
        opis=findViewById(R.id.komentar);
        kluc=getIntent().getStringExtra("zaAktivnost");
        kopce=findViewById(R.id.podnesi);
        odKogo=findViewById(R.id.od);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        doKogo=findViewById(R.id.doKogo);
        calendar=Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        date = dateFormat.format(calendar.getTime());
        niza=kluc.split("/");
        kluc=niza[0];
        databaseReference = FirebaseDatabase.getInstance().getReference("activities/"+kluc);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baranja value=snapshot.getValue(Baranja.class);
                dbUser=FirebaseDatabase.getInstance().getReference("users/"+value.getUser());
                userID=value.getUser();
                izv=value.getIzvestaj();
                dbVol=FirebaseDatabase.getInstance().getReference("users/"+value.getVolonter());
                volonterID=value.getVolonter();
                if(niza[1].equals("user")){
                    dbUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            postaro=user.getName();
                            String source="<b>Постаро лице: </b>"+user.getName();
                            odKogo.setText(Html.fromHtml(source));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    dbVol.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            volonter=user.getName();
                            doKoj="Волонтер";
                            String source="<b>Волонтер: </b>"+user.getName();
                            doKogo.setText(Html.fromHtml(source));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                else{
                    dbUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            postaro=user.getName();
                            doKoj="Постар";
                            String source="<b>Постаро лице: </b>"+user.getName();
                            doKogo.setText(Html.fromHtml(source));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    dbVol.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            volonter=user.getName();
                            String source="<b>Волонтер: </b>"+user.getName();
                            odKogo.setText(Html.fromHtml(source));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
                kopce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String opisA=opis.getText().toString();
                        Float rejting = ratingBar.getRating();
                        Izvestaj izvestaj=new Izvestaj(userID,volonterID,opisA,rejting,date,doKoj);
                        FirebaseDatabase.getInstance().getReference("rating").child(UUID.randomUUID().toString()).setValue(izvestaj).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if(niza[1].equals("user")){
                                        if(izv==0){
                                            databaseReference.child("izvestaj").setValue(1);
                                        }
                                        else if(izv==2){
                                            databaseReference.child("izvestaj").setValue(3);
                                        }
                                        Toast.makeText(Izvestaj_aktivnost.this,"Успешнo испратено!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Osnovno_povozrasen.class));
                                    }
                                    else{
                                        if(izv==0){
                                            databaseReference.child("izvestaj").setValue(2);
                                        }
                                        else if(izv==1){
                                            databaseReference.child("izvestaj").setValue(3);
                                        }
                                        Toast.makeText(Izvestaj_aktivnost.this,"Успешнo испратено!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),Osnovno_volonter.class));
                                    }
                                }
                                else {
                                    Toast.makeText(Izvestaj_aktivnost.this,"Неуспешно испраќање!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}