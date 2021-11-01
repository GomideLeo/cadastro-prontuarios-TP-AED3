package manager;

import model.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ManagerManager {
    private String documentFolder;
    private DataManager dataManager;
    private DirectoryManager dirManager;
    private IndexManager idxManager;

    private Directory dir;

    public ManagerManager(String documentFolder) {
        try {
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder + "/dir.db");
            this.idxManager = new IndexManager(this.documentFolder + "/idx.db");
            this.dataManager = new DataManager(this.documentFolder + "/data.db");

            this.updateMemoryDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagerManager(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize) {
        try {
            this.documentFolder = documentFolder;
            File theDir = new File(documentFolder);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            this.dirManager = new DirectoryManager(this.documentFolder + "/dir.db", dirProfundidade);
            this.idxManager = new IndexManager(this.documentFolder + "/idx.db", registersInBucket);
            this.dataManager = new DataManager(this.documentFolder + "/data.db", dataRegisterSize);

            this.dir = initDir(dirProfundidade);

            this.saveDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagerManager(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize,
            int dataNextCode) {
        try {
            this.documentFolder = documentFolder;
            File theDir = new File(documentFolder);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            this.dirManager = new DirectoryManager(this.documentFolder + "/dir.db", dirProfundidade);
            this.idxManager = new IndexManager(this.documentFolder + "/idx.db", registersInBucket);
            this.dataManager = new DataManager(this.documentFolder + "/data.db", dataRegisterSize, dataNextCode);

            this.dir = initDir(dirProfundidade);

            this.saveDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Directory initDir(int profundidade) {
        Directory emptyDir = null;

        emptyDir = new Directory(profundidade);
        this.idxManager.insertNBuckets((int) Math.pow(2, profundidade), profundidade);

        return emptyDir;
    }

    public byte[] findRegister(int key) throws Exception {
        byte[] data = null;
        int bucketAddress = dir.getBucket(dir.hashFunction(key));

        Bucket bucket = new Bucket(0, 0);
        bucket.fromByteArray(idxManager.getBucket(bucketAddress));
        int filePosition = bucket.getKeyData(key);

        System.out.println("File position: " + filePosition);
        if (filePosition >= 0) {
            data = dataManager.readFromFileBody(1, filePosition);
        }

        return data;
    }

    private Bucket getBucketByKey(int key) {
        int bucketID = dir.hashFunction(key);
        int bucketAddress = dir.getBucket(bucketID);
        Bucket bucket = null;

        try {
            bucket = new Bucket(idxManager.getBucket(bucketAddress));
        } catch (IndexOutOfBoundsException e) {
            bucket = new Bucket(dir.getProfundidade(), idxManager.getBucketSize());
            bucketAddress = idxManager.insertNewBucket(bucket.toByteArray());
            dir.setBucket(bucketAddress, bucketID);

            this.saveDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bucket;
    }

    private int[] getAllBucketsForKey(int key, int bucketDepth) {
        int bucketDirSize = (int) (Math.pow(2, bucketDepth));
        int keyBucketID = dir.hashFunction(key, bucketDepth);
        int depthDiff = dir.getProfundidade() - bucketDepth;
        // caso depthDiff == 0, usar 1, pois o diretório será expandido
        int nBucketsForKey = (int) Math.pow(2, depthDiff == 0 ? 1 : depthDiff);

        int[] bucketIndexes = new int[nBucketsForKey];

        for (int i = 0; i < nBucketsForKey; i++) {
            bucketIndexes[i] = bucketDirSize * i + keyBucketID;
        }

        return bucketIndexes;
    }

    private void insertToBucket(Bucket bucket, int key, int value) {
        int bucketID = dir.hashFunction(key);
        int bucketAddress = dir.getBucket(bucketID);

        if (bucket.getEmptyLength() > 0) {
            bucket.insertData(key, value);
            idxManager.updateBucket(bucket.toByteArray(), bucketAddress);
        } else {
            // Como nao ha espaco vai ter q refatorar o bucket
            // nesse caso salvo os dados que estao no buckt
            HashMap<Integer, Integer> bucketMap = new HashMap<Integer, Integer>(bucket.getData());
            bucketMap.put(key, value);

            // obtem todas as posições que apontam para o mesmo endereço
            int[] possibleBucketIds = getAllBucketsForKey(key, bucket.getProfundidade());

            // Crio o novo bucket com profundidade n+1
            Bucket newBucket = new Bucket(bucket.getProfundidade() + 1, idxManager.getBucketSize());
            int newBucketAddress = idxManager.insertNewBucket(newBucket.toByteArray());

            // Limpo bucket atual e salvo mudanca
            bucket.clearData();
            bucket.setProfundidade(bucket.getProfundidade() + 1);
            idxManager.updateBucket(bucket.toByteArray(), bucketAddress);

            if (bucket.getProfundidade() <= dir.getProfundidade()) {
                // setar bucket na pos caso nao seja necessario duplicar o dir
                for (int i = 0; i < possibleBucketIds.length; i++) {
                    // atualiza cada segundo bucket para apontar para o novo
                    if (i % 2 == 1) {
                        dir.setBucket(newBucketAddress, possibleBucketIds[i]);
                    }
                }
            } else {
                // duplicar dir atualizando o segundo bucket encontrado no diretório
                // sempre serão 2, pois depthDiff será sempre = 0
                dir.extendDir(possibleBucketIds[1], newBucketAddress);
            }

            // Refatorar o bucket
            for (Map.Entry<Integer, Integer> entry : bucketMap.entrySet()) {
                int destination = dir.hashFunction(entry.getKey());
                int destinationIndex = java.util.Arrays.binarySearch(possibleBucketIds, destination);

                if (destinationIndex != -1) {
                    if (destinationIndex % 2 == 0)
                        bucket.insertData(entry.getKey(), entry.getValue());
                    else
                        newBucket.insertData(entry.getKey(), entry.getValue());
                }
            }

            idxManager.updateBucket(bucket.toByteArray(), bucketAddress);
            idxManager.updateBucket(newBucket.toByteArray(), newBucketAddress);
            this.saveDir();
        }
    }

    public void insertKey(int key, byte[] object) throws Exception {
        int offset = -1;
        offset = dataManager.getFirstEmpty();

        if (offset == -1) {
            offset = dataManager.appendToFile(object);
        } else
            dataManager.writeToFileBody(object, offset);

        Bucket bucket = this.getBucketByKey(key);
        this.insertToBucket(bucket, key, offset);
    }

    private void saveDir() {
        dirManager.writeToFile(dir.toByteArray());
    }

    private void updateMemoryDir() {
        dir.readFromByteArray(dirManager.readFromFile());
    }
}
