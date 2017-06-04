package br.com.magicbox.soscasa;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;

public class ProfissionalActivity extends BaseLocationActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Circle circle;
    private List<String> problemasEnvolvidosUid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissional);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getDatabase().child("negociacoes")
                .orderByChild("profissional").equalTo(getSessao().getUsuarioUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    problemasEnvolvidosUid.add(data.getValue(Negociacao.class).getProblemaUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_profissional, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profissional_logout:
                logout();
                return true;
            case R.id.action_profissional_negociacoes:
                Intent intent = new Intent(ProfissionalActivity.this, MinhasNegociacoesActivity.class);
                intent.putExtra("sessao", getSessao());
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_profissional_profile:
                editProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);


        getDatabase().child("problemas")
                .orderByChild("area").equalTo(getSessao().getUsuario().getAreaUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMap.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Problema problema = data.getValue(Problema.class);
                            problema.setUid(data.getKey());

                            if (StatusProblema.SOLICITADO.equals(problema.getStatus()) && !problemasEnvolvidosUid.contains(problema.getUid())) {
                                LatLng mark = new LatLng(problema.getLatitude(), problema.getLongitude());
                                MarkerOptions marker = new MarkerOptions().position(mark).title(problema.getDescricao());
                                mMap.addMarker(marker).setTag(problema.getUid());
                            }
                        }

                        circle = mMap.addCircle(new CircleOptions()
                                .center(getLatLang())
                                .radius(600)
                                .strokeColor(Color.argb(180, 51, 51, 78))
                                .strokeWidth(5).fillColor(Color.argb(60, 51, 51, 78)));
                        circle.isVisible();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLang()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLang()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            circle.setCenter(getLatLang());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, ProblemaProfissionalActivity.class);
        intent.putExtra("problemaUid", (Serializable) marker.getTag());
        intent.putExtra("sessao", getSessao());
        startActivity(intent);

        return false;
    }

}
