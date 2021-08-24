package manager;
import java.io.RandomAccessFile;
import java.io.IOException;

public class DbManager {

    protected String filePath;

    public DbManager(String filePath){
        this.filePath = filePath;
    }

    public void writeToFile(byte data[], int offset, int len) {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(offset);
            arquivo.write(data, 0, len);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeToFileBody(byte data[], int offset, int len, int headerSize) {
        writeToFile(data, offset+headerSize, len);
    }

    public byte[] readFromFile(int len, int offset) throws Exception{

        byte[] data = new byte[len];
        RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
        arquivo.seek(offset);
        arquivo.read(data);
        arquivo.close();

        return data;
    }

    public byte[] readFromFileBody(int len, int offset, int headerSize) throws Exception {
        return readFromFile(len, offset+headerSize);
    }
}
