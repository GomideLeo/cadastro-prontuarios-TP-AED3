package main;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import dao.*;
import model.*;

// Classe contendo os testes a serem realizados de performance do projeto
public class Testes {
    static ProntuarioDAO pdao;
    static Random rng = new Random(); // Ideally just create one instance globally

    public static void main(String[] args) {
        String dataPath1 = "/d:/leocg/Documents/PUC/2021-2_3oPeriodo/AEDIII/TrabalhoPratico/cadastro-prontuarios-TP-AED3/dados/benchmark";
        String dataPath2 = "/c:/Users/leocg/AC3TP/dados";

        // 5 000 000 - 250B - 8 - 510

        // 200 000 - 250B - 2 - 62

        // 200 000 - 250B - 8 - 62

        pdao = new ProntuarioDAO(dataPath1 + "/teste01", 8, 62, 500);
        Set<Integer> insertCodes = getRandomCodes(20000);

        long start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        long end = System.currentTimeMillis();

        System.out.println(end - start);

        // 200 000 - 250B - 2 - 126
        // 200 000 - 250B - 8 - 126
        // 200 000 - 250B - 2 - 510
        // 200 000 - 250B - 8 - 510

        // 510

    }

    static Set<Integer> getRandomCodes(int n) {

        // Note: use LinkedHashSet to maintain insertion order
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < n) {
            Integer next = Math.abs(rng.nextInt());
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }

        return generated;
    }

    static void insertNProntuarios(Set<Integer> insertCodes) {
        for (Integer cod : insertCodes) {
            try {
                pdao.createProntuario(generateProntuario(cod));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static Prontuario generateProntuario(int cod) {
        return new Prontuario(cod, "Pessoa " + cod, new Date(cod), cod % 2 == 0 ? 'M' : 'F',
                "O empenho em analisar o desenvolvimento contínuo de distintas formas de atuação desafia a capacidade de equalização de todos os recursos funcionais envolvidos.");
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

}
