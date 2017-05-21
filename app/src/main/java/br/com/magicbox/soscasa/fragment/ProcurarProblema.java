package br.com.magicbox.soscasa.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import br.com.magicbox.soscasa.BaseActivity;
import br.com.magicbox.soscasa.ClienteActivity;
import br.com.magicbox.soscasa.ProblemaActivity;
import br.com.magicbox.soscasa.ProblemaProfissionalActivity;
import br.com.magicbox.soscasa.ProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.Profissional;
import br.com.magicbox.soscasa.models.StatusProblema;


public class ProcurarProblema extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ProfissionalActivity activity;

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ProfissionalActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_procurar_problema, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);


        //
        activity.getDatabase().child("problemas")
                //.equalTo(activity.getUsuario().getAreaUid(),"area")
                //.orderByChild("status").equalTo(StatusProblema.SOLICITADO.toString())

                .orderByChild("area").equalTo(activity.getUsuario().getAreaUid())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problema problema = dataSnapshot.getValue(Problema.class);
                problema.setUid(dataSnapshot.getKey());

                if(StatusProblema.SOLICITADO.equals(problema.getStatus())){
                    LatLng mark = new LatLng(problema.getLatitude(), problema.getLongitude());
                    MarkerOptions marker = new MarkerOptions().position(mark).title(problema.getDescricao());
                    mMap.addMarker(marker).setTag(problema);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getActivity(),"change", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(),"removed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getActivity(),"moved", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"canceled", Toast.LENGTH_LONG).show();
            }
        });


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng atual = new LatLng(activity.latitude, activity.longitude);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(atual));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(getActivity(), ProblemaProfissionalActivity.class);
        intent.putExtra("problema", (Serializable) marker.getTag());
        startActivity(intent);

        return false;
    }

}
