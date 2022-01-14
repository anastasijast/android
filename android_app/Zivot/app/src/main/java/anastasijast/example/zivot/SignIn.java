package anastasijast.example.zivot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email_n,password_n;
    private DatabaseReference mDatabase;
    Button kopce;
    String passwordn,emailn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        kopce=(Button) findViewById(R.id.kopce_najava);
        email_n = findViewById(R.id.email_n);
        password_n=(EditText) findViewById(R.id.password_n);
        kopce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOsnovno();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User value=snapshot.getValue(User.class);
                    if(value.getKategorija().equals("Повозрасно лице")){
                        Intent intent=new Intent(getApplicationContext(),Osnovno_povozrasen.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent=new Intent(getApplicationContext(),Osnovno_volonter.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignIn.this, "Неуспешнo!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void openOsnovno() {
        passwordn=password_n.getText().toString();
        emailn = email_n.getText().toString();
        mAuth.signInWithEmailAndPassword(emailn, passwordn).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Успешно се најавивте!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User value=snapshot.getValue(User.class);
                            if(value.getKategorija().equals("Повозрасно лице")){
                                Intent intent=new Intent(getApplicationContext(),Osnovno_povozrasen.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent=new Intent(getApplicationContext(),Osnovno_volonter.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignIn.this, "Неуспешнo!", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else{
                    Toast.makeText(SignIn.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}