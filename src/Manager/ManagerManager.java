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
            // this.idxMaanager = INDEX MANAGER
            this.dataManager = new DataManager(this.documentFolder+"/data.db");

            this.dir = new Directory(dirManager.readToFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagerManager(String documentFolder, int dirProfundidade, int dataRegisterSize){
        try { 
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder+"/dir.db", dirProfundidade);
            // this.idxMaanager = INDEX MANAGER
            this.dataManager = new DataManager(this.documentFolder+"/data.db", dataRegisterSize);

            this.dir = initDir(dirProfundidade);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ManagerManager(String documentFolder, int dirProfundidade, int dataRegisterSize, int dataNextCode){
        try { 
            this.documentFolder = documentFolder;
            this.dirManager = new DirectoryManager(this.documentFolder+"/dir.db", dirProfundidade);
            // this.idxMaanager = INDEX MANAGER
            this.dataManager = new DataManager(this.documentFolder+"/data.db", dataRegisterSize, dataNextCode);

            this.dir = initDir(dirProfundidade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Directory initDir(int profundidade) {
        Directory emptyDir = null;
        try {
            emptyDir = new Directory(profundidade);
            dirManager.writeToFile(emptyDir.toByteArray());
            this.saveDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyDir;
    }

    public byte[] findRegister(int key) throws Exception{
        byte[] data = null;
        int bucketAddress = dir.getBucket(dir.hashFunction(key));
        
        ///Bucket bucket = idxManager.getBucket(bucketAddress) ou aglo do tipo
        // int filePosition = bucket.findKey()
        int filePosition = 2;//usado para test

        if(filePosition > 0) { //Ou uma condicao que indique que foi encontrado
            data = dataManager.readFromFileBody(1, filePosition);
        }
        return data;
    }

    public void insertKey(int key){
        int bucketID = dir.hashFunction(key);
        int bucketAddress = dir.getBucket(bucketID);
        //Bucket bucket = idxManager.getBucket(bucketAddress)
        //int keyPosition = bucket.insertKey(key)
        int keyPosition = 3;//test 

        if(keyPosition < 0) { //Ou uma condicao que indique que o bucket ta mt cheio
            int newBucketId = bucketID*2;
            //int newBucketAddress = idxManager.insetBucket(newBucketId);
            //dir.extendDir(newBucketId, newBucketAddress);
        }
    }

    private void saveDir(){
        try {
            dirManager.writeToFile(dir.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
