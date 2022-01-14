package anastasijast.example.zivot;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    User user;
    EditText email_r,password_r,broj_r,ime_r;
    Button kopce;
    String emailr,passwordr,brojr,imer,katr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        kopce=(Button)findViewById(R.id.kopce_reg);
        email_r = (EditText)findViewById(R.id.email_r);
        password_r=(EditText) findViewById(R.id.password_r);
        broj_r=(EditText) findViewById(R.id.broj_r);
        ime_r=(EditText) findViewById(R.id.ime_r);
        kopce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Najava();
            }
        });
    }
    private void Najava() {
        imer=ime_r.getText().toString();
        emailr = email_r.getText().toString();
        passwordr=password_r.getText().toString();
        brojr=broj_r.getText().toString();
        Spinner spinner=findViewById(R.id.spinner);
        katr= (String) spinner.getSelectedItem();
        mAuth.createUserWithEmailAndPassword(emailr,passwordr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = new User(imer, emailr, passwordr, brojr, katr);
                    FirebaseUser user_new = mAuth.getCurrentUser();
                    FirebaseDatabase.getInstance().getReference("users").child(user_new.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this,"Успешна регистрација!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            else {
                                Toast.makeText(SignUp.this,"Неуспешно!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUp.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}