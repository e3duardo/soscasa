package br.com.magicbox.soscasa.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.magicbox.soscasa.ClienteActivity;
import br.com.magicbox.soscasa.R;
import br.com.magicbox.soscasa.models.Area;
import br.com.magicbox.soscasa.models.Problema;
import br.com.magicbox.soscasa.models.StatusProblema;
import br.com.magicbox.soscasa.models.Usuario;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;


public class DefinirProblemaFragment extends Fragment  {

    private Spinner areaSpinner;
    private EditText problemaEditText;
    private Button okButton;

    private ClienteActivity activity;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager locationManager;
    String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (ClienteActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_definir_problema, container, false);

        areaSpinner = (Spinner) view.findViewById(R.id.areaSpinner);
        problemaEditText = (EditText) view.findViewById(R.id.problemaEditText);
        okButton = (Button) view.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewProblema(problemaEditText.getText().toString(), (Area) areaSpinner.getSelectedItem());
            }
        });

        activity.getDatabase().child("areas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Area> areas = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Area area = postSnapshot.getValue(Area.class);
                    area.setUid(postSnapshot.getKey());
                    areas.add(area);
                }

                ArrayAdapter<Area> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, areas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getAreas:onCancelled", databaseError.toException());
            }
        });

        return view;
    }

    private void writeNewProblema(String descricao, Area area) {
        String key = activity.getDatabase().child("problemas").push().getKey();

        Problema problema = new Problema();
        problema.setStatus(StatusProblema.SOLICITADO);
        problema.setDescricao(descricao);
        problema.setAreaUid(area.getUid());
        problema.setClienteUid(activity.getUsuario().getUid());
        problema.setLatitude(activity.latitude);
        problema.setLongitude(activity.longitude);

        activity.getDatabase().child("problemas").child(key).setValue(problema);

        Toast.makeText(getActivity(), "novo problema: " + problema.getDescricao() + " " + area.getNome(), Toast.LENGTH_SHORT).show();
    }


}
