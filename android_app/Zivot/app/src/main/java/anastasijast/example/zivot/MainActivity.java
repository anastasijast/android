package anastasijast.example.zivot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button reg,naj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg=(Button) findViewById(R.id.registracija);
        naj=(Button) findViewById(R.id.najava);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistracija();
            }
        });
        naj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNajava();
            }
        });
    }
    public void openRegistracija(){
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void openNajava(){
        Intent intent=new Intent(this,SignIn.class);
        startActivity(intent);
    }
}