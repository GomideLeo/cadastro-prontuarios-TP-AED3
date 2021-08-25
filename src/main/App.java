package main;
import java.util.Date;

import dao.*;
import manager.*;
import model.*;

public class App {
    public static void main(String[] args) {
        
        Prontuario p1 = new Prontuario(1, "leozao da ", new Date(100,8,16), 'M', "sou binariiiiiiiiiiiiiiiiiooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        
        // System.out.println(p1);
        // System.out.println(p2);
        // System.out.println(p3);
        
        try {
            ProntuarioDAO pdao = new ProntuarioDAO("dados/pessoa.db");
            
            for (Prontuario p : pdao.readNProntuarios(10, 0)) {
                System.out.println(p);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
