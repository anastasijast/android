package anastasijast.example.zivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Aktivni_baranja extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    TextView tv;
    FusedLocationProviderClient fusedLocationProviderClient;
    public String strAddress;
    LatLng volonter,postar;
    DatabaseReference databaseReference,dbUser;
    ArrayList<Pomosna> niza;
    Location locationA,locationB;
    float distance;
    Pomosna dalecina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volonter=new LatLng(0,0);
        dalecina=new Pomosna();
        postar=new LatLng(0,0);
        strAddress=new String();
        locationA = new Location("Volonter");
        locationB = new Location("Postar");
        setContentView(R.layout.activity_aktivni_baranja);
        niza=new ArrayList<Pomosna>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase=FirebaseDatabase.getInstance();
        LinearLayout layout=findViewById(R.id.baranja_L);
        tv=findViewById(R.id.lokacijaV);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Aktivni_baranja.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
            Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(Aktivni_baranja.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);
                            strAddress=addresses.get(0).getAddressLine(0);
                            tv.setText(Html.fromHtml(
                                    strAddress
                            ));
                            volonter=getLocationFromAddress(getApplicationContext(),strAddress);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    DataSnapshot snapshot1=snapshot.child("activities");
                                    Iterable<DataSnapshot> children=snapshot1.getChildren();
                                    for(DataSnapshot ds : children) {
                                        postar=getLocationFromAddress(getApplicationContext(),ds.child("lokacija").getValue(String.class));
                                        locationA.setLatitude(volonter.latitude);
                                        locationA.setLongitude(volonter.longitude);
                                        locationB.setLatitude(postar.latitude);
                                        locationB.setLongitude(postar.longitude);
                                        distance =  locationA.distanceTo(locationB);
                                        dalecina=new Pomosna(ds.getKey(),distance);
                                        niza.add(dalecina);
                                    }
                                    bubbleSort(niza);
                                    for(int i=0;i<niza.size();i++){
                                        dbUser = FirebaseDatabase.getInstance().getReference("activities/" + niza.get(i).getUserID());
                                        int finalI = i;
                                        dbUser.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Baranja baranje = snapshot.getValue(Baranja.class);
                                                if (baranje.getStatus().equals("Активна")) {
                                                    Drawable drawable = getDrawable(R.drawable.edit_border);
                                                    LinearLayout nov = new LinearLayout(getApplicationContext());
                                                    nov.setBackground(drawable);
                                                    nov.setOrientation(LinearLayout.VERTICAL);
                                                    nov.setPadding(10, 30, 10, 50);
                                                    TextView text = new TextView(getApplicationContext());
                                                    Button button = new Button(getApplicationContext());
                                                    button.setText("Повеќе");
                                                    button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                                    text.setTextColor(Color.parseColor("#FFFFFF"));
                                                    text.setTextSize(18);
                                                    text.setPadding(50, 20, 20, 40);
                                                    String sourceString = "<br> <b>Активност: </b>" + baranje.getIme() +
                                                            "<br> <b>Локација: </b> <br>" + baranje.getLokacija() +
                                                            "<br><b>" + baranje.getStatus() + "</b>" + "<br><b>Креирање: " + baranje.getData() + "</b>"
                                                            + "<br><b>≈ " + niza.get(finalI).getDistance() / 1000 + "km</b>";
                                                    text.setText(Html.fromHtml(sourceString));
                                                    nov.addView(text);
                                                    nov.addView(button);
                                                    layout.addView(nov);
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getApplicationContext(), Detali_aktivnost.class);
                                                            intent.putExtra("baranje", snapshot.getKey());
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(Aktivni_baranja.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Aktivni_baranja.this, "Неуспешно превземање!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(Aktivni_baranja.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
    public static void bubbleSort(ArrayList<Pomosna> array) {
        boolean sorted = false;
        Pomosna temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < array.size() - 1; i++) {
                if (array.get(i).getDistance() > array.get(i+1).getDistance()) {
                    temp = array.get(i);
                    array.set(i,array.get(i+1));
                    array.set(i+1,temp);
                    sorted = false;
                }
            }
        }
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
