package dao;

import java.util.Arrays;

import manager.*;
import model.*;

public class ProntuarioDAO {
    protected DataManager dataManager;
    protected ManagerManager managerManager;
    protected int registerSize;

    public ProntuarioDAO(String documentFolder) {
        try {
            managerManager = new ManagerManager(documentFolder);
            dataManager = new DataManager(documentFolder + "/data.db");
            registerSize = dataManager.getRegisterSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProntuarioDAO(String documentFolder, int dirProfundidade, int registersInBucket, int dataRegisterSize) {
        this.registerSize = dataRegisterSize;
        try {
            managerManager = new ManagerManager(documentFolder, dirProfundidade, registersInBucket, dataRegisterSize);
            dataManager = new DataManager(documentFolder + "/data.db", registerSize);
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
            dataManager = new DataManager(documentFolder + "/data.db", dataRegisterSize, dataNextCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prontuario getProntuario(int id) throws Exception {
        return new Prontuario(managerManager.findRegister(id));
    }
    
    public Prontuario getProntuarioWOIndex(int id) {
        return getProntuario(id, 100);
    }

    private Prontuario getProntuario(int id, int step) {
        Prontuario prontuario = null;
        int numberOfRegisters = (dataManager.getFileSize() - dataManager.getHeaderSize()) / registerSize;

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

        if (prontuario.getCodigo() == -1) {
            prontuario.setCodigo(dataManager.getNextCode());
        }

        managerManager.insertKey(prontuario.getCodigo(), prontuario.toByteArray(registerSize));
    }

    public boolean updateProntuario(Prontuario prontuario) {
        return updateProntuario(prontuario, 100, false);
    }

    public boolean deleteProntuario(int id) {
        return updateProntuario(new Prontuario(id), 100, true);
    }

    private boolean updateProntuario(Prontuario prontuario, int step, boolean delete) {
        int numberOfRegisters = (dataManager.getFileSize() - dataManager.getHeaderSize()) / registerSize;
        int offset = -1;
        int index = -1;

        try {
            for (int i = 0; i < numberOfRegisters && offset == -1; i += step) {
                index = getProntuarioIndex(prontuario.getCodigo(), readNProntuarios(step, i));

                if (index != -1) {
                    offset = i + index;
                }
            }

            if (offset >= 0) {
                prontuario = new Prontuario(dataManager.readFromFileBody(1, offset));
                dataManager.writeToFileBody(prontuario.toByteArray(registerSize, delete), offset);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Prontuario[] readNProntuarios(int n, int offset) throws Exception {
        byte[] vet = dataManager.readFromFileBody(n, offset);
        Prontuario[] p = new Prontuario[n];
        for (int i = 0; i < p.length && i * registerSize < vet.length; i++) {
            p[i] = new Prontuario(Arrays.copyOfRange(vet, i * registerSize, (i + 1) * registerSize));
        }
        return p;
    }

}
