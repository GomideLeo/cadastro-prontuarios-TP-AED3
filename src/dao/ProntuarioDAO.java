package dao;
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

    public Prontuario getProntuario(int id) {
        Prontuario p = null;

        // dbm.readFromFile();

        return p;
    }
    
    public Prontuario[] readNProntuarios(int n, int offset) throws Exception{
        byte[] vet = dbm.readFromFileBody(n, offset);
        Prontuario[] p = new Prontuario[n];
        for(int i = 0; i < p.length && i * registerSize < vet.length; i++){
            p[i] = pm.byteArrayToProntuario(Arrays.copyOfRange(vet, i * registerSize, (i+1) * registerSize));
        }
        return p;
    }

    public int getFirstEmpty() throws Exception{
        return dbm.getFirstEmpty();
    }

}
