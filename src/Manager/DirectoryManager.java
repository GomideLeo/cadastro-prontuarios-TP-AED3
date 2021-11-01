package manager;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;

public class DirectoryManager {
    private String filePath;

    public DirectoryManager(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
    }

    public DirectoryManager(String filePath, int profundidade) {
        this.filePath = filePath;
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        writeHeader(profundidade);
    }

    public void writeHeader(int profundidade) {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(0);
            arquivo.writeInt(profundidade);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeToFile(byte[] data) {
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "rw");
            arquivo.seek(0);
            arquivo.write(data);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] readFromFile() {
        byte[] data = null;
        try {
            RandomAccessFile arquivo = new RandomAccessFile(filePath, "r");
            data = new byte[(int) arquivo.length()];
            arquivo.seek(0);
            arquivo.read(data);
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
