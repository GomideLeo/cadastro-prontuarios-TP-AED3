package main;

import java.util.Date;
import java.util.Scanner;

import dao.*;
import model.*;
import manager.*;

public class App {
    static Scanner s = new Scanner(System.in);
    static ProntuarioDAO pdao = new ProntuarioDAO("dados/pessoa.db");
    
    public static void main(String[] args) {
        ManagerManager manager = new ManagerManager("dados/teste01", 2,3,200);
        manager.insertKey(0, 40);
        manager.insertKey(4, 50);
        manager.insertKey(12, 30);
        manager.insertKey(8, 20);
        // Bucket b = new Bucket(0, 0);
        // try {
        //     byte[] aux = manager.findRegister(10);
        //     // b.fromByteArray(aux);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // System.out.println(b);
        // try {
        //     int i = 0;
            
        //     do {
        //         System.out.println("#====================#\n" + "|        Menu        |\n" + "|  0. Exit           |\n"
        //                 + "|  1. Create         |\n" + "|  2. Read           |\n" + "|  3. Update         |\n"
        //                 + "|  4. Delete         |\n" + "|  5. Read N         |\n" + "|  6.  New File      |\n"
        //                 + "#====================#\n");

        //         i = s.nextInt();
        //         s.nextLine();

        //         switch (i) {
        //             case 1:
        //                 create();
        //                 break;
        //             case 2:
        //                 read();
        //                 break;
        //             case 3:
        //                 update();
        //                 break;
        //             case 4:
        //                 delete();
        //                 break;
        //             case 5:
        //                 readN();
        //                 break;
        //             case 6:
        //                 newFile();
        //                 break;
        //         }
        //     } while (i != 0);

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    static void create() throws Exception {

        Prontuario p = new Prontuario();

        System.out.println("Nome?");
        p.setNome(s.nextLine());

        System.out.println("Nascimento? YYYY-MM-DD");
        String[] nasc = s.nextLine().split("-");
        p.setDataNascimento(
                new Date(Integer.parseInt(nasc[0]) - 1900, Integer.parseInt(nasc[1]), Integer.parseInt(nasc[2])));

        System.out.println("Sexo?");
        p.setSexo(s.nextLine().charAt(0));

        System.out.println("Anotações?");
        p.setAnotacoes(s.nextLine());

        pdao.createProntuario(p);
    }

    static void read() {

        System.out.println("Codigo?");
        System.out.println(pdao.getProntuario(s.nextInt()));
        s.nextLine();
    }

    static void update() throws Exception {

        System.out.println("Codigo?");
        Prontuario p = new Prontuario(s.nextInt());
        s.nextLine();

        System.out.println("Nome?");
        p.setNome(s.nextLine());

        System.out.println("Nascimento? YYYY-MM-DD");
        String[] nasc = s.nextLine().split("-");
        p.setDataNascimento(
                new Date(Integer.parseInt(nasc[0]) - 1900, Integer.parseInt(nasc[1]), Integer.parseInt(nasc[2])));

        System.out.println("Sexo?");
        p.setSexo(s.nextLine().charAt(0));

        System.out.println("Anotações?");
        p.setAnotacoes(s.nextLine());

        System.out.println(pdao.updateProntuario(p));
    }

    static void delete() {
        System.out.println("Codigo?");
        System.out.println(pdao.deleteProntuario(s.nextInt()));
        s.nextLine();
    }
    
    static void readN() throws Exception {
        System.out.println("N?");
        
        for (Prontuario p : pdao.readNProntuarios(s.nextInt(), 0))
            System.out.println(p);
        s.nextLine();
    }

    static void newFile() throws Exception {
        System.out.println("Tamanho do Registro?");
        pdao = new ProntuarioDAO("dados/pessoa.db", s.nextInt());
        
        s.nextLine();
    }
}
