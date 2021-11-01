package model;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Directory {
    private int profundidade;
    private int buckets[];

    public Directory(){
        this.profundidade = 3;
        initDir();
    }

    public Directory(int profundidade){
        this.profundidade = profundidade;
        initDir();
    }
    
    public Directory(byte[] data){
        this.readFromByteArray(data);
    }

    private void initDir() {
        this.buckets = new int[(int) Math.pow(2, this.profundidade)];

        // na inicialização, o indice criado fará referência aos buckets em ordem
        for (int i = 0; i < buckets.length; i++) {
            this.buckets[i] = i; 
        }
    }
    
    
    public int hashFunction(int valor, int prof) {
        return valor % (int) Math.pow(2, prof);
    }
    
    public int hashFunction (int valor) {
        return hashFunction(valor, this.profundidade);
    }

    private void extendDir(){
        int n = this.buckets.length;
        this.profundidade++;
        int newBuckets[] = new int[n * 2];

        for (int i = 0; i < n; i++ ){
            newBuckets[i] = newBuckets[n+i] = this.buckets[i];
        }
        this.buckets = newBuckets;
    }

    public void extendDir(int newBucket, int address) {
        this.extendDir();
        this.buckets[newBucket] = address;
    }

    public void setProfundidade(int profundidade){
        this.profundidade = profundidade;
    }

    public int getProfundidade(){
        return this.profundidade;
    }

    public void setBucket(int bucketAddress, int posicao){
        this.buckets[posicao] = bucketAddress;
    }

    public int getBucket(int posicao){
        return this.buckets[posicao];
    }

    @Override
    public String toString(){
        String aux = "";
        for(int i : this.buckets){
            aux +=(i +" ");
        }
        aux += "\n";
        return aux;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(this.getProfundidade());
            for(int bucket : this.buckets){ 
                dos.writeInt(bucket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public void readFromByteArray(byte[] v) {
        ByteArrayInputStream bais = new ByteArrayInputStream(v);
        DataInputStream dis = new DataInputStream(bais);
        
        try {
            this.setProfundidade(dis.readInt());

            buckets = new int[(int) Math.pow(2, this.profundidade)];
            
            for(int i = 0; i < Math.pow(2, this.profundidade); i++) {
                this.setBucket(dis.readInt(), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
