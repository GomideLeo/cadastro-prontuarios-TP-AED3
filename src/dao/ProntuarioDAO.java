package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import manager.*;
import model.*;

public class ProntuarioDAO {
    protected ManagerManager managerManager;
    protected int registerSize;

    public ProntuarioDAO(String documentFolder) throws FileNotFoundException {
        managerManager = new ManagerManager(documentFolder);
        registerSize = managerManager.getRegisterSize();
    }

    public ProntuarioDAO(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize) {
        this.registerSize = dataRegisterSize;
        try {
            managerManager = new ManagerManager(documentFolder, dirProfundidade, registersInBucket, dataRegisterSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProntuarioDAO(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize,
            int dataNextCode) {
        this.registerSize = dataRegisterSize;
        try {
            managerManager = new ManagerManager(documentFolder, dirProfundidade, registersInBucket, dataRegisterSize,
                    dataNextCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prontuario getProntuario(int id) throws Exception {
        byte[] byteArray = managerManager.findRegister(id);

        if (byteArray != null) {
            return new Prontuario(byteArray);
        }

        return null;
    }

    public int createProntuario(Prontuario prontuario) throws Exception {

        if (prontuario.getCodigo() == -1) {
            prontuario.setCodigo(managerManager.getNextCode());
        }

        managerManager.insertKey(prontuario.getCodigo(), prontuario.toByteArray(registerSize));

        return prontuario.getCodigo();
    }

    public boolean updateProntuario(Prontuario prontuario) throws IndexOutOfBoundsException, IOException {
        return managerManager.updateObject(prontuario.getCodigo(), prontuario.toByteArray(registerSize)) >= 0 ? true
                : false;
    }

    public boolean deleteProntuario(int id) {
        try {
            return managerManager.deleteObject(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Prontuario[] readNProntuarios(int n, int offset) throws Exception {
        byte[] vet = managerManager.readFromFileBody(n, offset);
        Prontuario[] p = new Prontuario[n];
        for (int i = 0; i < p.length && i * registerSize < vet.length; i++) {
            p[i] = new Prontuario(Arrays.copyOfRange(vet, i * registerSize, (i + 1) * registerSize));
        }
        return p;
    }

}
