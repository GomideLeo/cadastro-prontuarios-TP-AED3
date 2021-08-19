package Manager;
import model.Pessoa;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DbManager {

    protected String filePath;

    public DbManager(String filePath){
        this.filePath = filePath;
    }

    public byte[] getByteArray(Pessoa pessoa) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            // dos.writeInt(pessoa.getCodigo());
            // dos.writeUTF(pessoa.nome);
            // dos.writeByte(pessoa.idade);
            // dos.writeFloat(pessoa.salario);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public void setByteArray(byte [] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);

        Pessoa pessoa = new Pessoa();
        try {
            // pessoa.codigo = dis.readInt();
            // pessoa.nome = dis.readUTF();
            // pessoa.idade = dis.readByte();
            // pessoa.salario = dis.readFloat();
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
