package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import dao.*;
import model.*;

// Classe contendo os testes a serem realizados de performance do projeto
public class Testes {
    static ProntuarioDAO pdao;
    static Random rng = new Random(); // Ideally just create one instance globally
    static BufferedWriter csvWriter;

    public static void main(String[] args) throws IOException {
        String dataPathHDD = "/d:/leocg/Documents/PUC/2021-2_3oPeriodo/AEDIII/TrabalhoPratico/dados/benchmark";
        String dataPathSSD = "/c:/Users/leocg/AC3TP/dados";
        Path path;
        Set<Integer> insertCodes;
        long start;
        long end;
        long totalTime;
        int nRegisters;
        int registerSize;
        int dirInit;
        int bucketSize;
        String subPath;

        csvWriter = new BufferedWriter(new FileWriter("results.csv"));

        csvWriter.append(
                "Tipo de Memória, Tamanho do arquivo de dados, Numero de Registros, Tamanho do Registro, Profundidade Inicial do Diretório, Profundidade Final do Diretorio, Tamanho dos Buckets, Tempo Total de Inserção (ms), Tempo de Inserção por Registro (ms)\n");

        // 20 000 - 500B - 2 - 62

        nRegisters = 20000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 2;
        bucketSize = 62;
        subPath = "/teste01";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 20 000 - 500B - 8 - 62

        registerSize = 500;
        dirInit = 8;
        bucketSize = 62;
        subPath = "/teste02";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 500B - 2 - 62

        nRegisters = 200000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 2;
        bucketSize = 62;
        subPath = "/teste03";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 250B - 8 - 62

        registerSize = 500;
        dirInit = 8;
        bucketSize = 62;
        subPath = "/teste04";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 250B - 2 - 126

        registerSize = 500;
        dirInit = 2;
        bucketSize = 126;
        subPath = "/teste05";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 500B - 8 - 126

        registerSize = 500;
        dirInit = 8;
        bucketSize = 126;
        subPath = "/teste06";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 500B - 2 - 510

        registerSize = 500;
        dirInit = 2;
        bucketSize = 510;
        subPath = "/teste07";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 200 000 - 500B - 8 - 510

        registerSize = 500;
        dirInit = 8;
        bucketSize = 510;
        subPath = "/teste08";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        // 5 000 000 - 500B - 8 - 510

        nRegisters = 5000000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 8;
        bucketSize = 510;
        subPath = "/teste09";

        System.out.println("start " + subPath + " HDD");

        pdao = new ProntuarioDAO(dataPathHDD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathHDD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

        System.out.println("start " + subPath + " SSD");

        pdao = new ProntuarioDAO(dataPathSSD + subPath, dirInit, bucketSize, registerSize);

        start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        end = System.currentTimeMillis();
        totalTime = end - start;

        path = Paths.get((dataPathSSD + subPath + "/dir.db"));

        csvWriter.append("HDD, " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", " + dirInit + ", "
                + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", " + totalTime / nRegisters + "\n");

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
