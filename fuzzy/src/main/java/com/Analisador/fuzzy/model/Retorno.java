package com.Analisador.fuzzy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Retorno {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String mensagemDoUsuario;
    String sentimento;

    public Retorno() {
    }

    public Retorno(long id, String mensagemDoUsuario, String sentimento) {
        this.id = id;
        this.mensagemDoUsuario = mensagemDoUsuario;
        this.sentimento = sentimento;
    }

    public String getSentimento() {
        return sentimento;
    }

    public void setSentimento(String sentimento) {
        this.sentimento = sentimento;
    }

    public Retorno(String mensagemDoUsuario, String sentimento) {
        this.mensagemDoUsuario = mensagemDoUsuario;
        this.sentimento = sentimento;
    }

    public String getMensagemDoUsuario() {
        return mensagemDoUsuario;
    }

    public void setMensagemDoUsuario(String mensagemDoUsuario) {
        this.mensagemDoUsuario = mensagemDoUsuario;
    }
}
