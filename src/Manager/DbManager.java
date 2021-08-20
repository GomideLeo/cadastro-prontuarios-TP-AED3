package Manager;
import model.Prontuario;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class DbManager {

    protected String filePath;

    public DbManager(String filePath){
        this.filePath = filePath;
    }

    public void writeToFile(byte data[]) {
        try {
            FileOutputStream arquivo = new FileOutputStream(filePath);
            arquivo.write(data);
            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readFromFile(int len) {

        byte[] data = new byte[len];
        try {
            FileInputStream arquivo = new FileInputStream(filePath);
            data = arquivo.readNBytes(len);
            arquivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public byte[] getByteArray(Prontuario prontuario) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(prontuario.getCodigo());
            dos.writeUTF(prontuario.getNome());
            dos.writeLong(prontuario.getDataNascimento().getTime());
            dos.writeChar(prontuario.getSexo());
            dos.writeUTF(prontuario.getAnotacoes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public Prontuario setByteArray(byte [] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);

        Prontuario prontuario = new Prontuario();
        try {
            prontuario.setCodigo(dis.readInt());
            prontuario.setNome(dis.readUTF());
            Date date = new Date(dis.readLong());
            prontuario.setDataNascimento(date);
            prontuario.setSexo(dis.readChar());
            prontuario.setAnotacoes(dis.readUTF());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return prontuario;        
    }
}
