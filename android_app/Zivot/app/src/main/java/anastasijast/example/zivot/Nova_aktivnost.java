package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Nova_aktivnost extends AppCompatActivity{
    private Calendar calendar;
    final Calendar myCalendar= Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    private String data;
    Button mom,nova,isprati;
    TextView lok,ime,opis,vreme_od,vreme_do,datumIzv;
    RadioButton itno,neitno,ednas,poveke;
    String ime_B, opis_B,vreme_B,povtorlivost_B,itnost_B,lokacija_B,datum;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_aktivnost);
        calendar=Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        data = dateFormat.format(calendar.getTime());
        datumIzv=findViewById(R.id.datumIzv);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("activities");
        ime=findViewById(R.id.ime_a);
        opis=findViewById(R.id.opis_a);
        vreme_od=findViewById(R.id.vreme_od);
        vreme_do=findViewById(R.id.vreme_do);
        itno=findViewById(R.id.itno);
        neitno=findViewById(R.id.neItno);
        ednas=findViewById(R.id.ednok);
        poveke=findViewById(R.id.povt);
        lok = findViewById(R.id.lokacija);
        mom = findViewById(R.id.mom_lok);
        nova = findViewById(R.id.nova_lok);
        isprati=findViewById(R.id.gotova_a);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="MM-dd-yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
                datumIzv.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        datumIzv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Nova_aktivnost.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Nova_aktivnost.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {
                                try {
                                    Geocoder geocoder = new Geocoder(Nova_aktivnost.this,
                                            Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);
                                    lok.setText(Html.fromHtml(
                                            addresses.get(0).getAddressLine(0)
                                    ));


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(Nova_aktivnost.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        nova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
        isprati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
                ime_B=ime.getText().toString();
                opis_B=opis.getText().toString();
                vreme_B=vreme_od.getText().toString()+" - "+vreme_do.getText().toString();
                datum=datumIzv.getText().toString();
                if(poveke.isChecked()){ povtorlivost_B="Повторлива Активност"; }
                else{ povtorlivost_B="Еднократна Активност"; }
                if(itno.isChecked()){ itnost_B="Итна Активност";}
                lokacija_B=lok.getText().toString();
                String status_B="Активна";
                Baranja baranje=new Baranja(user,ime_B,opis_B,vreme_B,povtorlivost_B,itnost_B,lokacija_B,status_B,"",data,datum,0);
                FirebaseDatabase.getInstance().getReference("activities").child(UUID.randomUUID().toString()).setValue(baranje).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Nova_aktivnost.this,"Успешнo испратено!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Osnovno_povozrasen.class));
                        }
                        else {
                            Toast.makeText(Nova_aktivnost.this,"Неуспешно испраќање!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void onResume(){
        super.onResume();
        lok.setText(getIntent().getStringExtra("mytext"));

    }
}