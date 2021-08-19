package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

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

    public Prontuario(int codigo, String nome, Date dataNascimento, char sexo, String anotacoes) {
        this.codigo = codigo;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.anotacoes = anotacoes;
    }
    
    @Override
    public String toString() {
        return "\nCódigo: " + codigo +
               "\nNome: " + nome +
               "\nIdade: " + idade +
               "\nSalario: " + salario;
    }
    
    public byte[] getByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutputStream dos = new DataOutputStream(baos);
        
        try {
            dos.writeInt(codigo);
            dos.writeUTF(nome);
            dos.writeByte(idade);
            dos.writeFloat(salario);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public void setByteArray(byte [] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);

        try {
            codigo = dis.readInt();
            nome = dis.readUTF();
            idade = dis.readByte();
            salario = dis.readFloat();
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
}