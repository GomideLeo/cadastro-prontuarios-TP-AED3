package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexManager {
    protected String filePath;
    protected int headerSize;
    protected int bucketSize;
    protected int nBuckets;
    protected int fileSize;
    
    public IndexManager(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        File f = new File(filePath);
        if (f.exists()) {
            getHeaderData();
        } else {
            throw new FileNotFoundException();
        }
    }

    public IndexManager(String filePath, int bucketSize) throws FileNotFoundException {
        this.filePath = filePath;
        this.headerSize = 16;
        this.bucketSize = bucketSize;
        this.nBuckets = 0;
        this.fileSize = this.headerSize;

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        writeHeaderData();
    }


    
    public void startIndex (byte[] buckets, int nBuckets) throws IOException{
        if (this.nBuckets > 0 ) {
            throw new IOException("O indice já possui dados, operação inválida!");
        }
        
        this.nBuckets = nBuckets;
        writeToFile(buckets, headerSize);
        
        writeHeaderData();
    }

    public int insertNewBucket (byte[] bucket) {
        this.nBuckets += 1;
        
        writeToFile(bucket, fileSize);
        writeHeaderData();
        
        return nBuckets-1;
    }
    
    public void updateBucket (byte[] bucket, int pos) throws IndexOutOfBoundsException {
        if (pos > this.nBuckets) {
            throw new IndexOutOfBoundsException("Bucket n"+pos+" ainda não existe, tente inserí-lo primeiro.");
        }
        
        writeToFile(bucket, headerSize+(pos*bucketSize));
    } 

    private void writeToFile(byte data[], int offset) {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(offset);
            arquivo.write(data);
            updateFileSize(arquivo);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void updateFileSize(RandomAccessFile arquivo) throws IOException{
        arquivo.seek(12);
        fileSize = (int) arquivo.length();
        arquivo.writeInt(fileSize);
    }
    
    private void getHeaderData() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
            arquivo.seek(0);
            headerSize = arquivo.readInt();
            bucketSize = arquivo.readInt();
            nBuckets = arquivo.readInt();
            fileSize = arquivo.readInt();
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeHeaderData() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(0);
            arquivo.writeInt(headerSize);
            arquivo.writeInt(bucketSize);
            arquivo.writeInt(nBuckets);
            updateFileSize(arquivo);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBucketSize() {
        return this.bucketSize;
    }

    public int getNBuckets() {
        return this.nBuckets;
    }

    public int getFileSize() {
        return this.fileSize;
    }

}
