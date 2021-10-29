package manager;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataManager {

    protected String filePath;
    protected int headerSize;
    protected int registerSize;
    protected int nextCode;
    protected int fileSize;

    private void writeNextCode() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(8);
            arquivo.writeInt(nextCode);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFileSize() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(12);
            fileSize = (int) arquivo.length();
            arquivo.writeInt(fileSize);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextCode() {
        setNextCode(nextCode+1);
        return nextCode - 1;
    }

    public void setNextCode(int nextCode) {
        this.nextCode = nextCode;
        writeNextCode();
    }

    public int getHeaderSize() {
        return this.headerSize;
    }

    public int getRegisterSize() {
        return this.registerSize;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    private void getHeaderData() {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
            arquivo.seek(0);
            headerSize = arquivo.readInt();
            registerSize = arquivo.readInt();
            nextCode = arquivo.readInt();
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
            arquivo.writeInt(registerSize);
            arquivo.writeInt(nextCode);
            arquivo.writeInt(fileSize);
            arquivo.close();
            updateFileSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataManager(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        File f = new File(filePath);
        if (f.exists()) {
            getHeaderData();
        } else {
            throw new FileNotFoundException();
        }
    }

    public DataManager(String filePath, int registerSize) {
        this.filePath = filePath;
        this.registerSize = registerSize;
        this.headerSize = 16;
        this.nextCode = 1;
        this.fileSize = this.headerSize;

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }

        writeHeaderData();
    }

    public DataManager(String filePath, int registerSize, int nextCode) {
        this.filePath = filePath;
        this.registerSize = registerSize;
        this.headerSize = 16;
        this.nextCode = nextCode;
        this.fileSize = this.headerSize;

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }

        writeHeaderData();
    }
    
    private void writeToFile(byte data[], int offset) {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(offset);
            arquivo.write(data);
            arquivo.close();
            updateFileSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int appendToFile(byte data[]) {
        // obtem a posição que o registro vai ser inserido
        int pos = (this.fileSize - headerSize)/registerSize;

        writeToFile(data, this.fileSize);

        return pos;
    }

    public int writeToFileBody(byte data[], int offset) {
        writeToFile(data, (offset * registerSize) + headerSize);

        return offset;
    }

    private byte[] readFromFile(int len, int offset) throws Exception {

        if (offset > fileSize) {
            throw new IndexOutOfBoundsException();
        }

        byte[] data = new byte[(offset + len) > fileSize ? fileSize - offset : len];
        RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
        arquivo.seek(offset);
        arquivo.read(data);
        arquivo.close();

        return data;
    }

    public byte[] readFromFileBody(int len, int offset) throws Exception {
        return readFromFile(len * registerSize, (offset * registerSize) + headerSize);
    }

    public int getFirstEmpty() throws Exception {
        RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
        for (int i = 0; (i*registerSize + headerSize) < fileSize; i++) {
            arquivo.seek((i*registerSize) + headerSize);
            if (arquivo.readChar() == '*') {
                arquivo.close();
                return i;
            }
        }
        arquivo.close();
        return -1;
    }

}
