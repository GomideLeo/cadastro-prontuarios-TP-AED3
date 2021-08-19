package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pessoa {
    protected int codigo;
    protected String nome;
    protected byte idade;
    protected float salario;
    
    public Pessoa() {
        codigo = -1;
        nome = "";
        idade = (byte)0;
        salario = 0f;
    }

    public Pessoa(int c, String n, byte i, float s) {
        codigo = c;
        nome = n;
        idade = i;
        salario = s;
    }

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