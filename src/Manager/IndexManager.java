package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import model.Bucket;

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

    public void startIndex(byte[] buckets, int nBuckets) throws IOException {
        if (this.nBuckets > 0) {
            throw new IOException("O indice já possui dados, operação inválida!");
        }

        this.nBuckets = nBuckets;
        writeToFile(buckets, headerSize);

        writeHeaderData();
    }

    public int insertNewBucket(byte[] bucket) {
        this.nBuckets += 1;

        writeToFile(bucket, fileSize);
        writeHeaderData();

        return this.nBuckets - 1;
    }

    public void updateBucket(byte[] bucket, int pos) throws IndexOutOfBoundsException {
        if (pos > this.nBuckets) {
            throw new IndexOutOfBoundsException("Bucket n" + pos + " ainda não existe, tente inserí-lo primeiro.");
        }
        
        int registerSize = this.bucketSize * 8 + 12;
        writeToFile(bucket, headerSize + (pos * registerSize));
    }

    public byte[] getBucket(int pos) throws Exception {
        byte[] bucket = null;
        int registerSize = this.bucketSize * 8 + 12;
        try {
            bucket = readFromFile(registerSize, this.headerSize + (pos * registerSize));
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Bucket n" + pos + " ainda não existe, tente inserí-lo primeiro.");
        }

        return bucket;
    }

    private byte[] readFromFile(int length, int offset) throws Exception {
        if ((offset + length) > fileSize) {
            throw new IndexOutOfBoundsException();
        }
        byte[] data = new byte[length];

        RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
        arquivo.seek(offset);
        arquivo.read(data);
        arquivo.close();

        return data;
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

    private void updateFileSize(RandomAccessFile arquivo) throws IOException {
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
            arquivo.writeInt(fileSize);
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

    public void insertNBuckets(int n, int profundidade){
        Bucket tempBucket = new Bucket(profundidade, this.bucketSize);
        for (int i = 0; i < n; i++){
            insertNewBucket(tempBucket.toByteArray());
        }
    }
}
