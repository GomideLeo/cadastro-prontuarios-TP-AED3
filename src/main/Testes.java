package main;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    static RandomAccessFile csvWriter;

    public static void main(String[] args) throws IOException {
        String dataPathHDD = "D:/leocg/Documents/PUC/2021-2_3oPeriodo/AEDIII/TrabalhoPratico/dados/benchmark";
        String dataPathSSD = "C:/Users/leocg/AC3TP/dados";
        Set<Integer> insertCodes;
        int nRegisters;
        int registerSize;
        int dirInit;
        int bucketSize;
        String subPath;

        File f = new File("results.csv");
        if (f.exists()) {
            f.delete();
        }

        csvWriter = new RandomAccessFile("results.csv", "rw");

        csvWriter.seek(0);

        csvWriter.writeUTF(" , Tipo de Memória, Tamanho do arquivo de dados, Numero de Registros, Tamanho do Registro,"
                + " Profundidade Inicial do Diretório, Profundidade Final do Diretorio, Tamanho dos Buckets,"
                + " Tempo Total de Inserção (ms), Tempo de Inserção por Registro (ms)\n");

        csvWriter.close();

        // 20 000 - 500B - 2 - 62

        nRegisters = 20000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 2;
        bucketSize = 62;
        subPath = "/teste01";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 20 000 - 500B - 8 - 62

        registerSize = 500;
        dirInit = 8;
        bucketSize = 62;
        subPath = "/teste02";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 500B - 2 - 62

        nRegisters = 200000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 2;
        bucketSize = 62;
        subPath = "/teste03";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 250B - 8 - 62

        registerSize = 500;
        dirInit = 8;
        bucketSize = 62;
        subPath = "/teste04";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 250B - 2 - 126

        registerSize = 500;
        dirInit = 2;
        bucketSize = 126;
        subPath = "/teste05";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 500B - 8 - 126

        registerSize = 500;
        dirInit = 8;
        bucketSize = 126;
        subPath = "/teste06";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 500B - 2 - 510

        registerSize = 500;
        dirInit = 2;
        bucketSize = 510;
        subPath = "/teste07";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 200 000 - 500B - 8 - 510

        registerSize = 500;
        dirInit = 8;
        bucketSize = 510;
        subPath = "/teste08";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 5 000 000 - 500B - 8 - 62

        nRegisters = 5000000;
        insertCodes = getRandomCodes(nRegisters);
        registerSize = 500;
        dirInit = 8;
        bucketSize = 62;
        subPath = "/teste09";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");

        // 5 000 000 - 500B - 8 - 510

        registerSize = 500;
        dirInit = 8;
        bucketSize = 510;
        subPath = "/teste10";

        System.out.println("start " + subPath + " HDD");
        run(insertCodes, dataPathHDD + subPath, nRegisters, registerSize, dirInit, bucketSize, "HDD");

        System.out.println("start " + subPath + " SSD");
        run(insertCodes, dataPathSSD + subPath, nRegisters, registerSize, dirInit, bucketSize, "SSD");
    }

    static void run(Set<Integer> insertCodes, String dataPath, int nRegisters, int registerSize, int dirInit,
            int bucketSize, String memType) throws IOException {
        Path path = Paths.get(dataPath);

        pdao = new ProntuarioDAO(dataPath, dirInit, bucketSize, registerSize);

        long start = System.currentTimeMillis();
        insertNProntuarios(insertCodes);
        long end = System.currentTimeMillis();
        long totalTime = end - start;

        path = Paths.get(dataPath + "/data.db");

        csvWriter = new RandomAccessFile("results.csv", "rw");

        csvWriter.seek(csvWriter.length());

        csvWriter.writeUTF(" , " + memType + ", " + Files.size(path) + ", " + nRegisters + ", " + registerSize + ", "
                + dirInit + ", " + pdao.getDirDepth() + ", " + bucketSize + ", " + totalTime + ", "
                + totalTime / (double) nRegisters + "\n");

        csvWriter.close();
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
