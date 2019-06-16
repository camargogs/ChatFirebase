package br.ufc.quixada.trabmobile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import br.ufc.quixada.trabmobile.fragments.ChatFragment;
import br.ufc.quixada.trabmobile.fragments.CreateAccountFragment;
import br.ufc.quixada.trabmobile.fragments.CriarSalaFragment;
import br.ufc.quixada.trabmobile.fragments.LoginFragment;
import br.ufc.quixada.trabmobile.fragments.PrincipalFragment;
import br.ufc.quixada.trabmobile.fragments.UsuarioFragment;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, LoginFragment.newInstance())
                .commit();
    }


    @Override
    public void abrirCreateAccount() {
        replaceFragment(CreateAccountFragment.newInstance());
    }

    @Override
    public void abrirPerfil() { replaceFragment(UsuarioFragment.newInstance()); }

    @Override
    public void abrirChat(String sala) {
        Bundle b = new Bundle();
        b.putString("sala", sala);
        ChatFragment chatSala = new ChatFragment();
        chatSala.setArguments(b);
        replaceFragment(chatSala);
    }

    @Override
    public void abrirPrincipal() { replaceFragment(PrincipalFragment.newInstance()); }

    @Override
    public void abrirMapa() { startActivity(new Intent(this, Mapa.class)); }
                            /*Fragment.instantiate(this, Mapa.class.getName());*/

    @Override
    public void abrirCriarSalaFragment() { replaceFragment(CriarSalaFragment.newInstance()); }

    @Override
    public void logout() {
        replaceFragment(LoginFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}