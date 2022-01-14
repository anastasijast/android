package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pregled_aktivnosti extends AppCompatActivity {
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregled_aktivnosti);
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayout layout=findViewById(R.id.baranja_L);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("activities");
                Iterable<DataSnapshot> children=snapshot1.getChildren();
                for(DataSnapshot ds : children) {
                    if(user.equals(ds.child("user").getValue(String.class))){
                        Drawable drawable=getDrawable(R.drawable.edit_border);
                        LinearLayout nov=new LinearLayout(getApplicationContext());
                        nov.setBackground(drawable);
                        nov.setOrientation(LinearLayout.VERTICAL);
                        nov.setPadding(10,30,10,50);
                        TextView text=new TextView(getApplicationContext());
                        Button button=new Button(getApplicationContext());
                        button.setText("Повеќе");
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        text.setTextColor(Color.parseColor("#FFFFFF"));
                        text.setTextSize(18);
                        text.setPadding(50,20,20,40);
                        String sourceString = "<br> <b>Активност: </b>"+ds.child("ime").getValue(String.class)+
                                "<br> <b>Локација: </b> <br>"+ds.child("lokacija").getValue(String.class)+
                                "<br><b>"+ds.child("status").getValue(String.class)+"</b>"+"<br><b>Креирање: "+ds.child("data").getValue(String.class)+"</b>";
                        text.setText(Html.fromHtml(sourceString));
                        nov.addView(text);
                        nov.addView(button);
                        layout.addView(nov);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(),Pregled_na_aktivnost.class);
                                intent.putExtra("aktivnost",ds.getKey());
                                startActivity(intent);
                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pregled_aktivnosti.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}