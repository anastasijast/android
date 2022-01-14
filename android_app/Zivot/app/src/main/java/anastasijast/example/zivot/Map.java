package anastasijast.example.zivot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    Button btn;
    Marker marker;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
        btn=findViewById(R.id.kon_lok);
        tv=findViewById(R.id.lokacija);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap=googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String address = addresses.get(0).getAddressLine(0);
                        Intent intent=new Intent(getApplicationContext(),Nova_aktivnost.class);
                        intent.putExtra("mytext",address);
                        startActivity(intent);

                    }
                });
                gMap.clear();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                marker=gMap.addMarker(markerOptions);
            }
        });
    }
}