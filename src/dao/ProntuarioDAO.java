package dao;
import java.io.EOFException;
import java.util.Arrays;

import manager.*;
import model.*;

public class ProntuarioDAO {
    protected ProntuarioManager pm;
    protected DbManager dbm;
    protected int registerSize;
    
    public ProntuarioDAO(String filePath) {
        try {
            dbm = new DbManager(filePath);
            registerSize = dbm.getRegisterSize();
            pm = new ProntuarioManager(registerSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProntuarioDAO(String filePath, int registerSize) {
        this.registerSize = registerSize;
        try {
            pm = new ProntuarioManager(registerSize);
            dbm = new DbManager(filePath, registerSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prontuario getProntuario(int id)  throws Exception{
        return getProntuario(id, 100);
    }

    public Prontuario getProntuario(int id, int step) throws Exception{
        Prontuario prontuario = null;
        int size =  dbm.getFileSize();
        int test = registerSize;
        int numberOfRegisters = dbm.getFileSize()/registerSize;

        for (int i = 0; i < numberOfRegisters && prontuario == null; i+=step){
            prontuario = getProntuario(id, readNProntuarios(step,i));
        }

        return prontuario;
    }

    public Prontuario getProntuario (int id, Prontuario[] prontuarios) {
        for(Prontuario prontuario : prontuarios){
            if(prontuario != null){
                if(prontuario.getCodigo() == id){
                    return prontuario;
                }
            }
        }
        return null;
    }
    
    public void writeProntuario (Prontuario prontuario) throws Exception{
        int offset = this.getFirstEmpty();
        if(offset == -1)
            dbm.appendToFile(pm.prontuarioToByteArray(prontuario));
        else
            dbm.writeToFileBody(pm.prontuarioToByteArray(prontuario), offset);
    }

    public Prontuario[] readNProntuarios(int n, int offset) throws Exception{
        byte[] vet = dbm.readFromFileBody(n, offset);
        Prontuario[] p = new Prontuario[n];
        for(int i = 0; i < p.length && i * registerSize < vet.length; i++){
            p[i] = pm.byteArrayToProntuario(Arrays.copyOfRange(vet, i * registerSize, (i+1) * registerSize));
        }
        return p;
    }

    public int getFirstEmpty() throws Exception {
        return dbm.getFirstEmpty();
    }

}
