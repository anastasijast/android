package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class Itni_baranja extends AppCompatActivity {
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itni_baranja);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        LinearLayout layout=findViewById(R.id.baranja_L);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("activities");
                Iterable<DataSnapshot> children=snapshot1.getChildren();
                for(DataSnapshot ds : children) {
                    if(ds.child("itnost").exists() && ds.child("status").getValue(String.class).equals("Активна")) {
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
                                "<br><b>"+ds.child("status").getValue(String.class)+"</b>"+
                                "<br><b>Креирање: "+ds.child("data").getValue(String.class)+"</b>";
                        text.setText(Html.fromHtml(sourceString));
                        nov.addView(text);
                        nov.addView(button);
                        layout.addView(nov);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(),Detali_aktivnost.class);
                                intent.putExtra("baranje",ds.getKey());
                                startActivity(intent);
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Itni_baranja.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}