package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Prontuario {

    protected int codigo;
    protected String nome;
    protected Date dataNascimento;
    protected char sexo;
    protected String anotacoes;


    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(long dataNascimento) {
        this.dataNascimento = new Date(dataNascimento);
    }

    public char getSexo() {
        return this.sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getAnotacoes() {
        return this.anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }
    
    public Prontuario() {
        this.codigo = -1;
        this.nome = "";
        this.dataNascimento = new Date(0);
        this.sexo = '\0';
        this.anotacoes = "";
    }
    
    public Prontuario(int codigo) {
        this.codigo = codigo;
        this.nome = "";
        this.dataNascimento = new Date(0);
        this.sexo = '\0';
        this.anotacoes = "";
    }

    public Prontuario(int codigo, String nome, Date dataNascimento, char sexo, String anotacoes) {
        this.codigo = codigo;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.anotacoes = anotacoes;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        return "\nCódigo: " + codigo +
               "\nNome: " + nome +
               "\nData de Nascimento: " + dFormat.format(dataNascimento) +
               "\nSexo: " + sexo +
               "\nAnotações: " + anotacoes;
    }
}