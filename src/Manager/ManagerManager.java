package manager;

import manager.*;
import model.*;

import java.io.IOException;

public class ManagerManager {
    private String documentFolder;
    private DataManager dataManager;
    private DirectoryManager dirManager;
    private IndexManager idxManager;

    private Directory dir;

    public ManagerManager(String documentFolder) {
        try { 
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder+"/dir.db");
            this.idxManager = new IndexManager(this.documentFolder+"/idx.db");
            this.dataManager = new DataManager(this.documentFolder+"/data.db");

            this.updateMemoryDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagerManager(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize){
        try { 
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder+"/dir.db", dirProfundidade);
            this.idxManager = new IndexManager(this.documentFolder + "/idx.db", registersInBucket);
            this.dataManager = new DataManager(this.documentFolder+"/data.db", dataRegisterSize);
            
            this.dir = initDir(dirProfundidade);
            this.saveDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagerManager(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize, int dataNextCode){
        try { 
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder+"/dir.db", dirProfundidade);
            this.idxManager = new IndexManager(this.documentFolder + "/idx.db", registersInBucket);
            this.dataManager = new DataManager(this.documentFolder+"/data.db", dataRegisterSize, dataNextCode);

            this.dir = initDir(dirProfundidade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Directory initDir(int profundidade) {
        Directory emptyDir = null;
        
        emptyDir = new Directory(profundidade);

        return emptyDir;
    }

    public byte[] findRegister(int key) throws Exception{
        byte[] data = null;
        int bucketAddress = dir.getBucket(dir.hashFunction(key));
        
        Bucket bucket = new Bucket(0, 0);
        bucket.fromByteArray(idxManager.getBucket(bucketAddress));
        int filePosition = bucket.getKeyData(key);

        System.out.println("File position: " + filePosition);
        //TODO : Implementar leitura no arq mestre. Segue pseudo codigo abaixo
        // if(filePosition > 0) { //Ou uma condicao que indique que foi encontrado
        //     data = dataManager.readFromFileBody(1, filePosition);
        // }

        return data;
    }

    public void insertKey(int key, int value){

        int bucketID = dir.hashFunction(key);
        int bucketAddress = dir.getBucket(bucketID);
        Bucket bucket = null;
        try {
            bucket = new Bucket(idxManager.getBucket(bucketAddress));
        } catch (IndexOutOfBoundsException e) {
            bucket = new Bucket(1, idxManager.getBucketSize());
            bucketAddress = idxManager.insertNewBucket(bucket.toByteArray());
            dir.setBucket(bucketAddress, bucketID);
        } catch (Exception e){
            e.printStackTrace();
        }

        bucket.insertData(key, value);
        byte[] data = bucket.toByteArray();
        idxManager.updateBucket(data, bucketAddress);

        //TODO: faltar levar em consideracao se bucket estourar o tamanho
        // if(keyPosition < 0) { //Ou uma condicao que indique que o bucket ta mt cheio
        //     int newBucketId = bucketID*2;
        //     //int newBucketAddress = idxManager.insetBucket(newBucketId);
        //     //dir.extendDir(newBucketId, newBucketAddress);
        // }
    }

    private void saveDir(){
        dirManager.writeToFile(dir.toByteArray());
    }

    private void updateMemoryDir() {
        dir.readFromByteArray(dirManager.readFromFile());
    }
}
