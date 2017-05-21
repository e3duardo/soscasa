package br.com.magicbox.soscasa.fragment;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

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
                .orderByChild("profissional").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                problemasEnvolvidosUid.add(dataSnapshot.getValue(Negociacao.class).getProblemaUid());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

        activity.getDatabase().child("problemas")
                //.equalTo(activity.getUsuario().getAreaUid(),"area")
                //.orderByChild("status").equalTo(StatusProblema.SOLICITADO.toString())

                .orderByChild("area").equalTo(activity.getUsuario().getAreaUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Problema problema = dataSnapshot.getValue(Problema.class);
                        problema.setUid(dataSnapshot.getKey());

                        if (StatusProblema.SOLICITADO.equals(problema.getStatus()) && !problemasEnvolvidosUid.contains(problema.getUid())) {
                            LatLng mark = new LatLng(problema.getLatitude(), problema.getLongitude());
                            MarkerOptions marker = new MarkerOptions().position(mark).title(problema.getDescricao());
                            mMap.addMarker(marker).setTag(problema);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Toast.makeText(getActivity(), "change", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Toast.makeText(getActivity(), "removed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Toast.makeText(getActivity(), "moved", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "canceled", Toast.LENGTH_LONG).show();
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
        startActivity(intent);

        return false;
    }

}
