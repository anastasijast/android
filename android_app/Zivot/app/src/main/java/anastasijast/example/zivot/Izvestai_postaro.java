package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Izvestai_postaro extends AppCompatActivity {
    private DatabaseReference databaseReference,dbUser;
    String ime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izvestai_postaro);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayout layout=findViewById(R.id.izvestai_L);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("rating");
                Iterable<DataSnapshot> children=snapshot1.getChildren();
                for(DataSnapshot ds : children) {
                    if(ds.child("doKogo").getValue(String.class).equals("Постар") && user.equals(ds.child("userID").getValue(String.class))){
                        dbUser=FirebaseDatabase.getInstance().getReference("users/"+ds.child("volonterID").getValue(String.class));
                        dbUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user=snapshot.getValue(User.class);
                                ime=user.getName();
                                Drawable drawable=getDrawable(R.drawable.edit_border);
                                LinearLayout nov=new LinearLayout(getApplicationContext());
                                nov.setBackground(drawable);
                                nov.setOrientation(LinearLayout.VERTICAL);
                                nov.setPadding(10,30,10,50);
                                TextView text=new TextView(getApplicationContext());
                                text.setTextColor(Color.parseColor("#FFFFFF"));
                                text.setTextSize(18);
                                text.setPadding(50,20,20,40);
                                String sourceString = "<b>Име: </b>"+ime+
                                        "<br> <b>Рејтинг: </b>"+ds.child("rejting").getValue(Float.class).toString()+
                                        "<br><b>"+ds.child("opis").getValue(String.class)+ "</b>"+
                                        "<br><b>"+ds.child("datum").getValue(String.class)+"</b>";
                                text.setText(Html.fromHtml(sourceString));
                                nov.addView(text);
                                layout.addView(nov);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Izvestai_postaro.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Izvestai_postaro.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}