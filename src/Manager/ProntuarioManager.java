package manager;
import model.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class ProntuarioManager {

    // 1B 4B 8B 1B 4B + TamStrings
    // [char Lapide] [int cod] [long data] [char sexo] [String nome] [String
    // Anotações]
    protected int registerSize;

    public ProntuarioManager() {
        registerSize = 200;
    }

    public ProntuarioManager(int registerSize) throws Exception{
        this.setRegisterSize(registerSize);
    }

    public int getRegisterSize() {
        return this.registerSize;
    }

    public void setRegisterSize(int registerSize) throws Exception {
        if (registerSize >= 50)
            this.registerSize = registerSize;
        else
            throw new Exception("RegisterSize invalid");
    }

    public byte[] prontuarioToByteArray(Prontuario prontuario) throws IndexOutOfBoundsException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeChar('\0');
            dos.writeInt(prontuario.getCodigo());
            dos.writeUTF(prontuario.getNome());
            dos.writeLong(prontuario.getDataNascimento().getTime());
            dos.writeChar(prontuario.getSexo());
            dos.writeUTF(prontuario.getAnotacoes());
            // dos.writeUTF(
            //     prontuario.getAnotacoes().substring(0, 
            //         prontuario.getAnotacoes().length() > registerSize - baos.toByteArray().length -2 ? 
            //         registerSize - baos.toByteArray().length -2 : 
            //         prontuario.getAnotacoes().length() 
            //     )
            // );
            if (baos.toByteArray().length <= registerSize){
                byte[] tailLength = new byte[registerSize - baos.toByteArray().length];
                dos.write(tailLength);
            } else {
                throw new IndexOutOfBoundsException("Prontuário excede limite de tamanho.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return baos.toByteArray();
    }

    public Prontuario byteArrayToProntuario(byte [] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);

        Prontuario prontuario = null;
        try {
            if(dis.readChar() != '*'){
                prontuario = new Prontuario();
                prontuario.setCodigo(dis.readInt());
                prontuario.setNome(dis.readUTF());
                Date date = new Date(dis.readLong());
                prontuario.setDataNascimento(date);
                prontuario.setSexo(dis.readChar());
                prontuario.setAnotacoes(dis.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prontuario;        
    }
}