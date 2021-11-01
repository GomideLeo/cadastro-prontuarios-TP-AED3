package main;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import dao.*;
import model.*;

public class App {
    static Scanner s = new Scanner(System.in);
    static ProntuarioDAO pdao;

    public static void main(String[] args) {

        // try {
        // for (int i = 0; i < 200; i++) {
        // pdao.createProntuario(generateRandom());
        // }

        // Prontuario[] ps = pdao.readNProntuarios(200, 0);
        // for (Prontuario p : ps) {
        // System.out.println(p);
        // }

        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        menu();
    }

    static String generateRandomString(int len) {
        int leftLimit = 32; // spacebar ' '
        int rightLimit = 122; // letter 'z'
        int targetStringLength = len;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 32 || i >= 48) && (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        return generatedString;
    }

    @SuppressWarnings("deprecation")
    static Prontuario generateRandom() {
        return new Prontuario(-1, generateRandomString((int) Math.random() * 10 + 10),
                new Date((int) (Math.random() * 70 + 50), (int) (Math.random() * 12), (int) (Math.random() * 30 + 1)),
                (Math.random() < 0.5 ? 'M' : 'F'), generateRandomString((int) (Math.random() * 100) + 10));
    }

    static void menu() {

        try {
            openFile();

            int i = 0;

            do {
                System.out.println("#================#\n" + "|      Menu      |\n" + "| 0. Exit        |\n"
                        + "| 1. Create      |\n" + "| 2. Read        |\n" + "| 3. Update      |\n"
                        + "| 4. Delete      |\n" + "| 5. Read N      |\n" + "| 6. New File    |\n"
                        + "| 7. Open File   |\n" + "#================#\n");

                i = s.nextInt();
                s.nextLine();

                switch (i) {
                case 1:
                    create();
                    break;
                case 2:
                    read();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    readN();
                    break;
                case 6:
                    newFile();
                    break;
                case 7:
                    openFile();
                    break;
                }
            } while (i != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    static void create() throws Exception {

        System.out.println("Codigo? (-1 para autocompletar)");
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

        System.out.println(pdao.createProntuario(p));
    }

    static void read() throws Exception {

        System.out.println("Codigo?");
        System.out.println(pdao.getProntuario(s.nextInt()));
        s.nextLine();
    }

    @SuppressWarnings("deprecation")
    static void update() throws Exception {

        System.out.println("Codigo?");
        Prontuario p = new Prontuario(s.nextInt());
        s.nextLine();

        System.out.print("\nAntigo prontuário: ");
        p = pdao.getProntuario(p.getCodigo());

        if (p != null) {
            System.out.println(pdao.getProntuario(p.getCodigo()));

            System.out.println("\nNome?");
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
        } else {
            System.out.println("\n Prontuário não encontrado!");
        }
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

    static void newFile(String documentFolder) {
        System.out.println("Profundidade inicial do diretório");
        int dirProfundidade = s.nextInt();
        s.nextLine();

        System.out.println("Registros por Bucket?");
        int registersInBucket = s.nextInt();
        s.nextLine();

        System.out.println("Tamanho do Registro?");
        int dataRegisterSize = s.nextInt();
        s.nextLine();

        System.out.println("Primeiro código?");
        int dataNextCode = s.nextInt();
        s.nextLine();

        pdao = new ProntuarioDAO(documentFolder, dirProfundidade, registersInBucket, dataRegisterSize, dataNextCode);
    }

    static void newFile() {
        System.out.println("Caminho para o arquivo?");
        String documentFolder = "dados/" + s.nextLine();

        newFile(documentFolder);
    }

    static void openFile() {
        System.out.println("Caminho para o arquivo?");
        String documentFolder = "dados/" + s.nextLine();
        try {
            pdao = new ProntuarioDAO(documentFolder);
        } catch (FileNotFoundException e) {
            newFile(documentFolder);
        }
    }
}
