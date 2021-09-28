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
        this.saveDir();

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
        dirManager.writeToFile(dir.toByteArray());
    }

    private void updateMemoryDir() {
        dir.readFromByteArray(dirManager.readFromFile());
    }
}
