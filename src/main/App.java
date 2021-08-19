package main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import model.*;

public class App {
    public static void main(String[] args) {

        Pessoa p1 = new Pessoa(1, "Ana Maria", (byte) 22, 1500.0f);
        Pessoa p2 = new Pessoa(2, "João Paulo", (byte) 21, 1200.0f);
        Pessoa p3 = new Pessoa(3, "Teresa Cristina", (byte) 25, 1700.0f);

        // System.out.println(p1);
        // System.out.println(p2);
        // System.out.println(p3);

        try {
            
            FileOutputStream fos = new FileOutputStream("dados/pessoa.db");
            DataOutputStream dos = new DataOutputStream(fos);
            byte[] vet;

            vet = p1.getByteArray();
            dos.writeInt(vet.length);
            dos.write(vet);

            vet = p2.getByteArray();
            dos.writeInt(vet.length);
            dos.write(vet);

            vet = p3.getByteArray();
            dos.writeInt(vet.length);
            dos.write(vet);

            dos.close();
            fos.close();

            FileInputStream fis = new FileInputStream("dados/pessoa.db");
            DataInputStream dis = new DataInputStream(fis);

            Pessoa p4 = new Pessoa();
            Pessoa p5 = new Pessoa();
            Pessoa p6 = new Pessoa();

            int tam = dis.readInt();
            byte[] vet_lido = new byte[tam];
            dis.read(vet_lido);
            p4.setByteArray(vet_lido);

            tam = dis.readInt();
            vet_lido = new byte[tam];
            dis.read(vet_lido);
            p5.setByteArray(vet_lido);

            tam = dis.readInt();
            vet_lido = new byte[tam];
            dis.read(vet_lido);
            p6.setByteArray(vet_lido);

            System.out.println("Dados lidos:");
            System.out.println(p4);
            System.out.println(p5);
            System.out.println(p6);

            dis.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
