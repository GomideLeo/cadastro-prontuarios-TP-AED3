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

    public Prontuario getProntuario(int id)  {
        return getProntuario(id, 100);
    }

    private Prontuario getProntuario(int id, int step) {
        Prontuario prontuario = null;
        int numberOfRegisters = (dbm.getFileSize() - dbm.getHeaderSize()) / registerSize;

        try {
            for (int i = 0; i < numberOfRegisters && prontuario == null; i += step) {
                prontuario = getProntuario(id, readNProntuarios(step, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prontuario;
    }

    private int getProntuarioIndex(int id, Prontuario[] prontuarios) {
        for (int i = 0; i < prontuarios.length; i++) {
            if (prontuarios[i] != null) {
                if (prontuarios[i].getCodigo() == id) {
                    return i;
                }
            }
        }
        return -1;
    }

    private Prontuario getProntuario(int id, Prontuario[] prontuarios) {
        int prontuarioIndex = getProntuarioIndex(id, prontuarios);

        if (prontuarioIndex != -1) {
            return prontuarios[prontuarioIndex];
        }

        return null;
    }

    public void createProntuario(Prontuario prontuario) throws Exception {
        int offset = this.getFirstEmpty();

        if (prontuario.getCodigo() == -1) {
            prontuario.setCodigo(dbm.getNextCode());
        }

        if (offset == -1)
            dbm.appendToFile(pm.prontuarioToByteArray(prontuario));
        else
            dbm.writeToFileBody(pm.prontuarioToByteArray(prontuario), offset);
    }

    public boolean updateProntuario(Prontuario prontuario) {
        return updateProntuario(prontuario, 100, false);
    }
    
    public boolean deleteProntuario(int id) {
        return updateProntuario(new Prontuario(id), 100, true);
    }

    private boolean updateProntuario(Prontuario prontuario, int step, boolean delete) {
        int numberOfRegisters = (dbm.getFileSize() - dbm.getHeaderSize()) / registerSize;
        int offset = -1;
        int index = -1;

        try {
            for (int i = 0; i < numberOfRegisters && offset == -1; i += step) {
                index = getProntuarioIndex(prontuario.getCodigo(), readNProntuarios(step, i));

                if (index != -1) {
                    offset = i + index;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (offset >= 0) {
            dbm.writeToFileBody(pm.prontuarioToByteArray(prontuario, delete), offset);
            return true;
        }

        return false;
    }

    public Prontuario[] readNProntuarios(int n, int offset) throws Exception {
        byte[] vet = dbm.readFromFileBody(n, offset);
        Prontuario[] p = new Prontuario[n];
        for (int i = 0; i < p.length && i * registerSize < vet.length; i++) {
            p[i] = pm.byteArrayToProntuario(Arrays.copyOfRange(vet, i * registerSize, (i + 1) * registerSize));
        }
        return p;
    }

    public int getFirstEmpty() throws Exception {
        return dbm.getFirstEmpty();
    }

}
