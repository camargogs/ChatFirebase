package br.ufc.quixada.trabmobile;

public interface ActivityCallback {
    void abrirCreateAccount();
    void abrirPerfil();
    void abrirChat(String sala);
    void abrirPrincipal();
    void abrirMapa();
    void abrirCriarSalaFragment();
    void logout();
}
