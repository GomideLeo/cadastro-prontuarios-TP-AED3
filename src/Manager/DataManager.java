package manager;

import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataManager {

    protected String filePath;
    protected int headerSize;
    protected int registerSize;
    protected int nextCode;
    protected int firstEmpty;
    protected long fileSize;
    static RandomAccessFile arquivo;

    private void writeNextCode() {
        try {
            arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(8);
            arquivo.writeInt(nextCode);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFileSize() {
        try {
            arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(16);
            fileSize = arquivo.length();
            arquivo.writeLong(fileSize);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextCode() {
        setNextCode(nextCode + 1);
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

    public long getFileSize() {
        return this.fileSize;
    }

    private void getHeaderData() {
        try {
            arquivo = new RandomAccessFile(filePath, "r");
            arquivo.seek(0);
            headerSize = arquivo.readInt();
            registerSize = arquivo.readInt();
            nextCode = arquivo.readInt();
            firstEmpty = arquivo.readInt();
            fileSize = arquivo.readLong();
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeaderData() {
        try {
            arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(0);
            arquivo.writeInt(headerSize);
            arquivo.writeInt(registerSize);
            arquivo.writeInt(nextCode);
            arquivo.writeInt(firstEmpty);
            arquivo.writeLong(fileSize);
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
        this.headerSize = 24;
        this.nextCode = 1;
        this.firstEmpty = -1;
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
        this.headerSize = 24;
        this.nextCode = nextCode;
        this.firstEmpty = -1;
        this.fileSize = this.headerSize;

        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }

        writeHeaderData();
    }

    private void writeToFile(byte data[], long offset) throws IOException {
        arquivo = new RandomAccessFile(filePath, "rw");
        arquivo.seek(offset);
        arquivo.write(data);
        arquivo.close();
        updateFileSize();
    }

    public int appendToFile(byte data[]) throws IOException {
        // obtem a posição que o registro vai ser inserido
        int pos = (int) (this.fileSize - headerSize) / registerSize;

        writeToFile(data, this.fileSize);

        return pos;
    }

    public int writeToFileBody(byte data[], int offset) throws IOException {
        writeToFile(data, (offset * registerSize) + headerSize);

        return offset;
    }

    private byte[] readFromFile(int len, int offset) throws Exception {

        if (offset > fileSize) {
            throw new IndexOutOfBoundsException();
        }

        byte[] data = new byte[(int) ((offset + len) > fileSize ? fileSize - offset : len)];
        arquivo = new RandomAccessFile(filePath, "r");
        arquivo.seek(offset);
        arquivo.read(data);
        arquivo.close();

        return data;
    }

    public byte[] readFromFileBody(int len, int offset) throws Exception {
        return readFromFile(len * registerSize, (offset * registerSize) + headerSize);
    }

    public int getFirstEmpty() {
        return this.firstEmpty;
    }

    public void setFirstEmpty(int firstEmpty) {
        this.firstEmpty = firstEmpty;
        writeHeaderData();
    }

    public int updateFirstEmpty() throws Exception {
        int firstEmpty = getFirstEmpty();

        if (firstEmpty != -1) {
            // lê o próximo vazio da pilha
            byte[] nextEmpty = readFromFile(4, (firstEmpty * registerSize) + headerSize + 2);

            ByteArrayInputStream bais = new ByteArrayInputStream(nextEmpty);
            DataInputStream dis = new DataInputStream(bais);

            int next = dis.readInt();

            setFirstEmpty(next);
        }

        return firstEmpty;
    }
}
