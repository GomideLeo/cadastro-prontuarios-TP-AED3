package main;
import java.util.Date;

import dao.*;
import manager.*;
import model.*;

public class App {
    public static void main(String[] args) {
        
        Prontuario p1 = new Prontuario(1, "leozao da massa", new Date(100,04,15), 'M', "sou binariooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        
        // System.out.println(p1);
        // System.out.println(p2);
        // System.out.println(p3);
        
        try {
            
            // ProntuarioManager pm = new ProntuarioManager();
            // DbManager dbm = new DbManager("dados/pessoa.db");
            ProntuarioDAO dao = new ProntuarioDAO();
            Prontuario[] res = dao.readNProntuarios(1000, 0);
            for (Prontuario pront : res) {
                System.out.println(pront);
            }
            // dbm.writeToFileBody(pm.prontuarioToByteArray(p1), 0, 200, 0);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
