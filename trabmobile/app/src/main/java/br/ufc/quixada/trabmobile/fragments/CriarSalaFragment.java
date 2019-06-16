package br.ufc.quixada.trabmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.ufc.quixada.trabmobile.ActivityCallback;
import br.ufc.quixada.trabmobile.R;
import br.ufc.quixada.trabmobile.models.Sala;
import br.ufc.quixada.trabmobile.utils.Constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CriarSalaFragment extends Fragment {
    private EditText nomeSala;
    private ActivityCallback mCallback;

    public static CriarSalaFragment newInstance() {
        return new CriarSalaFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_criar_sala, container, false);
        setHasOptionsMenu(true);
        nomeSala = (EditText) root.findViewById(R.id.criarsala_nomeSala);
        Button createButton = (Button) root.findViewById(R.id.criarsala_botao);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarSala();
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_perfil).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_voltar) {
            mCallback.abrirPrincipal();
        }

        return super.onOptionsItemSelected(item);
    }

    private void criarSala() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference(Constants.DATABASE_NAME);
        Sala sala = new Sala(nomeSala.getText().toString(), "5");
        mReference.child(sala.getNome()).setValue(sala);
        nomeSala.setText("");
        mCallback.abrirPrincipal();
    }
}