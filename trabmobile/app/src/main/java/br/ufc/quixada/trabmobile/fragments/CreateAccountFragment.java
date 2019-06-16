package br.ufc.quixada.trabmobile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import br.ufc.quixada.trabmobile.ActivityCallback;
import br.ufc.quixada.trabmobile.R;
import br.ufc.quixada.trabmobile.models.Usuario;
import br.ufc.quixada.trabmobile.utils.Constants;
import br.ufc.quixada.trabmobile.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountFragment extends Fragment {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private View mProgressView;
    private View mCreateForm;

    private ActivityCallback mCallback;
    private FirebaseAuth mAuth;
    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_create_account, container, false);

        mUsername = (EditText) root.findViewById(R.id.create_account_username);
        mPassword = (EditText) root.findViewById(R.id.create_account_password);
        mEmail = (EditText) root.findViewById(R.id.create_account_email);

        mCreateForm = root.findViewById(R.id.create_account_form);
        mProgressView = root.findViewById(R.id.create_account_progress);

        Button createButton = (Button) root.findViewById(R.id.create_account_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        mAuth = FirebaseAuth.getInstance();

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
            mCallback.logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private DatabaseReference mReference;
    private DatabaseReference uReference;

    private void createAccount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference(Constants.DATABASE_NAME);
        showProgress(true);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()) {
                    Toast.makeText(getContext(), R.string.error_create_account, Toast.LENGTH_SHORT).show();
                } else {
                    String x = task.getResult().getUser().getUid();
                    Utils.saveLocalUser(getContext(),
                            mUsername.getText().toString(),
                            mEmail.getText().toString(),
                            x);
                    Usuario u = new Usuario();
                    u.setId(x);
                    u.setNome(mUsername.getText().toString());
                    u.setEmail(mEmail.getText().toString());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    uReference = database.getReference("usuario");
                    uReference.child(x).setValue(u);
                    mCallback.logout();
                }

                showProgress(false);
                Utils.closeKeyboard(getContext(), mEmail);
            }
        });
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mCreateForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
