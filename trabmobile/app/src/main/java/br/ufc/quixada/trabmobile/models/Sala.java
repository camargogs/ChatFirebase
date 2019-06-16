package br.ufc.quixada.trabmobile.models;

import java.util.ArrayList;

public class Sala {
    String nome;
    String numUsuarios;
    ArrayList<ChatData> chatData;

    public Sala() {
    }

    public Sala(String nome, String numUsuarios) {
        this.nome = nome;
        this.numUsuarios = numUsuarios;
        this.chatData = new ArrayList<ChatData>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(String numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public ArrayList<ChatData> getChatData() { return chatData; }

    public void setChatData(ArrayList<ChatData> chatData) { this.chatData = chatData; }
}
