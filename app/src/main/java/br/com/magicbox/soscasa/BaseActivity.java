package br.com.magicbox.soscasa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.magicbox.soscasa.models.Usuario;

/**
 * Criado por eduardo em 5/20/17.
 */

public class BaseActivity extends AppCompatActivity implements LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public double latitude = -21.1947618;
    public double longitude = -41.9047604;

    private DatabaseReference mDatabase;
    private Usuario usuario;

    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                requestProvider();
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            }
        }
    }

    protected void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, EntrarActivity.class));
        finish();
    }

    protected void editProfile() {
        Intent intent = new Intent(BaseActivity.this, PerfilActivity.class);
        startActivityForResult(intent, 1);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        requestProvider();
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    Toast.makeText(this, R.string.sem_permissao_localizacao, Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void requestProvider() {
        if (provider == null) {
            provider = locationManager.getBestProvider(new Criteria(), false);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
