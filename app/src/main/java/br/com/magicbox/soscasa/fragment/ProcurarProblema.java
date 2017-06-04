package br.com.magicbox.soscasa.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import br.com.magicbox.soscasa.ProblemaProfissionalActivity;
import br.com.magicbox.soscasa.ProfissionalActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Negociacao;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;


public class ProcurarProblema extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ProfissionalActivity activity;
    private GoogleMap mMap;
    private List<String> problemasEnvolvidosUid = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_procurar_problema, container, false);

        activity = (ProfissionalActivity) getActivity();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        activity.getDatabase().child("negociacoes")
                .orderByChild("profissional").equalTo(activity.getSessao().getUsuarioUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(-33.87365, 151.20689))
                .radius(10000)
                .strokeColor(Color.BLUE)
                .fillColor(Color.BLUE));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(activity.latitude, activity.longitude), 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        Log.e("Circle Lat Long:", activity.latitude + ", " + activity.longitude);
        circle.remove();
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(activity.latitude, activity.longitude))
                .radius(600)
                .strokeColor(Color.argb(180, 51, 51, 78))
                .strokeWidth(5).fillColor(Color.argb(60, 51, 51, 78)));
        circle.isVisible();


        activity.getDatabase().child("problemas")
                .orderByChild("area").equalTo(activity.getSessao().getUsuario().getAreaUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Problema problema = data.getValue(Problema.class);
                    problema.setUid(data.getKey());

                    if (StatusProblema.SOLICITADO.equals(problema.getStatus()) && !problemasEnvolvidosUid.contains(problema.getUid())) {
                        LatLng mark = new LatLng(problema.getLatitude(), problema.getLongitude());
                        MarkerOptions marker = new MarkerOptions().position(mark).title(problema.getDescricao());
                        mMap.addMarker(marker).setTag(problema);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        LatLng atual = new LatLng(activity.latitude, activity.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(atual));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(getActivity(), ProblemaProfissionalActivity.class);
        intent.putExtra("problema", (Serializable) marker.getTag());
        intent.putExtra("sessao", activity.getSessao());
        startActivity(intent);

        return false;
    }

}
