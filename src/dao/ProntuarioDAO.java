package dao;
import java.nio.ByteBuffer;
import java.util.Arrays;

import manager.*;
import model.*;

public class ProntuarioDAO {
    protected int headerSize;
    protected ProntuarioManager pm;
    protected DbManager dbm;

    public ProntuarioDAO() {
        pm = new ProntuarioManager();
        dbm = new DbManager("dados/pessoa.db");
        headerSize = 0;
    }

    public ProntuarioDAO(int registerSize) {
        try {
            pm = new ProntuarioManager(registerSize);
            dbm = new DbManager("dados/pessoa.db");
            headerSize = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prontuario getProntuario(int id) {
        Prontuario p = null;

        // dbm.readFromFile();

        return p;
    }

    public void writeHeader(){
        
    }

    public Prontuario[] readNProntuarios(int n, int offset){
        byte[] vet = dbm.readFromFileBody(pm.getRegisterSize()*n, offset*pm.getRegisterSize(), headerSize);
        Prontuario[] p = new Prontuario[n];
        for(int i = 0; i < p.length; i++){
            p[i] = pm.byteArrayToProntuario(Arrays.copyOfRange(vet, i*200, (i+1)*200));
        }
        return p;
    }

    public int getFirstEmpty(){
        int size = pm.getRegisterSize();
        int i = 0;
        Prontuario p = null;
        do{
            p = pm.byteArrayToProntuario(dbm.readFromFileBody(size, size*i++, headerSize));
        }while(p != null);
        return i;
    }

}
