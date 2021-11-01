package model;

import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public Prontuario(byte[] v) {
        readFromByteArray(v);
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
        if (codigo >= 0) {
            return "\nCódigo: " + codigo + "\nNome: " + nome + "\nData de Nascimento: " + dFormat.format(dataNascimento)
                    + "\nSexo: " + sexo + "\nAnotações: " + anotacoes;
        } else {
            return null;
        }
    }

    public byte[] toByteArray(int registerSize, boolean delete, int nextToDelete) throws IndexOutOfBoundsException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeChar(delete ? '*' : '\0');
            // caso o registro estiver marcado para deleção, escreve a posição do próximo a
            // deletar no lugar do seu ID
            dos.writeInt(delete ? nextToDelete : this.getCodigo());
            dos.writeUTF(this.getNome());
            dos.writeLong(this.getDataNascimento().getTime());
            dos.writeChar(this.getSexo());
            dos.writeUTF(this.getAnotacoes());
            // dos.writeUTF(
            // prontuario.getAnotacoes().substring(0,
            // prontuario.getAnotacoes().length() > registerSize - baos.toByteArray().length
            // -2 ?
            // registerSize - baos.toByteArray().length -2 :
            // prontuario.getAnotacoes().length()
            // )
            // );
            if (baos.toByteArray().length <= registerSize) {
                byte[] tailLength = new byte[registerSize - baos.toByteArray().length];
                dos.write(tailLength);
            } else {
                throw new IndexOutOfBoundsException("Prontuário excede limite de tamanho.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public byte[] toByteArray(int registerSize, boolean delete) throws IndexOutOfBoundsException {
        return toByteArray(registerSize, delete, -1);
    }

    public byte[] toByteArray(int registerSize) throws IndexOutOfBoundsException {
        return toByteArray(registerSize, false);
    }

    public void readFromByteArray(byte[] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);

        try {
            if (dis.readChar() != '*') {
                this.setCodigo(dis.readInt());
                this.setNome(dis.readUTF());
                Date date = new Date(dis.readLong());
                this.setDataNascimento(date);
                this.setSexo(dis.readChar());
                this.setAnotacoes(dis.readUTF());
            } else {
                this.setCodigo(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}